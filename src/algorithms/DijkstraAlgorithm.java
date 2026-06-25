package algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class DijkstraAlgorithm {

    public static class Node implements Comparable<Node> {
        public int id;
        public double distance;

        public Node(int id, double distance) {
            this.id = id;
            this.distance = distance;
        }

        @Override
        public int compareTo(Node other) {
            return Double.compare(this.distance, other.distance);
        }
    }

    /**
     * Finds the shortest path in an adjacency matrix from source to target.
     * Returns a list of vertex IDs representing the path, or an empty list if no path exists.
     */
    public static List<Integer> findShortestPath(double[][] adjMatrix, int source, int target) {
        int n = adjMatrix.length;
        double[] dist = new double[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Double.MAX_VALUE);
        Arrays.fill(parent, -1);

        dist[source] = 0;
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(source, 0));

        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            int u = curr.id;

            if (u == target) {
                break;
            }
            if (curr.distance > dist[u]) {
                continue;
            }

            for (int v = 0; v < n; v++) {
                if (adjMatrix[u][v] > 0) { // 0 means no connection
                    double weight = adjMatrix[u][v];
                    if (dist[u] + weight < dist[v]) {
                        dist[v] = dist[u] + weight;
                        parent[v] = u;
                        pq.add(new Node(v, dist[v]));
                    }
                }
            }
        }

        if (dist[target] == Double.MAX_VALUE) {
            return new ArrayList<>(); // No path found
        }

        List<Integer> path = new ArrayList<>();
        int curr = target;
        while (curr != -1) {
            path.add(curr);
            curr = parent[curr];
        }
        Collections.reverse(path);
        return path;
    }
}
