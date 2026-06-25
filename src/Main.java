import managers.*;
import models.*;
import algorithms.*;
import datastructures.*;
import utils.InputHelper;

import java.util.ArrayList;
import java.util.List;

public class Main {

    // ANSI Colors for premium terminal UI
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String CYAN = "\u001B[36m";
    public static final String PURPLE = "\u001B[35m";

    public static void main(String[] args) {
        StockManager stockManager = new StockManager();
        TradingEngine tradingEngine = new TradingEngine();
        PortfolioManager portfolioManager = new PortfolioManager();
        TransactionHistory transactionHistory = new TransactionHistory();
        MarketAnalytics marketAnalytics = new MarketAnalytics();
        RiskManager riskManager = new RiskManager();

        // Initialize Trader Profile
        Trader trader = new Trader(101, "Jane Doe", 100000.0);
        int orderIdCounter = 1001;

        printBanner();

        while (true) {
            System.out.println("\n" + BOLD + CYAN + "========================================================================" + RESET);
            System.out.println(BOLD + CYAN + "                       ALGO-TRADE PRO DESKTOP BOARD                     " + RESET);
            System.out.println(BOLD + CYAN + "========================================================================" + RESET);
            System.out.println("  [1] " + BOLD + "MARKET DESK" + RESET + "       : View, Add, and Sort Stock Options");
            System.out.println("  [2] " + BOLD + "TRADING DESK" + RESET + "      : Place Orders, View Queue, and Execute Trades");
            System.out.println("  [3] " + BOLD + "PORTFOLIO PANEL" + RESET + "   : Live Net Worth, Cash, Holdings and P&L");
            System.out.println("  [4] " + BOLD + "LEDGER CENTER" + RESET + "     : Transaction History Audit (B-Tree Indexed)");
            System.out.println("  [5] " + BOLD + "MARKET ANALYTICS" + RESET + "  : Technical Indicators, Bull Runs (LIS), Range Queries");
            System.out.println("  [6] " + BOLD + "PORTFOLIO RISK" + RESET + "    : Stress Testing, Value at Risk (VaR), HHI Index");
            System.out.println("  [7] " + BOLD + "QUANT ALGORITHMS" + RESET + "  : Arbitrage Detection, Knapsack Optimization, Correlation");
            System.out.println("  [8] " + BOLD + "EXIT SYSTEM" + RESET);
            System.out.println(CYAN + "------------------------------------------------------------------------" + RESET);

            int choice = InputHelper.readInt("Select Operations Menu (1-8): ", 1, 8);

            switch (choice) {
                case 1:
                    handleMarketDesk(stockManager, marketAnalytics);
                    break;
                case 2:
                    handleTradingDesk(stockManager, tradingEngine, portfolioManager, trader, transactionHistory, orderIdCounter);
                    orderIdCounter += 10; // Increment order IDs
                    break;
                case 3:
                    portfolioManager.viewPortfolio(trader, stockManager);
                    InputHelper.pressEnterToContinue();
                    break;
                case 4:
                    handleLedgerCenter(transactionHistory);
                    break;
                case 5:
                    handleMarketAnalytics(stockManager, marketAnalytics);
                    break;
                case 6:
                    riskManager.analyzeRisk(trader, portfolioManager, stockManager);
                    InputHelper.pressEnterToContinue();
                    break;
                case 7:
                    handleQuantAlgorithms(stockManager, trader);
                    break;
                case 8:
                    System.out.println(BOLD + GREEN + "\nShutting down AlgoTrade Engine... Good bye!" + RESET);
                    System.exit(0);
            }
        }
    }

    private static void printBanner() {
        System.out.println(CYAN + BOLD + "  ██████╗  ██████╗ ███╗   ███╗██████╗  ██████╗ ██╗  ██╗███████╗" + RESET);
        System.out.println(CYAN + BOLD + "  ██╔═══██╗██╔═══██╗████╗ ████║██╔══██╗██╔═══██╗╚██╗██╔╝██╔════╝" + RESET);
        System.out.println(CYAN + BOLD + "  ██║   ██║██║   ██║██╔████╔██║██████╔╝██║   ██║ ╚███╔╝ █████╗  " + RESET);
        System.out.println(CYAN + BOLD + "  ██║▄▄ ██║██║   ██║██║╚██╔╝██║██╔═══╝ ██║   ██║ ██╔██╗ ██╔══╝  " + RESET);
        System.out.println(CYAN + BOLD + "  ╚██████╔╝╚██████╔╝██║ ╚═╝ ██║██║     ╚██████╔╝██╔╝ ██╗███████╗" + RESET);
        System.out.println(CYAN + BOLD + "   ╚══▀▀═╝  ╚═════╝ ╚═╝     ╚═╝╚═╝      ╚═════╝ ╚═╝  ╚═╝╚══════╝" + RESET);
        System.out.println("                   Institutional Algorithmic Platform v3.5");
    }

