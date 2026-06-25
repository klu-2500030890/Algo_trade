package algorithms;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BellmanFord {

    public static class Edge {
        public int u, v;
        public double weight; // weight is -ln(rate)
        public double rate;   // original exchange rate

        public Edge(int u, int v, double rate) {
            this.u = u;
            this.v = v;
            this.rate = rate;
            this.weight = -Math.log(rate);
        }
    }

    /**
     * Runs Bellman-Ford to find negative cycles (arbitrage opportunities).
     * Returns a list of vertex IDs representing the cycle path, or null if none.
     */
    public static List<Integer> findArbitrageLoop(int numVertices, List<Edge> edges) {
        double[] dist = new double[numVertices];
        int[] parent = new int[numVertices];

        for (int i = 0; i < numVertices; i++) {
            dist[i] = 1e9; // represents infinity
            parent[i] = -1;
        }
        dist[0] = 0.0;

        // Relax edges V-1 times
        for (int i = 0; i < numVertices - 1; i++) {
            for (Edge edge : edges) {
                if (dist[edge.u] + edge.weight < dist[edge.v]) {
                    dist[edge.v] = dist[edge.u] + edge.weight;
                    parent[edge.v] = edge.u;
                }
            }
        }

        // Check for negative weight cycles
        for (Edge edge : edges) {
            if (dist[edge.u] + edge.weight < dist[edge.v]) {
                // Negative cycle detected. Trace it back.
                int cycleStart = edge.v;
                for (int i = 0; i < numVertices; i++) {
                    cycleStart = parent[cycleStart];
                }

                List<Integer> cycle = new ArrayList<>();
                int curr = cycleStart;
                while (true) {
                    cycle.add(curr);
                    if (curr == cycleStart && cycle.size() > 1) {
                        break;
                    }
                    curr = parent[curr];
                }
                Collections.reverse(cycle);
                return cycle;
            }
        }
        return null;
    }
}
