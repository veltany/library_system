package com.miva.controller;

import java.util.Comparator;

import com.miva.model.LibraryItem;
import com.miva.packages.sort.InsertionSort;
import com.miva.packages.sort.MergeSort;
import com.miva.packages.sort.QuickSort;
import com.miva.packages.sort.SelectionSort;
import com.miva.packages.sort.Sort;

public class SortEngine {

    // Friendly enums that know exactly WHICH sorting strategy to execute
    public enum AlgoType {
        SELECTION(new SelectionSort()),
        INSERTION(new InsertionSort()),
        MERGE(new MergeSort()),
        QUICK(new QuickSort());

        private final Sort strategy;

        AlgoType(Sort strategy) {
            this.strategy = strategy;
        }

        public Sort getStrategy() {
            return strategy;
        }
    }

    // ==========================================
    // EXPOSED PUBLIC API METHODS
    // ==========================================

    public static void sortByAuthor(LibraryItem[] items, AlgoType algo, boolean isAscending) {
        Comparator<LibraryItem> byAuthor = (a, b) -> a.getAuthor().compareToIgnoreCase(b.getAuthor());
        executeSort(items, byAuthor, algo, isAscending);
    }

    public static void sortByTitle(LibraryItem[] items, AlgoType algo, boolean isAscending) {
        Comparator<LibraryItem> byTitle = (a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle());
        executeSort(items, byTitle, algo, isAscending);
    }

    public static void sortByYear(LibraryItem[] items, AlgoType algo, boolean isAscending) {
        Comparator<LibraryItem> byYear = (a, b) -> Integer.compare(a.getYear(), b.getYear());
        executeSort(items, byYear, algo, isAscending);
    }

    public static void sortByAuthor(LibraryItem[] items, AlgoType algo) {
        sortByAuthor(items, algo, true);
    }

    // ==========================================
    // ENGINE CENTRAL ROUTER
    // ==========================================
    private static void executeSort(LibraryItem[] items, Comparator<LibraryItem> comp, AlgoType algo,
            boolean isAscending) {
        if (items == null || items.length <= 1)
            return;

        // Fallback safety check
        AlgoType selectedAlgo = (algo != null) ? algo : AlgoType.MERGE;

        // One-line routing using Polymorphism
        selectedAlgo.getStrategy().sort(items, comp, isAscending);
    }
}