    private static void handleMarketDesk(StockManager stockManager, MarketAnalytics marketAnalytics) {
        while (true) {
            System.out.println("\n" + BOLD + BLUE + "--- [1] MARKET OPERATION DESK ---" + RESET);
            System.out.println("  1. View Current Stocks (AVL-Tree alphabetical index)");
            System.out.println("  2. Add Stock Manually");
            System.out.println("  3. Sort Stocks (Price/Volume/Market Cap Benchmarks)");
            System.out.println("  4. Generate & Load 1,000-Stock Dataset (CSV)");
            System.out.println("  5. Back to Main Menu");

            int choice = InputHelper.readInt("Select Market operation (1-5): ", 1, 5);
            if (choice == 5) return;

            switch (choice) {
                case 1:
                    stockManager.displayStocks();
                    InputHelper.pressEnterToContinue();
                    break;
                case 2:
                    System.out.println("\n" + BOLD + "Enter Details for New Stock:" + RESET);
                    String symbol = InputHelper.readString("  Stock Symbol (e.g. INFY): ").toUpperCase();
                    if (symbol.isEmpty()) {
                        System.out.println(RED + "Invalid Symbol." + RESET);
                        break;
                    }
                    double price = InputHelper.readDouble("  Market Price ($): ", 1.0, 100000.0);
                    int volume = InputHelper.readInt("  Available Volume: ", 1, 100000000);
                    double marketCap = price * 10000000.0; // Mock shares outstanding

                    int id = stockManager.getStocksList().size() + 1;
                    stockManager.addStock(new Stock(id, symbol, price, volume, marketCap));
                    InputHelper.pressEnterToContinue();
                    break;
                case 3:
                    System.out.println("\nSelect Sort Criteria:");
                    System.out.println("  1. Price");
                    System.out.println("  2. Volume");
                    System.out.println("  3. Market Cap");
                    int criteria = InputHelper.readInt("Enter sorting criteria (1-3): ", 1, 3);

                    System.out.println("\nSelect Sorting Algorithm:");
                    System.out.println("  1. Merge Sort");
                    System.out.println("  2. Quick Sort");
                    System.out.println("  3. Heap Sort");
                    int algorithm = InputHelper.readInt("Enter sorting algorithm (1-3): ", 1, 3);

                    marketAnalytics.sortMarketStocks(stockManager.getStocksList(), criteria, algorithm);
                    InputHelper.pressEnterToContinue();
                    break;
                case 4:
                    String datasetPath = "stocks_dataset.csv";
                    System.out.println("\nGenerating 1,000 stocks dataset in " + datasetPath + "...");
                    utils.DatasetGenerator.generateDataset(datasetPath, 1000);
                    stockManager.loadStocksFromCSV(datasetPath);
                    InputHelper.pressEnterToContinue();
                    break;
            }
        }
    }

    private static void handleTradingDesk(StockManager stockManager, TradingEngine tradingEngine,
                                         PortfolioManager portfolioManager, Trader trader,
                                         TransactionHistory transactionHistory, int orderIdCounter) {
        while (true) {
            System.out.println("\n" + BOLD + PURPLE + "--- [2] TRADING DESK OPERATION ---" + RESET);
            System.out.println("  1. Place Order");
            System.out.println("  2. View Pending Orders Queue");
            System.out.println("  3. Execute Pending Order Queue");
            System.out.println("  4. Back to Main Menu");

            int choice = InputHelper.readInt("Select Trading operation (1-4): ", 1, 4);
            if (choice == 4) return;

            switch (choice) {
                case 1:
                    System.out.println("\n" + BOLD + "Place a Buy/Sell Limit Order:" + RESET);
                    String symbol = InputHelper.readString("  Stock Symbol: ").toUpperCase();
                    Stock s = stockManager.searchStock(symbol);
                    if (s == null) {
                        System.out.println(RED + "Error: Stock not found in market." + RESET);
                        break;
                    }

                    System.out.println("  Selected Stock Current Details: " + s);
                    String type = InputHelper.readString("  Order Type (BUY/SELL): ").toUpperCase();
                    if (!type.equals("BUY") && !type.equals("SELL")) {
                        System.out.println(RED + "Error: Type must be BUY or SELL." + RESET);
                        break;
                    }

                    int qty = InputHelper.readInt("  Quantity of Shares: ", 1, 1000000);
                    double limitPrice = InputHelper.readDouble("  Limit Price per Share ($): ", 0.01, 100000.0);

                    Order order = new Order(orderIdCounter++, symbol, qty, type, limitPrice);
                    tradingEngine.placeOrder(order);
                    InputHelper.pressEnterToContinue();
                    break;
                case 2:
                    tradingEngine.displayQueue();
                    InputHelper.pressEnterToContinue();
                    break;
                case 3:
                    tradingEngine.processOrders(stockManager, portfolioManager, trader, transactionHistory);
                    InputHelper.pressEnterToContinue();
                    break;
            }
        }
    }

