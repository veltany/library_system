package com.miva.packages.sort;

import java.util.Comparator;

/*
 * Insertion Sort Algorithm Implementation


 * Insertion sort is a simple sorting algorithm that works similar to the way
 * you sort whot game cards in your hands.
 * The array is virtually split into a sorted and an unsorted part.
 * Values from the unsorted part are picked and placed at the correct position
 * in the sorted part.
 *
 * Time Complexity: O(n2)
 * Space Complexity: O(1)
 * Stable: Yes
 */

public class InsertionSort implements Sort {

    public <T> void sort(T[] arr, Comparator<T> comp, boolean isAscending) {
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

}
