package com.miva.packages.sort;

import java.util.Comparator;

/**
 *
 * Selection Sort Algorithm Implementation
 *
 *
 * Selection sort is an in-place comparison sorting algorithm. It has an
 * O(n2) time complexity, which makes it inefficient on large lists, and
 * generally performs worse than the similar insertion sort.
 *
 * Time Complexity: O(n2)
 * Space Complexity: O(1)
 * Stable: No
 */

public class SelectionSort implements Sort {

    public <T> void sort(T[] arr, Comparator<T> comp, boolean isAscending) {
        int n = arr.length;
        int factor = isAscending ? 1 : -1;
        for (int i = 0; i < n - 1; i++) {
            int targetIdx = i;
            for (int j = i + 1; j < n; j++) {
                // Multiplying by factor flips the logic automatically for descending
                if ((comp.compare(arr[j], arr[targetIdx]) * factor) < 0) {
                    targetIdx = j;
                }
            }
            swap(arr, i, targetIdx);
        }
    }

    private <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

}
