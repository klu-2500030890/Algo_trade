package datastructures;

public class FenwickTree {
    private int[] tree;
    private int n;

    public FenwickTree(int size) {
        this.n = size;
        this.tree = new int[n + 1];
    }

    public FenwickTree(int[] arr) {
        this(arr.length);
        for (int i = 0; i < arr.length; i++) {
            update(i, arr[i]);
        }
    }

    /**
     * Updates value at 0-indexed position by delta.
     */
    public void update(int index, int delta) {
        int i = index + 1; // Fenwick Tree is 1-indexed internally
        while (i <= n) {
            tree[i] += delta;
            i += i & (-i);
        }
    }

    /**
     * Queries prefix sum from index 0 to index (0-indexed).
     */
    public int query(int index) {
        int sum = 0;
        int i = index + 1;
        while (i > 0) {
            sum += tree[i];
            i -= i & (-i);
        }
        return sum;
    }

    /**
     * Queries range sum from l to r (inclusive, 0-indexed).
     */
    public int queryRange(int l, int r) {
        if (l > r || l < 0 || r >= n) {
            return 0;
        }
        return query(r) - query(l - 1);
    }
}
