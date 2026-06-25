package algorithms;

import models.Stock;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class MergeSort {

    public static void sort(List<Stock> list, Comparator<Stock> comparator) {
        mergeSort(list, 0, list.size() - 1, comparator);
    }

    private static void mergeSort(List<Stock> list, int left, int right, Comparator<Stock> comparator) {
        if (left < right) {
            int mid = (left + right) / 2;

            mergeSort(list, left, mid, comparator);
            mergeSort(list, mid + 1, right, comparator);

            merge(list, left, mid, right, comparator);
        }
    }

    private static void merge(List<Stock> list, int left, int mid, int right, Comparator<Stock> comparator) {
        int n1 = mid - left + 1;
        int n2 = right - mid;

        List<Stock> L = new ArrayList<>(n1);
        List<Stock> R = new ArrayList<>(n2);

        for (int i = 0; i < n1; i++) {
            L.add(list.get(left + i));
        }
        for (int j = 0; j < n2; j++) {
            R.add(list.get(mid + 1 + j));
        }

        int i = 0, j = 0;
        int k = left;

        while (i < n1 && j < n2) {
            if (comparator.compare(L.get(i), R.get(j)) <= 0) {
                list.set(k, L.get(i));
                i++;
            } else {
                list.set(k, R.get(j));
                j++;
            }
            k++;
        }

        while (i < n1) {
            list.set(k, L.get(i));
            i++;
            k++;
        }

        while (j < n2) {
            list.set(k, R.get(j));
            j++;
            k++;
        }
    }
}