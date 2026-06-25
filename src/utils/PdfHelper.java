package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class PdfHelper {
    public static class Line {
        public final String text;
        public final boolean isBold;
        public final int indent;

        public Line(String text, boolean isBold, int indent) {
            this.text = text;
            this.isBold = isBold;
            this.indent = indent;
        }
    }

    private final List<Line> lines = new ArrayList<>();

    public void addLine(String text) {
        lines.add(new Line(text, false, 0));
    }

    public void addLine(String text, boolean isBold) {
        lines.add(new Line(text, isBold, 0));
    }

    public void addLine(String text, boolean isBold, int indent) {
        lines.add(new Line(text, isBold, indent));
    }

    public void addEmptyLine() {
        lines.add(new Line("", false, 0));
    }

    public byte[] generatePdf() {
        // A4 height = 842. Margin = 50. Usable height = 742.
        // Line height = 15. Max lines per page = 48.
        List<List<Line>> pages = new ArrayList<>();
        List<Line> currentPage = new ArrayList<>();
        pages.add(currentPage);
        
        int lineCountOnPage = 0;
        for (Line line : lines) {
            if (lineCountOnPage >= 48) {
                currentPage = new ArrayList<>();
                pages.add(currentPage);
                lineCountOnPage = 0;
            }
            currentPage.add(line);
            lineCountOnPage++;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        List<Integer> offsets = new ArrayList<>();
        
        try {
            // Write PDF Header
            bos.write("%PDF-1.4\n%\u00E2\u00E3\u00CF\u00D3\n".getBytes(StandardCharsets.UTF_8));
            
            int numPages = pages.size();
            
            // Object 1: Catalog
            offsets.add(bos.size());
            bos.write("1 0 obj\n<< /Type /Catalog /Pages 2 0 R >>\nendobj\n".getBytes(StandardCharsets.UTF_8));
            
            // Object 2: Pages Catalog
            offsets.add(bos.size());
            StringBuilder kids = new StringBuilder();
            for (int i = 0; i < numPages; i++) {
                int pageObjId = 5 + i * 2;
                kids.append(pageObjId).append(" 0 R ");
            }
            bos.write(("2 0 obj\n<< /Type /Pages /Kids [ " + kids.toString().trim() + " ] /Count " + numPages + " >>\nendobj\n").getBytes(StandardCharsets.UTF_8));
            
            // Object 3: Font Regular (Helvetica)
            offsets.add(bos.size());
            bos.write("3 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>\nendobj\n".getBytes(StandardCharsets.UTF_8));
            
            // Object 4: Font Bold (Helvetica-Bold)
            offsets.add(bos.size());
            bos.write("4 0 obj\n<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica-Bold >>\nendobj\n".getBytes(StandardCharsets.UTF_8));
            
            // Generate content streams for each page
            for (int i = 0; i < numPages; i++) {
                List<Line> pageLines = pages.get(i);
                
                ByteArrayOutputStream streamBos = new ByteArrayOutputStream();
                streamBos.write("BT\n".getBytes(StandardCharsets.UTF_8));
                
                int y = 780; // Start y coordinate near top
                
                streamBos.write("/F1 10 Tf\n".getBytes(StandardCharsets.UTF_8));
                
                for (Line line : pageLines) {
                    if (line.text.isEmpty()) {
                        y -= 15;
                        continue;
                    }
                    
                    String escaped = escapePdfText(line.text);
                    int x = 50 + line.indent;
                    
                    if (line.isBold) {
                        streamBos.write("/F2 10 Tf\n".getBytes(StandardCharsets.UTF_8));
                    } else {
                        streamBos.write("/F1 10 Tf\n".getBytes(StandardCharsets.UTF_8));
                    }
                    
                    // Absolute positioning translation
                    streamBos.write((x + " " + y + " Td\n").getBytes(StandardCharsets.UTF_8));
                    streamBos.write(("(" + escaped + ") Tj\n").getBytes(StandardCharsets.UTF_8));
                    streamBos.write(((-x) + " " + (-y) + " Td\n").getBytes(StandardCharsets.UTF_8));
                    
                    y -= 15;
                }
                
                // Add page number at bottom
                streamBos.write("/F1 9 Tf\n".getBytes(StandardCharsets.UTF_8));
                String footerText = "Page " + (i + 1) + " of " + numPages;
                int footerX = 270;
                int footerY = 35;
                streamBos.write((footerX + " " + footerY + " Td\n").getBytes(StandardCharsets.UTF_8));
                streamBos.write(("(" + footerText + ") Tj\n").getBytes(StandardCharsets.UTF_8));
                
                streamBos.write("ET\n".getBytes(StandardCharsets.UTF_8));
                byte[] streamBytes = streamBos.toByteArray();
                
                int pageObjId = 5 + i * 2;
                int contentObjId = 6 + i * 2;
                
                // Page object
                offsets.add(bos.size());
                bos.write((pageObjId + " 0 obj\n<< /Type /Page /Parent 2 0 R /MediaBox [ 0 0 595 842 ] /Resources << /Font << /F1 3 0 R /F2 4 0 R >> >> /Contents " + contentObjId + " 0 R >>\nendobj\n").getBytes(StandardCharsets.UTF_8));
                
                // Content stream object
                offsets.add(bos.size());
                bos.write((contentObjId + " 0 obj\n<< /Length " + streamBytes.length + " >>\nstream\n").getBytes(StandardCharsets.UTF_8));
                bos.write(streamBytes);
                bos.write("\nendstream\nendobj\n".getBytes(StandardCharsets.UTF_8));
            }
            
            // Xref table
            int xrefOffset = bos.size();
            bos.write("xref\n".getBytes(StandardCharsets.UTF_8));
            bos.write(("0 " + (offsets.size() + 1) + "\n").getBytes(StandardCharsets.UTF_8));
            bos.write("0000000000 65535 f \n".getBytes(StandardCharsets.UTF_8));
            for (int offset : offsets) {
                bos.write(String.format("%010d 00000 n \n", offset).getBytes(StandardCharsets.UTF_8));
            }
            
            // Trailer
            bos.write("trailer\n".getBytes(StandardCharsets.UTF_8));
            bos.write(("<< /Size " + (offsets.size() + 1) + " /Root 1 0 R >>\n").getBytes(StandardCharsets.UTF_8));
            bos.write("startxref\n".getBytes(StandardCharsets.UTF_8));
            bos.write((xrefOffset + "\n").getBytes(StandardCharsets.UTF_8));
            bos.write("%%EOF\n".getBytes(StandardCharsets.UTF_8));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return bos.toByteArray();
    }

    private String escapePdfText(String text) {
        if (text == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(' || c == ')') {
                sb.append('\\').append(c);
            } else if (c == '\\') {
                sb.append("\\\\");
            } else {
                sb.append(c);
            }
        }
        return sb.toString();
    }
}
