package managers;

import models.Portfolio;
import models.Stock;
import models.Trader;
import java.util.Map;

public class RiskManager {

    public void analyzeRisk() {
        System.out.println("Analyzing Portfolio Risk...");
        System.out.println("Risk Level: Moderate");
    }

    public void analyzeRisk(Trader trader, PortfolioManager portfolioManager, StockManager stockManager) {
        Map<String, Portfolio.Position> holdings = portfolioManager.getPortfolio().getHoldings();
        double cash = trader.getBalance();

        if (holdings.isEmpty()) {
            System.out.println("\nNo stock holdings currently in your portfolio. Risk is extremely low (100% Cash).");
            System.out.printf("Current Cash: $%.2f | Net Worth: $%.2f\n", cash, cash);
            return;
        }

        System.out.println("\n==========================================================================================");
        System.out.println("                                PORTFOLIO RISK ANALYSIS SUITE                             ");
        System.out.println("==========================================================================================");

        double totalHoldingsVal = 0.0;
        for (Portfolio.Position pos : holdings.values()) {
            Stock s = stockManager.searchStock(pos.getSymbol());
            double price = (s != null) ? s.getPrice() : pos.getAveragePrice();
            totalHoldingsVal += pos.getQuantity() * price;
        }
        double netWorth = cash + totalHoldingsVal;

        // 1. Herfindahl-Hirschman Index (HHI) for asset concentration
        double hhi = 0.0;
        System.out.println("Asset Class Allocations:");
        for (Portfolio.Position pos : holdings.values()) {
            Stock s = stockManager.searchStock(pos.getSymbol());
            double price = (s != null) ? s.getPrice() : pos.getAveragePrice();
            double val = pos.getQuantity() * price;
            double allocation = (val / netWorth) * 100.0;
            System.out.printf("  - %-10s: %6.2f%% ($%12.2f)\n", pos.getSymbol(), allocation, val);
            hhi += allocation * allocation;
        }
        double cashAlloc = (cash / netWorth) * 100.0;
        System.out.printf("  - CASH      : %6.2f%% ($%12.2f)\n", cashAlloc, cash);
        hhi += cashAlloc * cashAlloc;

        System.out.printf("\nHerfindahl-Hirschman Index (HHI): %.2f\n", hhi);
        System.out.print("Diversification Rating: ");
        if (hhi < 2000.0) {
            System.out.println("\u001B[32mEXCELLENT (Highly diversified, minimal single-stock exposure)\u001B[0m");
        } else if (hhi < 4000.0) {
            System.out.println("\u001B[33mMODERATE (A few concentrated holdings; keep watch)\u001B[0m");
        } else {
            System.out.println("\u001B[31mPOOR (High concentration risk; recommend rebalancing)\u001B[0m");
        }

        // 2. Value at Risk (VaR)
        // Assume daily volatility is ~2% per stock, mock VaR 95% confidence level is ~1.65 * daily_vol * portfolio_value
        double dailyVolatility = 0.02;
        double confidenceFactor = 1.65; // 95% confidence level
        double dailyVaR = totalHoldingsVal * dailyVolatility * confidenceFactor;
        System.out.printf("\nValue at Risk (95%% Confidence Level, 1-Day Horizon): $%.2f\n", dailyVaR);
        System.out.printf("  (Interpretation: There is a 5%% probability of losing $%.2f or more in a single market day)\n", dailyVaR);

        // 3. Scenario Stress Testing
        System.out.println("\n------------------------------------------------------------------------------------------");
        System.out.println("                               SCENARIO STRESS TESTING PORTFOLIO                          ");
        System.out.println("------------------------------------------------------------------------------------------");
        
        runScenario("Market Crash (-20% broad-market correction)", totalHoldingsVal, netWorth, -0.20);
        runScenario("Growth Sector Rally (+15% technology stock rise)", totalHoldingsVal, netWorth, 0.15);
        runScenario("Interest Rate Hike (-7% discount rate tightening)", totalHoldingsVal, netWorth, -0.07);
        System.out.println("==========================================================================================");
    }

    private void runScenario(String scenarioName, double holdingsVal, double netWorth, double assetImpact) {
        double newHoldingsVal = holdingsVal * (1.0 + assetImpact);
        double impactDelta = newHoldingsVal - holdingsVal;
        double newNetWorth = netWorth + impactDelta;
        double pctChange = (impactDelta / netWorth) * 100.0;

        String color = (pctChange >= 0) ? "\u001B[32m" : "\u001B[31m";
        String reset = "\u001B[0m";

        System.out.printf("Scenario: %s\n", scenarioName);
        System.out.printf("  - Portfolio Value Impact: %s$%-11.2f (%+6.2f%%)%s\n", color, impactDelta, pctChange, reset);
    }
}