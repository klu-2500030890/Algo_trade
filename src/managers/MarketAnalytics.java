package managers;

import algorithms.HeapSort;
import algorithms.MergeSort;
import algorithms.QuickSort;
import algorithms.LIS;
import datastructures.SegmentTree;
import datastructures.FenwickTree;
import models.Stock;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class MarketAnalytics {
    private static final Random random = new Random();

    public void analyzeMarket() {
        System.out.println("Analyzing Market Trends...");
        System.out.println("Top Performing Stocks Generated");
    }

    public void sortMarketStocks(List<Stock> stocks, int criteria, int algorithm) {
        if (stocks.isEmpty()) {
            System.out.println("No stocks available to sort.");
            return;
        }

        List<Stock> copy = new ArrayList<>(stocks);
        Comparator<Stock> comp;
        String criteriaName;

        switch (criteria) {
            case 1:
                comp = Comparator.comparingDouble(Stock::getPrice);
                criteriaName = "Price";
                break;
            case 2:
                comp = Comparator.comparingInt(Stock::getVolume);
                criteriaName = "Volume";
                break;
            case 3:
                comp = Comparator.comparingDouble(Stock::getMarketCap);
                criteriaName = "Market Cap";
                break;
            default:
                System.out.println("Invalid sorting criteria.");
                return;
        }

        long startTime = System.nanoTime();
        String algoName;

        switch (algorithm) {
            case 1:
                MergeSort.sort(copy, comp);
                algoName = "Merge Sort";
                break;
            case 2:
                QuickSort.sort(copy, comp);
                algoName = "Quick Sort";
                break;
            case 3:
                HeapSort.sort(copy, comp);
                algoName = "Heap Sort";
                break;
            default:
                System.out.println("Invalid sorting algorithm.");
                return;
        }
        long duration = System.nanoTime() - startTime;

        System.out.printf("\nSorted Stocks by %s using %s (Execution Time: %d ns):\n", criteriaName, algoName, duration);
        System.out.println("+----------+------------+------------+------------+---------------+");
        System.out.printf("| %-8s | %-10s | %-10s | %-10s | %-13s |\n", "Stock ID", "Symbol", "Price ($)", "Volume", "Market Cap");
        System.out.println("+----------+------------+------------+------------+---------------+");
        for (Stock stock : copy) {
            System.out.printf("| %-8d | %-10s | %-10.2f | %-10d | %-13.2f |\n",
                    stock.getStockId(), stock.getSymbol(), stock.getPrice(), stock.getVolume(), stock.getMarketCap());
        }
        System.out.println("+----------+------------+------------+------------+---------------+");
    }

    public void analyzeStockBullRun(Stock stock) {
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        int days = 15;
        double[] prices = new double[days];
        prices[0] = stock.getPrice() * 0.90; // start slightly lower

        for (int i = 1; i < days; i++) {
            // Fluctuation: -5% to +8%
            double change = -0.05 + (random.nextDouble() * 0.13);
            prices[i] = prices[i - 1] * (1.0 + change);
        }

        System.out.printf("\nSimulated 15-Day Historical Price Data for %s:\n", stock.getSymbol());
        for (int i = 0; i < days; i++) {
            System.out.printf("Day %02d: $%-8.2f ", i + 1, prices[i]);
            int barCount = (int) (prices[i] / (stock.getPrice() / 15.0));
            for (int b = 0; b < Math.min(barCount, 40); b++) {
                System.out.print("■");
            }
            System.out.println();
        }

        int maxRun = LIS.longestIncreasingSubsequence(prices);
        System.out.printf("\n>>> Bull Run Result: The longest increasing trend was %d days (out of %d days).\n", maxRun, days);
        if (maxRun >= 9) {
            System.out.println("Market Trend Sentiment: STRONGLY BULLISH 🚀");
        } else if (maxRun >= 6) {
            System.out.println("Market Trend Sentiment: MODERATELY BULLISH 📈");
        } else {
            System.out.println("Market Trend Sentiment: BEARISH OR CONSOLIDATING 📉");
        }
    }

    public void runRangeQueries(Stock stock) {
        if (stock == null) {
            System.out.println("Stock not found.");
            return;
        }

        int days = 20;
        double[] prices = new double[days];
        int[] volumes = new int[days];
        prices[0] = stock.getPrice();
        volumes[0] = stock.getVolume() / 20;

        for (int i = 1; i < days; i++) {
            prices[i] = prices[i - 1] * (1.0 + (-0.03 + random.nextDouble() * 0.06));
            volumes[i] = (int) (volumes[i - 1] * (0.8 + random.nextDouble() * 0.45));
        }

        SegmentTree segmentTree = new SegmentTree(prices);
        FenwickTree fenwickTree = new FenwickTree(volumes);

        System.out.printf("\n--- 20-Day Range Query Analysis for %s ---\n", stock.getSymbol());
        System.out.printf("Start Price: $%-8.2f | Start Vol: %-6d\n", prices[0], volumes[0]);
        System.out.printf("Mid Price:   $%-8.2f | Mid Vol:   %-6d\n", prices[9], volumes[9]);
        System.out.printf("End Price:   $%-8.2f | End Vol:   %-6d\n", prices[19], volumes[19]);

        double maxPriceH1 = segmentTree.queryMax(0, 9);
        double maxPriceH2 = segmentTree.queryMax(10, 19);
        int totalVolH1 = fenwickTree.queryRange(0, 9);
        int totalVolH2 = fenwickTree.queryRange(10, 19);

        System.out.println("\nSegment Tree (Max Price) & Fenwick Tree (Cumulative Volume) Query Results:");
        System.out.printf("  [Days 01-10] Max Price: $%-8.2f | Total Volume: %-10d\n", maxPriceH1, totalVolH1);
        System.out.printf("  [Days 11-20] Max Price: $%-8.2f | Total Volume: %-10d\n", maxPriceH2, totalVolH2);
    }
}