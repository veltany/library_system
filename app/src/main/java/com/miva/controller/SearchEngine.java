// SearchEngine.java
package com.miva.controller;
import com.miva.model.LibraryItem;
import java.util.List;

public class SearchEngine {
    
    // Linear Search Implementation
    public static LibraryItem linearSearch(List<LibraryItem> items, String targetTitle) {
        for (LibraryItem item : items) {
            if (item.getTitle().equalsIgnoreCase(targetTitle)) {
                return item;
            }
        }
        return null;
    }

    // Recursive Binary Search Implementation
    public static LibraryItem recursiveSearch(List<LibraryItem> items, String targetTitle, int left, int right) {
        if (left > right) return null;
        
        int mid = left + (right - left) / 2;
        int comparison = items.get(mid).getTitle().compareToIgnoreCase(targetTitle);
        
        if (comparison == 0) return items.get(mid);
        if (comparison > 0) return recursiveSearch(items, targetTitle, left, mid - 1);
        return recursiveSearch(items, targetTitle, mid + 1, right);
    }
    
    // Merge Sort Implementation (Recommended)
    public static void mergeSortByYear(List<LibraryItem> items) {
        if (items.size() <= 1) return;
        
        int mid = items.size() / 2;
        List<LibraryItem> left = new java.util.ArrayList<>(items.subList(0, mid));
        List<LibraryItem> right = new java.util.ArrayList<>(items.subList(mid, items.size()));
        
        mergeSortByYear(left);
        mergeSortByYear(right);
        merge(items, left, right);
    }

    private static void merge(List<LibraryItem> result, List<LibraryItem> left, List<LibraryItem> right) {
        int i = 0, j = 0, k = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i).getYear() <= right.get(j).getYear()) {
                result.set(k++, left.get(i++));
            } else {
                result.set(k++, right.get(j++));
            }
        }
        while (i < left.size()) result.set(k++, left.get(i++));
        while (j < right.size()) result.set(k++, right.get(j++));
    }
}