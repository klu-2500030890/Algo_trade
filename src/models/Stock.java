package models;

public class Stock {

    private int stockId;
    private String symbol;
    private double price;
    private int volume;
    private double marketCap;

    public Stock(int stockId, String symbol, double price, int volume, double marketCap) {
        this.stockId = stockId;
        this.symbol = symbol;
        this.price = price;
        this.volume = volume;
        this.marketCap = marketCap;
    }

    public int getStockId() {
        return stockId;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getMarketCap() {
        return marketCap;
    }

    public void setMarketCap(double marketCap) {
        this.marketCap = marketCap;
    }

    @Override
    public String toString() {
        return "Stock ID: " + stockId +
                ", Symbol: " + symbol +
                ", Price: " + price +
                ", Volume: " + volume +
                ", Market Cap: " + marketCap;
    }
}