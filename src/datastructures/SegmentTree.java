package datastructures;

public class SegmentTree {
    private double[] tree;
    private int n;

    public SegmentTree(double[] arr) {
        if (arr != null && arr.length > 0) {
            this.n = arr.length;
            this.tree = new double[4 * n];
            build(arr, 0, 0, n - 1);
        }
    }

    private void build(double[] arr, int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
            return;
        }
        int mid = (start + end) / 2;
        build(arr, 2 * node + 1, start, mid);
        build(arr, 2 * node + 2, mid + 1, end);
        tree[node] = Math.max(tree[2 * node + 1], tree[2 * node + 2]);
    }

    /**
     * Queries the maximum stock price in the index range [l, r] (0-indexed).
     */
    public double queryMax(int l, int r) {
        if (n == 0 || l > r || l < 0 || r >= n) {
            return 0.0;
        }
        return queryMax(0, 0, n - 1, l, r);
    }

    private double queryMax(int node, int start, int end, int l, int r) {
        if (r < start || end < l) {
            return -1e9; // representing negative infinity
        }
        if (l <= start && end <= r) {
            return tree[node];
        }
        int mid = (start + end) / 2;
        double leftMax = queryMax(2 * node + 1, start, mid, l, r);
        double rightMax = queryMax(2 * node + 2, mid + 1, end, l, r);
        return Math.max(leftMax, rightMax);
    }
}
