package algorithms;

public class FloydWarshall {

    /**
     * Computes all-pairs shortest paths using Floyd-Warshall.
     * Helpful for sector-wide correlation or transaction distance mapping.
     */
    public static double[][] computeAllPairsShortestPaths(double[][] adjMatrix) {
        int n = adjMatrix.length;
        double[][] dist = new double[n][n];

        // Initialize distance matrix
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    dist[i][j] = 0;
                } else if (adjMatrix[i][j] > 0) {
                    dist[i][j] = adjMatrix[i][j];
                } else {
                    dist[i][j] = 1e9; // representing infinity
                }
            }
        }

        // Run Floyd-Warshall dynamic programming
        for (int k = 0; k < n; k++) {
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    if (dist[i][k] + dist[k][j] < dist[i][j]) {
                        dist[i][j] = dist[i][k] + dist[k][j];
                    }
                }
            }
        }

        return dist;
    }
}
