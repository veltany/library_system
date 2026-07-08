// LibraryManager.java
package com.miva.controller;
import com.miva.database.DatabaseManager;
import com.miva.database.UserAccountDatabase;
import com.miva.model.LibraryItem;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

public class LibraryManager {
    private ArrayList<LibraryItem> catalogue;
    private Queue<String> reservationQueue;
    private Stack<LibraryItem> undoStack;
    private LibraryItem[] frequentAccessCache;
    private int cacheIndex = 0;
    private DatabaseManager db;
    private UserAccountDatabase userDb;

    private final UserManager userManager;




    public LibraryManager() {
       
        this.reservationQueue = new LinkedList<>();
        this.undoStack = new Stack<>();
        this.frequentAccessCache = new LibraryItem[5];

        // initialise database
        this.db = new DatabaseManager();
        this.userDb = new UserAccountDatabase(db);
        this.userManager = new UserManager(db);

        // load catalogue from database
        this.catalogue = new ArrayList<>(db.getItemsTable().values());
    }

    public void addItem(LibraryItem item) {
        catalogue.add(item);
         db.getItemsTable().put(item.getId(), item);
        db.saveAllData(); 
       
    }

    public void saveToDatabase() {
        db.saveAllData();
    }


    public void deleteItem(LibraryItem item) {
        if (catalogue.remove(item)) {
            undoStack.push(item);
              // LibraryItem target = db.getItemsTable().remove(item);
        
            db.saveAllData();
        
        }
    }



      public List<LibraryItem> getCatalogue() {
        if (db.getItemsTable() == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(db.getItemsTable().values());
    }

    public void undoLastDeletion() {
        if (!undoStack.isEmpty()) {
            catalogue.add(undoStack.pop());
        }
    }

    public void addToCache(LibraryItem item) {
        frequentAccessCache[cacheIndex % 5] = item;
        cacheIndex++;
    }
    
    // Recursive Algorithm to compute total items
    public int countTotalItemsRecursively(int index) {
        if (index >= catalogue.size()) return 0;
        return 1 + countTotalItemsRecursively(index + 1);
    }


    

public LibraryItem getItemById(String id) {
    for (LibraryItem item : catalogue) {
       // if (item.getId().equals(id)) {
       //     return item;
      //  }
    }
    return null; // Return null if item isn't found
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