package utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class DatasetGenerator {

    private static final String[] SECTORS = {
        "TECH", "FIN", "HEALTH", "CONS", "ENERGY", "INDUS", "UTIL", "MAT", "TELE"
    };

    public static void main(String[] args) {
        String path = "stocks_dataset.csv";
        if (args.length > 0) {
            path = args[0];
        }
        generateDataset(path, 1000);
    }

    /**
     * Generates a CSV file containing specified number of stock samples.
     */
    public static void generateDataset(String filePath, int count) {
        Random random = new Random();

        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {
            // Write CSV Header
            writer.println("StockID,Symbol,Price,Volume,MarketCap,Sector");

            for (int i = 1; i <= count; i++) {
                String sector = SECTORS[random.nextInt(SECTORS.length)];
                String symbol = sector + String.format("%03d", i);
                double price = 5.0 + (random.nextDouble() * 2495.0); // Price from $5 to $2500
                int volume = 5000 + random.nextInt(995000);          // Volume from 5,000 to 1,000,000
                double sharesOutstanding = 100000 + random.nextInt(9900000);
                double marketCap = price * sharesOutstanding;        // Market cap based on shares outstanding

                writer.printf("%d,%s,%.2f,%d,%.2f,%s\n", i, symbol, price, volume, marketCap, sector);
            }
            System.out.println("Dataset successfully generated: " + count + " samples saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error generating dataset: " + e.getMessage());
        }
    }
}
