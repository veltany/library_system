package com.miva.packages.sort;

import java.util.Comparator;

/**
 *
 * UniversalSort
 * Single helper to use all sort algorithms
 */

public class Sorter {

    private final MergeSort mergeSort;
    private final InsertionSort insertionSort;
    private final SelectionSort selectionSort;
    private final QuickSort quickSort;

    public Sorter() {
        this.mergeSort = new MergeSort();
        this.insertionSort = new InsertionSort();
        this.selectionSort = new SelectionSort();
        this.quickSort = new QuickSort();
    }

    public <T> void merge(T[] arr, Comparator<T> comp, boolean isAscending) {
        mergeSort.sort(arr, comp, isAscending);
    }

    public <T> void insertion(T[] arr, Comparator<T> comp, boolean isAscending) {
        insertionSort.sort(arr, comp, isAscending);
    }

    public <T> void selection(T[] arr, Comparator<T> comp, boolean isAscending) {
        selectionSort.sort(arr, comp, isAscending);
    }

    public <T> void quick(T[] arr, Comparator<T> comp, boolean isAscending) {
        quickSort.sort(arr, comp, isAscending);
    }

}
