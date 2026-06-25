package models;

import java.util.HashMap;
import java.util.Map;

public class Portfolio {

    public static class Position {
        private String symbol;
        private int quantity;
        private double averagePrice;

        public Position(String symbol, int quantity, double averagePrice) {
            this.symbol = symbol;
            this.quantity = quantity;
            this.averagePrice = averagePrice;
        }

        public String getSymbol() {
            return symbol;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getAveragePrice() {
            return averagePrice;
        }

        public void buy(int qty, double price) {
            double totalCost = (this.quantity * this.averagePrice) + (qty * price);
            this.quantity += qty;
            this.averagePrice = totalCost / this.quantity;
        }

        public boolean sell(int qty) {
            if (qty > this.quantity) {
                return false;
            }
            this.quantity -= qty;
            return true;
        }
    }

    private Map<String, Position> holdings = new HashMap<>();

    public void addStock(Stock stock) {
        addShares(stock.getSymbol(), stock.getVolume(), stock.getPrice());
    }

    public void addShares(String symbol, int quantity, double price) {
        Position pos = holdings.get(symbol.toUpperCase());
        if (pos == null) {
            holdings.put(symbol.toUpperCase(), new Position(symbol.toUpperCase(), quantity, price));
        } else {
            pos.buy(quantity, price);
        }
    }

    public boolean removeShares(String symbol, int quantity) {
        Position pos = holdings.get(symbol.toUpperCase());
        if (pos == null || pos.getQuantity() < quantity) {
            return false;
        }
        pos.sell(quantity);
        if (pos.getQuantity() == 0) {
            holdings.remove(symbol.toUpperCase());
        }
        return true;
    }

    public Map<String, Position> getHoldings() {
        return holdings;
    }

    public void displayPortfolio() {
        if (holdings.isEmpty()) {
            System.out.println("Portfolio Empty");
            return;
        }

        System.out.printf("| %-10s | %-10s | %-12s | %-12s |\n", "Symbol", "Quantity", "Avg Price", "Total Cost");
        System.out.println("---------------------------------------------------------");
        for (Position pos : holdings.values()) {
            System.out.printf("| %-10s | %-10d | %-12.2f | %-12.2f |\n",
                    pos.getSymbol(), pos.getQuantity(), pos.getAveragePrice(), (pos.getQuantity() * pos.getAveragePrice()));
        }
    }
}