    private static void handleLedgerCenter(TransactionHistory transactionHistory) {
        while (true) {
            System.out.println("\n" + BOLD + YELLOW + "--- [4] LEDGER AUDIT CENTER ---" + RESET);
            System.out.println("  1. Display Chronological Transaction Ledger");
            System.out.println("  2. Search Transaction by ID (B-Tree indexed lookup)");
            System.out.println("  3. Back to Main Menu");

            int choice = InputHelper.readInt("Select Ledger operation (1-3): ", 1, 3);
            if (choice == 3) return;

            switch (choice) {
                case 1:
                    transactionHistory.displayTransactions();
                    InputHelper.pressEnterToContinue();
                    break;
                case 2:
                    int id = InputHelper.readInt("Enter Transaction ID to search: ", 1, 99999);
                    Transaction t = transactionHistory.searchTransaction(id);
                    if (t == null) {
                        System.out.println(RED + "Transaction ID not found in B-Tree index." + RESET);
                    } else {
                        System.out.println(GREEN + "\nTransaction found in B-Tree (Order-3 Index):" + RESET);
                        System.out.println("  " + t);
                    }
                    InputHelper.pressEnterToContinue();
                    break;
            }
        }
    }

    private static void handleMarketAnalytics(StockManager stockManager, MarketAnalytics marketAnalytics) {
        while (true) {
            System.out.println("\n" + BOLD + CYAN + "--- [5] MARKET TECHNICAL ANALYTICS ---" + RESET);
            System.out.println("  1. Analyze Stock Bull Run (LIS chart analysis)");
            System.out.println("  2. Run Price Range Query (Segment Tree / Fenwick Tree)");
            System.out.println("  3. Back to Main Menu");

            int choice = InputHelper.readInt("Select Analytics option (1-3): ", 1, 3);
            if (choice == 3) return;

            String symbol = InputHelper.readString("Enter Stock Symbol: ").toUpperCase();
            Stock s = stockManager.searchStock(symbol);
            if (s == null) {
                System.out.println(RED + "Error: Stock not found in market." + RESET);
                break;
            }

            switch (choice) {
                case 1:
                    marketAnalytics.analyzeStockBullRun(s);
                    InputHelper.pressEnterToContinue();
                    break;
                case 2:
                    marketAnalytics.runRangeQueries(s);
                    InputHelper.pressEnterToContinue();
                    break;
            }
        }
    }

    private static void handleQuantAlgorithms(StockManager stockManager, Trader trader) {
        while (true) {
            System.out.println("\n" + BOLD + GREEN + "--- [7] QUANT ALGORITHMIC DESK ---" + RESET);
            System.out.println("  1. Arbitrage Detection Cycle (Bellman-Ford on FX Graph)");
            System.out.println("  2. Portfolio Return Optimization (0/1 Knapsack allocation)");
            System.out.println("  3. Stock Price Correlation Pathway (Dijkstra's shortest path)");
            System.out.println("  4. Back to Main Menu");

            int choice = InputHelper.readInt("Select Algorithm (1-4): ", 1, 4);
            if (choice == 4) return;

            switch (choice) {
                case 1:
                    runArbitrageDetection();
                    break;
                case 2:
                    runKnapsackOptimization(stockManager, trader);
                    break;
                case 3:
                    runDijkstraCorrelationPath(stockManager);
                    break;
            }
        }
    }

