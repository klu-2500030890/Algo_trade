package models;

public class Trader {

    private int traderId;
    private String traderName;
    private double balance;

    public Trader(int traderId, String traderName, double balance) {
        this.traderId = traderId;
        this.traderName = traderName;
        this.balance = balance;
    }

    public int getTraderId() {
        return traderId;
    }

    public String getTraderName() {
        return traderName;
    }

    public double getBalance() {
        return balance;
    }

    public boolean deductBalance(double amount) {
        if (amount > balance) {
            return false;
        }
        balance -= amount;
        return true;
    }

    public void creditBalance(double amount) {
        balance += amount;
    }

    @Override
    public String toString() {
        return "Trader ID: " + traderId +
                ", Name: " + traderName +
                ", Balance: " + balance;
    }
}