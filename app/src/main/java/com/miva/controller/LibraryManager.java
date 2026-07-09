package com.miva.controller;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import com.miva.database.DatabaseManager;
import com.miva.database.UserAccountDatabase;
import com.miva.model.LibraryItem;
import com.miva.utils.FrequentAccessCache;

public class LibraryManager {
    private Queue<String> reservationQueue;
    private Stack<LibraryItem> undoStack;
    private final FrequentAccessCache<LibraryItem> frequentAccessCache;

    private int cacheIndex = 0;
    private DatabaseManager db;
    private UserAccountDatabase userDb;
    private final UserManager userManager;

    public LibraryManager() {
        this.reservationQueue = new LinkedList<>();
        this.undoStack = new Stack<>();
        this.frequentAccessCache = new FrequentAccessCache<>(5);

        // Initialise database layers
        this.db = new DatabaseManager();
        this.userDb = new UserAccountDatabase(db);
        this.userManager = new UserManager(db);
    }

    public void addItem(LibraryItem item) {
        if (item != null) {
            db.getItemsTable().put(item.getId(), item);
            db.saveAllData();
        }
    }

    public void saveToDatabase() {
        db.saveAllData();
    }

    public void deleteItem(LibraryItem item) {
        if (item != null && db.getItemsTable().containsKey(item.getId())) {
            undoStack.push(item);
            db.getItemsTable().remove(item.getId());
            db.saveAllData();
        }
    }

    public List<LibraryItem> getCatalogue() {
        if (db.getItemsTable() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(db.getItemsTable().values());
    }

    /**
     * Get available catalogue items
     */
    public List<LibraryItem> getAvailableItems() {
        List<LibraryItem> availableItems = new ArrayList<>();
        // FIX: Always query current data stream instead of reading a stale collection
        // field
        for (LibraryItem item : getCatalogue()) {
            if (item.isAvailable()) {
                availableItems.add(item);
            }
        }
        return availableItems;
    }

    /**
     * Get currently active borrowed items
     */
    public List<LibraryItem> getBorrowedItems() {
        List<LibraryItem> borrowedItems = new ArrayList<>();
        // Always query current data stream
        for (LibraryItem item : getCatalogue()) {
            if (!item.isAvailable()) {
                borrowedItems.add(item);
            }
        }
        return borrowedItems;
    }

    public void undoLastDeletion() {
        if (!undoStack.isEmpty()) {
            LibraryItem restoredItem = undoStack.pop();
            db.getItemsTable().put(restoredItem.getId(), restoredItem); // Add back to database
            db.saveAllData(); // FIX: Save tracking changes to file system immediately
        }
    }

    /**
     * IMPLEMENTATION of quick fixed-size cache for
     * “Most Frequently Accessed
     * Items"
     *
     */

    public void addToCache(LibraryItem item) {
        if (item != null) {
            frequentAccessCache.put(item);
        }
    }

    public LibraryItem checkCache(LibraryItem item) {
        return frequentAccessCache.get(item);
    }

    // Recursive Algorithm to compute total items
    public int countTotalItemsRecursively(int index) {
        List<LibraryItem> currentCatalog = getCatalogue();
        if (index >= currentCatalog.size())
            return 0;
        return 1 + countTotalItemsRecursively(index + 1);
    }

    /**
     * Return a single library catalogue item by ID
     */
    public LibraryItem getItemById(String id) {
        if (id == null || id.trim().isEmpty())
            throw new IllegalArgumentException("ID cannot be empty");

        return db.getItemsTable().get(id); // FIX: O(1) performance lookup instead of O(N) stream iteration loop
    }

    public void addToReservationQueue(String reservationDetails) {
        reservationQueue.add(reservationDetails);
    }

    public UserAccountDatabase getUserDatabase() {
        return userDb;
    }

    public UserManager getUserManager() {
        return userManager;
    }
}
