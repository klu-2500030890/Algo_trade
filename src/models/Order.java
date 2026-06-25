package models;

public class Order {

    private int orderId;
    private String stockSymbol;
    private int quantity;
    private String orderType;
    private double price;

    public Order(int orderId, String stockSymbol, int quantity,
                 String orderType, double price) {

        this.orderId = orderId;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.orderType = orderType;
        this.price = price;
    }

    public int getOrderId() {
        return orderId;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getOrderType() {
        return orderType;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Order ID: " + orderId +
                ", Stock: " + stockSymbol +
                ", Quantity: " + quantity +
                ", Type: " + orderType +
                ", Price: " + price;
    }
}