    private static void runArbitrageDetection() {
        System.out.println("\n" + BOLD + "Running Foreign Exchange Arbitrage Detection (Bellman-Ford)..." + RESET);
        
        // Define currencies: 0=USD, 1=EUR, 2=GBP, 3=JPY, 4=INR
        int numCurrencies = 5;
        MarketGraph fxGraph = new MarketGraph(numCurrencies);
        fxGraph.addNodeName("USD");
        fxGraph.addNodeName("EUR");
        fxGraph.addNodeName("GBP");
        fxGraph.addNodeName("JPY");
        fxGraph.addNodeName("INR");

        // Exchange rates: let's build an arbitrage loop
        // USD -> EUR = 0.92, EUR -> GBP = 0.86, GBP -> USD = 1.28
        // Arbitrage loop: USD -> EUR -> GBP -> USD: 0.92 * 0.86 * 1.28 = 1.0125 (1.25% profit)
        List<BellmanFord.Edge> edges = new ArrayList<>();
        
        // Populate graph edges
        edges.add(new BellmanFord.Edge(0, 1, 0.92));  // USD -> EUR
        edges.add(new BellmanFord.Edge(1, 2, 0.86));  // EUR -> GBP
        edges.add(new BellmanFord.Edge(2, 0, 1.28));  // GBP -> USD (Arbitrage path)
        edges.add(new BellmanFord.Edge(2, 3, 192.5)); // GBP -> JPY
        edges.add(new BellmanFord.Edge(3, 0, 0.0062));// JPY -> USD
        edges.add(new BellmanFord.Edge(0, 4, 83.5));  // USD -> INR
        edges.add(new BellmanFord.Edge(4, 3, 1.85));  // INR -> JPY

        System.out.println("Simulated Forex Matrix Exchange Rates:");
        for (BellmanFord.Edge e : edges) {
            System.out.printf("  %s -> %s: Rate = %.4f\n", 
                    fxGraph.getNodeName(e.u), fxGraph.getNodeName(e.v), e.rate);
        }

        List<Integer> arbitrageLoop = BellmanFord.findArbitrageLoop(numCurrencies, edges);

        if (arbitrageLoop != null && !arbitrageLoop.isEmpty()) {
            System.out.println(GREEN + "\n[!] OPPORTUNITY IDENTIFIED: Negative-weight cycle detected in logs!" + RESET);
            System.out.print("Arbitrage Loop Path: ");
            double expectedReturnMultiplier = 1.0;
            for (int i = 0; i < arbitrageLoop.size(); i++) {
                int u = arbitrageLoop.get(i);
                System.out.print(fxGraph.getNodeName(u));
                if (i < arbitrageLoop.size() - 1) {
                    System.out.print(" -> ");
                    int v = arbitrageLoop.get(i + 1);
                    // find exchange rate
                    for (BellmanFord.Edge e : edges) {
                        if (e.u == u && e.v == v) {
                            expectedReturnMultiplier *= e.rate;
                            break;
                        }
                    }
                }
            }
            System.out.println();
            System.out.printf("Estimated Profit Margin: %s+%.4f%%%s (Return multiplier: %.4fx)\n", 
                    GREEN, (expectedReturnMultiplier - 1.0) * 100.0, RESET, expectedReturnMultiplier);
        } else {
            System.out.println(YELLOW + "\nNo arbitrage loops detected. Market is currently efficient." + RESET);
        }
        InputHelper.pressEnterToContinue();
    }

    private static void runKnapsackOptimization(StockManager stockManager, Trader trader) {
        System.out.println("\n" + BOLD + "Running Portfolio Optimization via 0/1 Knapsack..." + RESET);
        System.out.printf("Current Trader Cash Budget: $%.2f\n", trader.getBalance());
        
        double budgetInput = InputHelper.readDouble("Enter cash budget limit for optimization ($): ", 10.0, trader.getBalance());

        List<Stock> marketStocks = stockManager.getStocksList();
        List<Stock> recommended = Knapsack.optimizePortfolio(marketStocks, budgetInput);

        if (recommended.isEmpty()) {
            System.out.println(YELLOW + "No stocks fit within the specified budget threshold." + RESET);
        } else {
            System.out.println(GREEN + "\nOptimized Stock Recommendations:" + RESET);
            System.out.println("+----------+------------+------------+--------------------+");
            System.out.printf("| %-8s | %-10s | %-10s | %-18s |\n", "Stock ID", "Symbol", "Cost ($)", "Simulated Return %");
            System.out.println("+----------+------------+------------+--------------------+");
            double totalCost = 0;
            for (Stock s : recommended) {
                double mockReturnPct = (((s.getStockId() * 7 + s.getSymbol().length() * 13) % 16) + 5);
                System.out.printf("| %-8d | %-10s | $%-9.2f | %-18s |\n", 
                        s.getStockId(), s.getSymbol(), s.getPrice(), String.format("+%.2f%%", mockReturnPct));
                totalCost += s.getPrice();
            }
            System.out.println("+----------+------------+------------+--------------------+");
            System.out.printf("Total Cash Required: $%.2f | Remaining Budget: $%.2f\n", totalCost, (budgetInput - totalCost));
        }
        InputHelper.pressEnterToContinue();
    }

