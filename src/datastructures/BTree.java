package datastructures;

import models.Transaction;

public class BTree {
    private static final int M = 3; // Order of B-Tree (Max children = 3, Max keys = 2)

    public static class BNode {
        public int numKeys;
        public int[] keys = new int[M]; // Store transactionId
        public Transaction[] values = new Transaction[M]; // Store Transaction object
        public BNode[] children = new BNode[M + 1];
        public boolean isLeaf = true;

        public BNode() {
            this.numKeys = 0;
        }
    }

    private BNode root;

    public BTree() {
        this.root = new BNode();
    }

    public Transaction search(int key) {
        return search(root, key);
    }

    private Transaction search(BNode node, int key) {
        int i = 0;
        while (i < node.numKeys && key > node.keys[i]) {
            i++;
        }
        if (i < node.numKeys && key == node.keys[i]) {
            return node.values[i];
        }
        if (node.isLeaf) {
            return null;
        }
        return search(node.children[i], key);
    }

    public void insert(int key, Transaction value) {
        BNode r = root;
        if (r.numKeys == M - 1) { // Node is full, need to split root
            BNode s = new BNode();
            root = s;
            s.isLeaf = false;
            s.children[0] = r;
            splitChild(s, 0, r);
            insertNonFull(s, key, value);
        } else {
            insertNonFull(r, key, value);
        }
    }

    private void insertNonFull(BNode node, int key, Transaction value) {
        int i = node.numKeys - 1;
        if (node.isLeaf) {
            while (i >= 0 && key < node.keys[i]) {
                node.keys[i + 1] = node.keys[i];
                node.values[i + 1] = node.values[i];
                i--;
            }
            node.keys[i + 1] = key;
            node.values[i + 1] = value;
            node.numKeys++;
        } else {
            while (i >= 0 && key < node.keys[i]) {
                i--;
            }
            i++;
            BNode child = node.children[i];
            if (child.numKeys == M - 1) {
                splitChild(node, i, child);
                if (key > node.keys[i]) {
                    i++;
                }
            }
            insertNonFull(node.children[i], key, value);
        }
    }

    private void splitChild(BNode parent, int i, BNode child) {
        BNode z = new BNode();
        z.isLeaf = child.isLeaf;

        // Since M=3 (Order 3), a full node has 2 keys.
        // Splitting a node of size 2 splits it into two nodes of size 1,
        // and pushes the middle key up to the parent.
        int t = 1;
        z.numKeys = t;

        // Copy the last key of child to z
        z.keys[0] = child.keys[1];
        z.values[0] = child.values[1];

        // Copy children if not a leaf
        if (!child.isLeaf) {
            z.children[0] = child.children[1];
            z.children[1] = child.children[2];
        }

        child.numKeys = t; // Child now has 1 key (the 0-th key)

        // Shift parent children
        for (int j = parent.numKeys; j >= i + 1; j--) {
            parent.children[j + 1] = parent.children[j];
        }
        parent.children[i + 1] = z;

        // Shift parent keys
        for (int j = parent.numKeys - 1; j >= i; j--) {
            parent.keys[j + 1] = parent.keys[j];
            parent.values[j + 1] = parent.values[j];
        }

        // Push the split key up to the parent
        parent.keys[i] = child.keys[1];
        parent.values[i] = child.values[1];
        parent.numKeys++;
    }
}
