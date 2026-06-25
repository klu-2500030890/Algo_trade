package algorithms;

import models.Stock;
import java.util.Comparator;
import java.util.List;

public class HeapSort {

    public static void sort(List<Stock> list, Comparator<Stock> comparator) {
        int n = list.size();

        // Build heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(list, n, i, comparator);
        }

        // One by one extract elements from heap
        for (int i = n - 1; i > 0; i--) {
            // Move current root to end
            Stock temp = list.get(0);
            list.set(0, list.get(i));
            list.set(i, temp);

            // call max heapify on the reduced heap
            heapify(list, i, 0, comparator);
        }
    }

    private static void heapify(List<Stock> list, int n, int i, Comparator<Stock> comparator) {
        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (l < n && comparator.compare(list.get(l), list.get(largest)) > 0) {
            largest = l;
        }

        // If right child is larger than largest so far
        if (r < n && comparator.compare(list.get(r), list.get(largest)) > 0) {
            largest = r;
        }

        // If largest is not root
        if (largest != i) {
            Stock swap = list.get(i);
            list.set(i, list.get(largest));
            list.set(largest, swap);

            // Recursively heapify the affected sub-tree
            heapify(list, n, largest, comparator);
        }
    }
}