    private static void runDijkstraCorrelationPath(StockManager stockManager) {
        System.out.println("\n" + BOLD + "Finding Shortest Price-Correlation Influence Path (Dijkstra)..." + RESET);
        
        // Define stock nodes: 0=RELIANCE, 1=TCS, 2=INFY, 3=HDFCBANK, 4=WIPRO
        int n = 5;
        MarketGraph stockGraph = new MarketGraph(n);
        stockGraph.addNodeName("RELIANCE"); // 0
        stockGraph.addNodeName("TCS");      // 1
        stockGraph.addNodeName("INFY");     // 2
        stockGraph.addNodeName("HDFCBANK"); // 3
        stockGraph.addNodeName("WIPRO");    // 4

        // Build adjacency matrix representing correlation distance = (1.0 - correlation_coefficient)
        // High correlation = lower distance. Shortest path = strongest correlation pipeline.
        double[][] matrix = stockGraph.getAdjacencyMatrix();
        
        // Connections (undirected correlation distance mapping)
        stockGraph.addEdge(0, 3, 0.40); // RELIANCE -> HDFCBANK (Financials link)
        stockGraph.addEdge(3, 0, 0.40);
        
        stockGraph.addEdge(1, 2, 0.20); // TCS -> INFY (High IT correlation)
        stockGraph.addEdge(2, 1, 0.20);

        stockGraph.addEdge(2, 4, 0.30); // INFY -> WIPRO (IT Mid-cap correlation)
        stockGraph.addEdge(4, 2, 0.30);

        stockGraph.addEdge(3, 1, 0.60); // HDFCBANK -> TCS
        stockGraph.addEdge(1, 3, 0.60);

        stockGraph.addEdge(0, 2, 0.90); // RELIANCE -> INFY (Low correlation)
        stockGraph.addEdge(2, 0, 0.90);

        System.out.println("Available Nodes in Correlation Graph: ");
        for (int i = 0; i < n; i++) {
            System.out.printf("  [%d] %s\n", i, stockGraph.getNodeName(i));
        }

        System.out.println("\nActive High-Correlation Bridges (Distance = 1.0 - Correlation):");
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double w = stockGraph.getEdgeWeight(i, j);
                if (w > 0) {
                    System.out.printf("  %s <-> %s: Distance = %.2f (Correlation = %.2f)\n", 
                            stockGraph.getNodeName(i), stockGraph.getNodeName(j), w, (1.0 - w));
                }
            }
        }

        int source = InputHelper.readInt("\nEnter Source Node index (0-4): ", 0, 4);
        int target = InputHelper.readInt("Enter Target Node index (0-4): ", 0, 4);

        if (source == target) {
            System.out.println("Source and Target are the same.");
            InputHelper.pressEnterToContinue();
            return;
        }

        List<Integer> path = DijkstraAlgorithm.findShortestPath(matrix, source, target);

        if (path.isEmpty()) {
            System.out.println(RED + "No path of correlation exists between these assets." + RESET);
        } else {
            System.out.println(GREEN + "\n[!] Path of Strongest Correlation Influence Found:" + RESET);
            System.out.print("  Path: ");
            double totalDist = 0;
            for (int i = 0; i < path.size(); i++) {
                int u = path.get(i);
                System.out.print(stockGraph.getNodeName(u));
                if (i < path.size() - 1) {
                    System.out.print(" -> ");
                    totalDist += stockGraph.getEdgeWeight(u, path.get(i + 1));
                }
            }
            System.out.println();
            System.out.printf("  Cumulative Distance: %.2f | Average Correlation: %.2f\n", 
                    totalDist, (1.0 - (totalDist / (path.size() - 1))));
        }
        InputHelper.pressEnterToContinue();
    }
}