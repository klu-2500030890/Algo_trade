package datastructures;

import java.util.ArrayList;
import java.util.List;

public class MarketGraph {
    private int numVertices;
    private double[][] adjacencyMatrix;
    private List<String> nodeNames;

    public MarketGraph(int numVertices) {
        this.numVertices = numVertices;
        this.adjacencyMatrix = new double[numVertices][numVertices];
        this.nodeNames = new ArrayList<>();
    }

    public void addNodeName(String name) {
        nodeNames.add(name);
    }

    public String getNodeName(int id) {
        if (id >= 0 && id < nodeNames.size()) {
            return nodeNames.get(id);
        }
        return "Asset " + id;
    }

    public void addEdge(int source, int target, double weight) {
        if (source >= 0 && source < numVertices && target >= 0 && target < numVertices) {
            adjacencyMatrix[source][target] = weight;
        }
    }

    public double getEdgeWeight(int source, int target) {
        if (source >= 0 && source < numVertices && target >= 0 && target < numVertices) {
            return adjacencyMatrix[source][target];
        }
        return 0.0;
    }

    public int getNumVertices() {
        return numVertices;
    }

    public double[][] getAdjacencyMatrix() {
        return adjacencyMatrix;
    }
}
