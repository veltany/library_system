// BorrowController.java
package com.miva.controller;
import com.miva.model.LibraryItem;

public class BorrowController {
    private LibraryManager manager;

    public BorrowController(LibraryManager manager) {
        this.manager = manager;
    }

    public String processBorrow(String itemId, String userId) {
        // In a real system, you'd fetch the item from the manager's catalogue
        LibraryItem item = manager.getItemById(itemId); 
        
        if (item == null) {
            return "Error: Item not found.";
        }
        
        if (item.borrowItem(userId)) {
            manager.addToCache(item); // Update frequent access cache
            return "Success: " + item.getTitle() + " borrowed by " + userId;
        } else {
            manager.addToReservationQueue(userId + "-" + itemId);
            return "Item unavailable. Added to reservation waitlist.";
        }
    }

    public String processReturn(String itemId) {
        LibraryItem item = manager.getItemById(itemId);
        if (item != null && item.returnItem()) {
            return "Success: Item returned.";
        }
        return "Error processing return.";
    }
}