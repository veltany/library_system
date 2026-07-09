package com.miva.utils;

/**
 * Universal Sort Implementation
 */

import java.util.Comparator;

public class UniversalSorter {

    // ==========================================
    // 1. SELECTION SORT
    // ==========================================
    public static <T> void selectionSort(T[] arr, Comparator<T> comp, boolean isAscending) {
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

    // ==========================================
    // 2. INSERTION SORT
    // ==========================================
    public static <T> void insertionSort(T[] arr, Comparator<T> comp, boolean isAscending) {
        int n = arr.length;
        int factor = isAscending ? 1 : -1;
        for (int i = 1; i < n; i++) {
            T key = arr[i];
            int j = i - 1;
            while (j >= 0 && (comp.compare(arr[j], key) * factor) > 0) {
                arr[j + 1] = arr[j];
                j = j - 1;
            }
            arr[j + 1] = key;
        }
    }

    // ==========================================
    // 3. MERGE SORT
    // ==========================================
    public static <T> void mergeSort(T[] arr, Comparator<T> comp, boolean isAscending) {
        mergeSortRecursive(arr, 0, arr.length - 1, comp, isAscending);
    }

    private static <T> void mergeSortRecursive(T[] arr, int left, int right, Comparator<T> comp, boolean isAscending) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSortRecursive(arr, left, mid, comp, isAscending);
            mergeSortRecursive(arr, mid + 1, right, comp, isAscending);
            merge(arr, left, mid, right, comp, isAscending);
        }
    }

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

    // ==========================================
    // 4. QUICK SORT
    // ==========================================
    public static <T> void quickSort(T[] arr, Comparator<T> comp, boolean isAscending) {
        quickSortRecursive(arr, 0, arr.length - 1, comp, isAscending);
    }

    private static <T> void quickSortRecursive(T[] arr, int low, int high, Comparator<T> comp, boolean isAscending) {
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
