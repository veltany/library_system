package com.miva.packages.sort;

import java.util.Comparator;

/**
 * Merge Sort Implementation
 */

/**
 * Merge Sort Algorithm Implementation
 * Merge sort is a divide-and-conquer algorithm. It works by dividing the input
 * list into two halves, sorting each half recursively, and then merging
 * recursively.
 * This can be done in place, requiring small additional amounts of memory to
 * perform the sorting.
 *
 * Time Complexity: O(n log n)
 * Space Complexity: O(n)
 * Stable: Yes
 */

public class MergeSort implements Sort {

    /*
     * Merge Sort
     */

    public <T> void sort(T[] arr, Comparator<T> comp, boolean isAscending) {
        mergeSortRecursive(arr, 0, arr.length - 1, comp, isAscending);
    }

    /**
     * Merge algorithm helper function
     *
     */
    private static <T> void mergeSortRecursive(T[] arr, int left, int right, Comparator<T> comp, boolean isAscending) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortRecursive(arr, left, mid, comp, isAscending);
            mergeSortRecursive(arr, mid + 1, right, comp, isAscending);
            merge(arr, left, mid, right, comp, isAscending);
        }
    }

    /**
     * Merge algorithm helper function
     *
     * @param <T>
     * @param arr
     * @param left
     * @param mid
     * @param right
     * @param comp
     * @param isAscending
     */

    @SuppressWarnings("unchecked")
    private static <T> void merge(T[] arr, int left, int mid, int right, Comparator<T> comp, boolean isAscending) {
        int n1 = mid - left + 1;
        int n2 = right - mid;
        int factor = isAscending ? 1 : -1;

        T[] leftArray = (T[]) new Object[n1];
        T[] rightArray = (T[]) new Object[n2];

        System.arraycopy(arr, left, leftArray, 0, n1);
        System.arraycopy(arr, mid + 1, rightArray, 0, n2);

        int i = 0, j = 0, k = left;
        while (i < n1 && j < n2) {
            if ((comp.compare(leftArray[i], rightArray[j]) * factor) <= 0) {
                arr[k] = leftArray[i++];
            } else {
                arr[k] = rightArray[j++];
            }
            k++;
        }

        while (i < n1)
            arr[k++] = leftArray[i++];
        while (j < n2)
            arr[k++] = rightArray[j++];
    }

}
