package managers;

import models.Portfolio;
import models.Stock;
import models.Trader;
import java.util.Map;

public class PortfolioManager {

    private Portfolio portfolio = new Portfolio();

    public void addToPortfolio(Stock stock) {
        portfolio.addStock(stock);
    }

    public Portfolio getPortfolio() {
        return portfolio;
    }

    public void viewPortfolio() {
        portfolio.displayPortfolio();
    }

    public void viewPortfolio(Trader trader, StockManager stockManager) {
        System.out.println("\n==========================================================================================");
        System.out.printf(" PORTFOLIO FOR: %-30s | Account ID: %d\n", trader.getTraderName().toUpperCase(), trader.getTraderId());
        System.out.printf(" CASH BALANCE:  $%-30.2f\n", trader.getBalance());
        System.out.println("==========================================================================================");

        Map<String, Portfolio.Position> holdings = portfolio.getHoldings();

        if (holdings.isEmpty()) {
            System.out.println(" No stock positions currently held in your portfolio.");
            System.out.printf(" Total Portfolio Value (Net Worth): $%.2f\n", trader.getBalance());
            System.out.println("==========================================================================================");
            return;
        }

        System.out.printf("| %-10s | %-10s | %-15s | %-15s | %-15s | %-15s | %-12s |\n",
                "Symbol", "Qty Held", "Avg Buy Price", "Total Cost", "Current Price", "Market Value", "P&L (%)");
        System.out.println("+------------+------------+-----------------+-----------------+-----------------+-----------------+--------------+");

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

            // Green for profit, red for loss
            String color = (pnl >= 0) ? "\u001B[32m" : "\u001B[31m";
            String reset = "\u001B[0m";

            System.out.printf("| %-10s | %-10d | $%-14.2f | $%-14.2f | $%-14.2f | $%-14.2f | %s%+-11.2f%%%s |\n",
                    symbol, qty, avgBuyPrice, cost, currentPrice, marketValue, color, pnlPercentage, reset);
        }

        System.out.println("+------------+------------+-----------------+-----------------+-----------------+-----------------+--------------+");
        
        double overallPnL = totalMarketValue - totalCost;
        double overallPnLPercentage = (totalCost > 0) ? (overallPnL / totalCost) * 100.0 : 0.0;
        double netWorth = trader.getBalance() + totalMarketValue;

        String summaryColor = (overallPnL >= 0) ? "\u001B[32m" : "\u001B[31m";
        String reset = "\u001B[0m";

        System.out.printf(" Total Cost of Positions:      $%.2f\n", totalCost);
        System.out.printf(" Market Value of Positions:    $%.2f\n", totalMarketValue);
        System.out.printf(" Total Unrealized profit/loss: %s$%.2f (%+.2f%%)%s\n", summaryColor, overallPnL, overallPnLPercentage, reset);
        System.out.printf(" TOTAL NET WORTH:              $%.2f\n", netWorth);
        System.out.println("==========================================================================================");
    }
}