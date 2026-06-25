package models;

public class Transaction {

    private int transactionId;
    private String stockSymbol;
    private int quantity;
    private double amount;

    public Transaction(int transactionId, String stockSymbol,
                        int quantity, double amount) {

        this.transactionId = transactionId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.amount = amount;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        String type = (quantity >= 0) ? "BUY" : "SELL";
        return String.format("TXN ID: %d | Stock: %-6s | Type: %-4s | Qty: %-4d | Amount: $%-8.2f",
                transactionId, stockSymbol, type, Math.abs(quantity), amount);
    }
}