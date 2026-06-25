package datastructures;

import models.Stock;
import java.util.List;

public class AVLTree {

    public static class AVLNode {
        public String key;
        public Stock value;
        public int height;
        public AVLNode left, right;

        public AVLNode(String key, Stock value) {
            this.key = key;
            this.value = value;
            this.height = 1;
        }
    }

    private AVLNode root;

    public AVLNode getRoot() {
        return root;
    }

    public void insert(String key, Stock value) {
        root = insert(root, key.toUpperCase(), value);
    }

    public Stock search(String key) {
        AVLNode node = search(root, key.toUpperCase());
        return node == null ? null : node.value;
    }

    public void getInOrderStocks(List<Stock> list) {
        inOrder(root, list);
    }

    private int height(AVLNode node) {
        return node == null ? 0 : node.height;
    }

    private int getBalance(AVLNode node) {
        return node == null ? 0 : height(node.left) - height(node.right);
    }

    private AVLNode rightRotate(AVLNode y) {
        AVLNode x = y.left;
        AVLNode T2 = x.right;

        // Perform rotation
        x.right = y;
        y.left = T2;

        // Update heights
        y.height = Math.max(height(y.left), height(y.right)) + 1;
        x.height = Math.max(height(x.left), height(x.right)) + 1;

        // Return new root
        return x;
    }

    private AVLNode leftRotate(AVLNode x) {
        AVLNode y = x.right;
        AVLNode T2 = y.left;

        // Perform rotation
        y.left = x;
        x.right = T2;

        // Update heights
        x.height = Math.max(height(x.left), height(x.right)) + 1;
        y.height = Math.max(height(y.left), height(y.right)) + 1;

        // Return new root
        return y;
    }

    private AVLNode insert(AVLNode node, String key, Stock value) {
        if (node == null) {
            return new AVLNode(key, value);
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insert(node.left, key, value);
        } else if (cmp > 0) {
            node.right = insert(node.right, key, value);
        } else {
            node.value = value; // Update the stock value if key already exists
            return node;
        }

        // Update height of this ancestor node
        node.height = 1 + Math.max(height(node.left), height(node.right));

        // Get balance factor of this ancestor node
        int balance = getBalance(node);

        // If this node becomes unbalanced, then there are 4 cases:

        // 1. Left Left Case
        if (balance > 1 && key.compareTo(node.left.key) < 0) {
            return rightRotate(node);
        }

        // 2. Right Right Case
        if (balance < -1 && key.compareTo(node.right.key) > 0) {
            return leftRotate(node);
        }

        // 3. Left Right Case
        if (balance > 1 && key.compareTo(node.left.key) > 0) {
            node.left = leftRotate(node.left);
            return rightRotate(node);
        }

        // 4. Right Left Case
        if (balance < -1 && key.compareTo(node.right.key) < 0) {
            node.right = rightRotate(node.right);
            return leftRotate(node);
        }

        return node;
    }

    private AVLNode search(AVLNode node, String key) {
        if (node == null) {
            return null;
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            return search(node.left, key);
        } else if (cmp > 0) {
            return search(node.right, key);
        } else {
            return node;
        }
    }

    private void inOrder(AVLNode node, List<Stock> list) {
        if (node != null) {
            inOrder(node.left, list);
            list.add(node.value);
            inOrder(node.right, list);
        }
    }
}