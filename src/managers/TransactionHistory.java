package managers;

import datastructures.BTree;
import models.Transaction;
import java.util.ArrayList;

public class TransactionHistory {

    private ArrayList<Transaction> transactions = new ArrayList<>();
    private BTree bTree = new BTree();

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        bTree.insert(transaction.getTransactionId(), transaction);
    }

    public void displayTransactions() {
        if (transactions.isEmpty()) {
            System.out.println("No Transactions Found.");
            return;
        }

        System.out.println("\n+-------------------------------------------------------------------------+");
        System.out.println("|                           TRANSACTION LEDGER                            |");
        System.out.println("+------------+------------+------------+------------+---------------------+");
        System.out.printf("| %-10s | %-10s | %-10s | %-10s | %-19s |\n", "Txn ID", "Symbol", "Type", "Quantity", "Total Amount ($)");
        System.out.println("+------------+------------+------------+------------+---------------------+");
        for (Transaction t : transactions) {
            String type = (t.getQuantity() >= 0) ? "BUY" : "SELL";
            System.out.printf("| %-10d | %-10s | %-10s | %-10d | $%-18.2f |\n",
                    t.getTransactionId(), t.getStockSymbol(), type, Math.abs(t.getQuantity()), t.getAmount());
        }
        System.out.println("+-------------------------------------------------------------------------+");
    }

    public Transaction searchTransaction(int txnId) {
        // O(log N) lookup in B-Tree
        return bTree.search(txnId);
    }
}