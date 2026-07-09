package com.miva.utils;

import java.util.Arrays;

/**
 * Implementation for quick fixed-size cache for
 * “Most Frequently Accessed Items”
 *
 * A fixed-size cache that retains items based on their access
 * frequency.
 * Evicts the least frequently accessed item when full.
 *
 * @param <T> The type of objects cached.
 */
public class FrequentAccessCache<T> {

    // Simple inner wrapper to bind an item to its hit score
    private static class CacheEntry<E> {
        E item;
        int hitCount;

        CacheEntry(E item) {
            this.item = item;
            this.hitCount = 1; // Initialized with 1 hit on start
        }
    }

    private final CacheEntry<T>[] cacheArray;
    private final int capacity;
    private int size;

    @SuppressWarnings("unchecked")
    public FrequentAccessCache(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Cache capacity must be greater than 0");
        }
        this.capacity = capacity;
        this.size = 0;
        this.cacheArray = new CacheEntry[capacity];
    }

    /**
     * Retrieves an item from the cache. If found, its access score increments.
     *
     * @param target The item to look up.
     * @return The cached item instance, or null if it does not exist in the cache.
     */
    public T get(T target) {
        if (target == null)
            return null;

        for (int i = 0; i < size; i++) {
            if (cacheArray[i].item.equals(target)) {
                cacheArray[i].hitCount++;
                return cacheArray[i].item;
            }
        }
        return null; // Cache Miss
    }

    /**
     * Adds an item to the cache. Increments frequency if it already exists.
     * Evicts the least frequently accessed item if capacity is exceeded.
     */
    public void put(T item) {
        if (item == null)
            return;

        for (int i = 0; i < size; i++) {
            if (cacheArray[i].item.equals(item)) {
                cacheArray[i].hitCount++;
                return;
            }
        }

        // Cache has free space remaining
        if (size < capacity) {
            cacheArray[size] = new CacheEntry<>(item);
            size++;
            return;
        }

        // Cache is Full. Evict the Least Frequently Accessed item
        int lowestIndex = 0;
        int minHits = cacheArray[0].hitCount;

        for (int i = 1; i < capacity; i++) {
            if (cacheArray[i].hitCount < minHits) {
                minHits = cacheArray[i].hitCount;
                lowestIndex = i;
            }
        }

        // Overwrite the slot of the evicted element with the fresh item
        cacheArray[lowestIndex] = new CacheEntry<>(item);
    }

    /**
     * Clears all frequency tracking tables and empties references safely.
     */
    public void clear() {
        Arrays.fill(cacheArray, null);
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    public int capacity() {
        return this.capacity;
    }
}
