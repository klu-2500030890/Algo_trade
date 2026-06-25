package utils;

import models.Portfolio;
import models.Stock;
import models.Trader;
import managers.PortfolioManager;
import managers.StockManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class PdfReportGenerator {

    public static void generateReport(String filePath, Trader trader, PortfolioManager portfolioManager, StockManager stockManager) throws IOException {
        PdfHelper pdf = new PdfHelper();

        // Title and Header
        pdf.addLine("=========================================================================", true);
        pdf.addLine("                     ALGO-TRADE PRO PORTFOLIO & STOCK REPORT             ", true);
        pdf.addLine("=========================================================================", true);
        pdf.addEmptyLine();

        // Metadata
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = formatter.format(new Date());
        pdf.addLine("Generated On  : " + dateStr);
        pdf.addLine("Trader Name   : " + trader.getTraderName());
        pdf.addLine("Account ID    : " + trader.getTraderId());
        pdf.addLine("Cash Balance  : $" + String.format("%.2f", trader.getBalance()));
        pdf.addLine("=========================================================================");
        pdf.addEmptyLine();

        // Portfolio section
        pdf.addLine("1. PORTFOLIO STATUS & HOLDINGS", true);
        pdf.addLine("-------------------------------------------------------------------------", true);

        Map<String, Portfolio.Position> holdings = portfolioManager.getPortfolio().getHoldings();

        if (holdings.isEmpty()) {
            pdf.addLine("No stock positions currently held in your portfolio.");
            pdf.addLine("Total Portfolio Value (Net Worth): $" + String.format("%.2f", trader.getBalance()));
        } else {
            // Table Header
            String tableHeader = String.format("%-10s | %-8s | %-12s | %-12s | %-12s | %-12s | %-10s",
                    "Symbol", "Qty Held", "Avg Buy Px", "Total Cost", "Current Px", "Market Val", "P&L (%)");
            pdf.addLine(tableHeader, true);
            pdf.addLine("-----------+----------+--------------+--------------+--------------+--------------+----------");

            double totalCost = 0;
            double totalMarketValue = 0;

            for (Portfolio.Position pos : holdings.values()) {
                String symbol = pos.getSymbol();
                int qty = pos.getQuantity();
                double avgBuyPrice = pos.getAveragePrice();
                double cost = qty * avgBuyPrice;

                Stock marketStock = stockManager.searchStock(symbol);
                double currentPrice = (marketStock != null) ? marketStock.getPrice() : avgBuyPrice;
                double marketValue = qty * currentPrice;

                double pnl = marketValue - cost;
                double pnlPercentage = (cost > 0) ? (pnl / cost) * 100.0 : 0.0;

                totalCost += cost;
                totalMarketValue += marketValue;

                String row = String.format("%-10s | %-8d | $%-11.2f | $%-11.2f | $%-11.2f | $%-11.2f | %+.2f%%",
                        symbol, qty, avgBuyPrice, cost, currentPrice, marketValue, pnlPercentage);
                pdf.addLine(row);
            }

            pdf.addLine("-----------+----------+--------------+--------------+--------------+--------------+----------");
            double overallPnL = totalMarketValue - totalCost;
            double overallPnLPercentage = (totalCost > 0) ? (overallPnL / totalCost) * 100.0 : 0.0;
            double netWorth = trader.getBalance() + totalMarketValue;

            pdf.addLine(String.format("Total Cost of Positions   : $%.2f", totalCost));
            pdf.addLine(String.format("Market Value of Positions : $%.2f", totalMarketValue));
            pdf.addLine(String.format("Total Unrealized P&L      : $%.2f (%+.2f%%)", overallPnL, overallPnLPercentage));
            pdf.addLine(String.format("TOTAL PORTFOLIO NET WORTH : $%.2f", netWorth), true);
        }
        pdf.addLine("=========================================================================");
        pdf.addEmptyLine();

        // Stocks Section
        pdf.addLine("2. MARKET STOCK WATCHLIST", true);
        pdf.addLine("-------------------------------------------------------------------------", true);

        java.util.List<Stock> stocks = stockManager.getStocksList();
        if (stocks.isEmpty()) {
            pdf.addLine("No stocks available in the market desk.");
        } else {
            String stockHeader = String.format("%-6s | %-10s | %-12s | %-12s | %-20s",
                    "ID", "Symbol", "Price ($)", "Volume", "Market Cap ($)");
            pdf.addLine(stockHeader, true);
            pdf.addLine("-------+------------+--------------+--------------+----------------------");

            // Avoid overflowing the PDF with thousands of lines, print up to 50 stocks
            int count = 0;
            for (Stock s : stocks) {
                if (count >= 50) {
                    pdf.addLine("... and " + (stocks.size() - 50) + " more stocks in the database.");
                    break;
                }
                String row = String.format("%-6d | %-10s | $%-11.2f | %-12d | $%-19.2f",
                        s.getStockId(), s.getSymbol(), s.getPrice(), s.getVolume(), s.getMarketCap());
                pdf.addLine(row);
                count++;
            }
        }
        pdf.addLine("=========================================================================");

        // Compile and write PDF
        byte[] pdfBytes = pdf.generatePdf();
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(pdfBytes);
        }
    }
}
