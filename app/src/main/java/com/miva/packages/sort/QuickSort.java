package com.miva.packages.sort;

import java.util.Comparator;

/**
 * Universal Sort Implementation
 */

/*
 * Quick Sort Algorithm Implementation
 * Quick sort is a divide-and-conquer algorithm. It works by selecting a pivot
 * element from the array and partitioning the other elements into two
 * sub-arrays,
 * according to whether they are less than or greater than the pivot.
 * The sub-arrays are then sorted recursively. This can be done in place,
 * requiring small additional amounts of memory to perform the sorting.
 *
 * Time Complexity: O(n log n)
 * Space Complexity: O(log n)
 * Stable: No
 */

public class QuickSort implements Sort {

    public <T> void sort(T[] arr, Comparator<T> comp, boolean isAscending) {
        quickSortRecursive(arr, 0, arr.length - 1, comp, isAscending);
    }

    private <T> void quickSortRecursive(T[] arr, int low, int high, Comparator<T> comp, boolean isAscending) {
        if (low < high) {
            int pi = partition(arr, low, high, comp, isAscending);
            quickSortRecursive(arr, low, pi - 1, comp, isAscending);
            quickSortRecursive(arr, pi + 1, high, comp, isAscending);
        }
    }

    private static <T> int partition(T[] arr, int low, int high, Comparator<T> comp, boolean isAscending) {
        T pivot = arr[high];
        int i = (low - 1);
        int factor = isAscending ? 1 : -1;
        for (int j = low; j < high; j++) {
            if ((comp.compare(arr[j], pivot) * factor) <= 0) {
                i++;
                swap(arr, i, j);
            }
        }
        swap(arr, i + 1, high);
        return i + 1;
    }

    private static <T> void swap(T[] arr, int i, int j) {
        T temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}
