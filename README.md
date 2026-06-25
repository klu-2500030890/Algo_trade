# Algo_Trade Pro System

An institutional-grade algorithmic trading console application featuring self-balancing data structures, quantitative algorithms, portfolio risk suites, dynamic order books, and transaction ledger indexing.

## Features
1. **Market Desk**: Alphabetic display of stock tickers using AVL Tree in-order traversal, manual stock addition, and real-time sorting (by Price, Volume, or Market Cap) benchmarked across three sorting algorithms.
2. **Trading Desk**: Interactive order desk to queue buy/sell limit orders, view pending queues, and batch-execute them with cash deductions, portfolio position tracking, and market price impact adjustments.
3. **Portfolio Panel**: Comprehensive client asset evaluation showing cash balance, average cost prices, real-time market prices, unrealized profits/losses, and total net worth.
4. **Ledger Center**: Audit logging of transaction logs indexed in a custom B-Tree structure for fast key searches.
5. **Market Technical Analytics**: Advanced data queries including trend analysis (LIS) and range-query indexing (Segment & Fenwick Trees).
6. **Portfolio Risk Panel**: Risk evaluation covering concentration checks (HHI), Value-at-Risk (VaR) calculations, and macroeconomic stress simulations.
7. **Quant Algorithmic Tools**: Advanced institutional-grade quantitative algorithms for trading:
   - **Bellman-Ford**: Negative-log cycle detection for FX arbitrage opportunities.
   - **Dijkstra**: Shortest price-correlation influence path between stocks.
   - **0/1 Knapsack**: Optimal portfolio allocation maximizing expected return under cash constraints.

## Tech Stack & Data Structures
- **Language**: Java 8+
- **Self-Balancing Trees**: AVL Tree (Alphabetical Ticker Index), B-Tree (Order 3 Transaction Index)
- **Range Queries**: Segment Tree (Range Max Price), Fenwick Tree (Range Sum Volume)
- **Graphs**: MarketGraph (FX Nodes, Stock correlation connections)

## How to Build & Run
1. **Compilation**:
   ```bash
   javac -d out src/models/*.java src/datastructures/*.java src/algorithms/*.java src/utils/*.java src/managers/*.java src/Main.java
   ```
2. **Run Application**:
   ```bash
   java -cp out Main
   ```
3. **Generate standalone 1,000-Stock Dataset**:
   ```bash
   java -cp out utils.DatasetGenerator
   ```
