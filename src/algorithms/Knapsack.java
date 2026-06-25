package algorithms;

import models.Stock;
import java.util.ArrayList;
import java.util.List;

public class Knapsack {

    /**
     * Solves the 0/1 Knapsack problem to select stocks that fit within the budget
     * and maximize expected returns.
     * Price is treated as weight, and simulated expected profit is treated as value.
     */
    public static List<Stock> optimizePortfolio(List<Stock> stocks, double budget) {
        int n = stocks.size();
        int W = (int) budget;

        if (n == 0 || W <= 0) {
            return new ArrayList<>();
        }

        int[] wt = new int[n];
        int[] val = new int[n];

        for (int i = 0; i < n; i++) {
            Stock s = stocks.get(i);
            wt[i] = (int) Math.max(1, s.getPrice());
            // Mock expected return percentage: between 5% and 20%
            double returnPercentage = (((s.getStockId() * 7 + s.getSymbol().length() * 13) % 16) + 5) / 100.0;
            val[i] = (int) Math.max(1, wt[i] * returnPercentage);
        }

        int[][] dp = new int[n + 1][W + 1];

        // Build DP table
        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= W; w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = 0;
                } else if (wt[i - 1] <= w) {
                    dp[i][w] = Math.max(val[i - 1] + dp[i - 1][w - wt[i - 1]], dp[i - 1][w]);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        // Trace back the selected items
        List<Stock> selected = new ArrayList<>();
        int w = W;
        for (int i = n; i > 0 && w > 0; i--) {
            if (dp[i][w] != dp[i - 1][w]) {
                selected.add(stocks.get(i - 1));
                w -= wt[i - 1];
            }
        }

        return selected;
    }
}
