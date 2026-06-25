package managers;

import datastructures.AVLTree;
import models.Stock;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StockManager {

    private ArrayList<Stock> stocks = new ArrayList<>();
    private AVLTree avlTree = new AVLTree();

    public StockManager() {
        // Initialize with default stocks for trading
        addStockInternal(new Stock(1, "RELIANCE", 2450.0, 500000, 15000000.0));
        addStockInternal(new Stock(2, "TCS", 3400.0, 300000, 12000000.0));
        addStockInternal(new Stock(3, "INFY", 1500.0, 400000, 6000000.0));
        addStockInternal(new Stock(4, "HDFCBANK", 1650.0, 600000, 9000000.0));
        addStockInternal(new Stock(5, "WIPRO", 400.0, 800000, 2000000.0));
    }

    private void addStockInternal(Stock stock) {
        stocks.add(stock);
        avlTree.insert(stock.getSymbol(), stock);
    }

    public void addStock(Stock stock) {
        if (searchStock(stock.getSymbol()) != null) {
            System.out.println("Error: Stock with symbol " + stock.getSymbol().toUpperCase() + " already exists.");
            return;
        }
        addStockInternal(stock);
        System.out.println("Stock '" + stock.getSymbol().toUpperCase() + "' Added Successfully (Indexed in AVL Tree)!");
    }

    public void displayStocks() {
        List<Stock> sortedStocks = new ArrayList<>();
        avlTree.getInOrderStocks(sortedStocks);

        if (sortedStocks.isEmpty()) {
            System.out.println("No Stocks Available");
            return;
        }

        System.out.println("\n+----------+------------+------------+------------+---------------+");
        System.out.printf("| %-8s | %-10s | %-10s | %-10s | %-13s |\n", "Stock ID", "Symbol", "Price ($)", "Volume", "Market Cap");
        System.out.println("+----------+------------+------------+------------+---------------+");
        // Limit display to first 25 stocks if there are too many (e.g. 1000) to keep console readable
        int count = 0;
        for (Stock stock : sortedStocks) {
            if (count >= 30) {
                System.out.printf("| ... and %d more stocks in the AVL Tree ...\n", sortedStocks.size() - count);
                break;
            }
            System.out.printf("| %-8d | %-10s | %-10.2f | %-10d | %-13.2f |\n",
                    stock.getStockId(), stock.getSymbol(), stock.getPrice(), stock.getVolume(), stock.getMarketCap());
            count++;
        }
        System.out.println("+----------+------------+------------+------------+---------------+");
        System.out.println("Total Stocks Indexed: " + sortedStocks.size());
    }

    public Stock searchStock(String symbol) {
        // Fast search using AVL Tree
        return avlTree.search(symbol);
    }

    public ArrayList<Stock> getStocksList() {
        return stocks;
    }

    /**
     * Loads stocks from a CSV file into the database.
     */
    public void loadStocksFromCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Read header line
            int count = 0;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 5) {
                    int id = Integer.parseInt(tokens[0]);
                    String symbol = tokens[1];
                    double price = Double.parseDouble(tokens[2]);
                    int volume = Integer.parseInt(tokens[3]);
                    double marketCap = Double.parseDouble(tokens[4]);

                    // Check if stock already exists to prevent duplicate symbols
                    if (searchStock(symbol) == null) {
                        Stock stock = new Stock(id, symbol, price, volume, marketCap);
                        addStockInternal(stock);
                        count++;
                    }
                }
            }
            System.out.println("Loaded " + count + " new stocks from CSV dataset into the AVL Tree index!");
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error loading stocks from CSV: " + e.getMessage());
        }
    }
}