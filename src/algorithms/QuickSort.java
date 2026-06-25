package algorithms;

import models.Stock;
import java.util.Comparator;
import java.util.List;

public class QuickSort {

    public static void sort(List<Stock> list, Comparator<Stock> comparator) {
        quickSort(list, 0, list.size() - 1, comparator);
    }

    private static void quickSort(List<Stock> list, int low, int high, Comparator<Stock> comparator) {
        if (low < high) {
            int pi = partition(list, low, high, comparator);
            quickSort(list, low, pi - 1, comparator);
            quickSort(list, pi + 1, high, comparator);
        }
    }

    private static int partition(List<Stock> list, int low, int high, Comparator<Stock> comparator) {
        Stock pivot = list.get(high);
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element is smaller than or equal to pivot
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;

                // swap list[i] and list[j]
                Stock temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        // swap list[i+1] and list[high] (or pivot)
        Stock temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }
}
