package managers;

import models.Order;
import models.Stock;
import models.Trader;
import models.Transaction;

import java.util.LinkedList;
import java.util.Queue;

public class TradingEngine {

    private Queue<Order> orderQueue = new LinkedList<>();

    public void placeOrder(Order order) {
        orderQueue.add(order);
        System.out.println("Order Placed: " + order.getOrderType() + " " + order.getQuantity() + " " + order.getStockSymbol() + " @ " + order.getPrice());
    }

    public void displayQueue() {
        if (orderQueue.isEmpty()) {
            System.out.println("No Pending Orders.");
            return;
        }

        System.out.println("\n+----------+------------+------------+------------+------------+");
        System.out.printf("| %-8s | %-10s | %-10s | %-10s | %-10s |\n", "Order ID", "Symbol", "Type", "Quantity", "Price ($)");
        System.out.println("+----------+------------+------------+------------+------------+");
        for (Order order : orderQueue) {
            System.out.printf("| %-8d | %-10s | %-10s | %-10d | %-10.2f |\n",
                    order.getOrderId(), order.getStockSymbol(), order.getOrderType(), order.getQuantity(), order.getPrice());
        }
        System.out.println("+----------+------------+------------+------------+------------+");
    }

    public void processOrders(StockManager stockManager, PortfolioManager portfolioManager, Trader trader, TransactionHistory transactionHistory) {
        if (orderQueue.isEmpty()) {
            System.out.println("No Orders To Process.");
            return;
        }

        System.out.println("\nProcessing orders...");
        int transactionIdCounter = 5001 + (int)(Math.random() * 1000);

        while (!orderQueue.isEmpty()) {
            Order order = orderQueue.poll();
            String symbol = order.getStockSymbol().toUpperCase();
            Stock marketStock = stockManager.searchStock(symbol);

            if (marketStock == null) {
                System.out.printf("Execution Failed: Stock symbol %s not found in market.\n", symbol);
                continue;
            }

            int qty = order.getQuantity();
            double limitPrice = order.getPrice();
            double totalCost = qty * limitPrice;

            if (order.getOrderType().equalsIgnoreCase("BUY")) {
                if (marketStock.getVolume() < qty) {
                    System.out.printf("Execution Failed: Market has insufficient volume for %s (Requested: %d, Available: %d).\n",
                            symbol, qty, marketStock.getVolume());
                    continue;
                }
                if (trader.getBalance() < totalCost) {
                    System.out.printf("Execution Failed: Insufficient balance. Required: $%.2f, Available: $%.2f.\n",
                            totalCost, trader.getBalance());
                    continue;
                }

                // Execute trade
                trader.deductBalance(totalCost);
                marketStock.setVolume(marketStock.getVolume() - qty);

                // Simulate minor price impact (buying pushes price up)
                double priceImpactRatio = 1.0 + ((double) qty / marketStock.getVolume() * 0.05);
                marketStock.setPrice(marketStock.getPrice() * priceImpactRatio);
                marketStock.setMarketCap(marketStock.getPrice() * 10000000); // update market cap

                // Add to portfolio
                portfolioManager.addToPortfolio(new Stock(marketStock.getStockId(), symbol, limitPrice, qty, marketStock.getMarketCap()));

                // Record transaction
                Transaction transaction = new Transaction(transactionIdCounter++, symbol, qty, totalCost);
                transactionHistory.addTransaction(transaction);

                System.out.printf("Executed BUY: %d shares of %s @ $%.2f. Total Cost: $%.2f.\n",
                        qty, symbol, limitPrice, totalCost);

            } else if (order.getOrderType().equalsIgnoreCase("SELL")) {
                var position = portfolioManager.getPortfolio().getHoldings().get(symbol);
                if (position == null || position.getQuantity() < qty) {
                    System.out.printf("Execution Failed: Insufficient shares of %s in portfolio. Held: %d, Required: %d.\n",
                            symbol, position == null ? 0 : position.getQuantity(), qty);
                    continue;
                }

                // Execute trade
                portfolioManager.getPortfolio().removeShares(symbol, qty);
                trader.creditBalance(totalCost);
                marketStock.setVolume(marketStock.getVolume() + qty);

                // Simulate price impact (selling pushes price down)
                double priceImpactRatio = 1.0 - ((double) qty / marketStock.getVolume() * 0.05);
                marketStock.setPrice(Math.max(1.0, marketStock.getPrice() * priceImpactRatio));
                marketStock.setMarketCap(marketStock.getPrice() * 10000000);

                // Record transaction (negative quantity signifies sell in history)
                Transaction transaction = new Transaction(transactionIdCounter++, symbol, -qty, totalCost);
                transactionHistory.addTransaction(transaction);

                System.out.printf("Executed SELL: %d shares of %s @ $%.2f. Total Value: $%.2f.\n",
                        qty, symbol, limitPrice, totalCost);
            }
        }
    }
}