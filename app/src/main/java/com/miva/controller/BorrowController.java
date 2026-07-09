// BorrowController.java
package com.miva.controller;

import com.miva.model.LibraryItem;
import com.miva.model.UserAccount;
import com.miva.utils.Response;

public class BorrowController {
    private LibraryManager manager;

    public BorrowController(LibraryManager manager) {
        this.manager = manager;

    }

    // Validation
    private boolean isValidItemId(String itemId) {
        return manager.getItemById(itemId) != null;
    }

    private boolean isValidUserId(String userId) {
        return manager.getUserManager().getUserById(userId) != null;
    }

    // Business Logic

    public Response<Void> processBorrow(String itemId, String userId) {

        if (itemId.isEmpty() || userId.isEmpty()) {
            return Response.error("Error: Missing item or user ID.");
        }

        if (!isValidItemId(itemId) || !isValidUserId(userId)) {
            return Response.error("Error: Invalid item or user ID.");
        }

        // fetch item
        LibraryItem item = manager.getItemById(itemId);
        UserAccount user = manager.getUserManager().getUserById(userId).orElse(null);

        if (item == null || user == null) {
            return Response.error("Error: Item or user not found.");
        }

        // check availability of item
        if (!item.isAvailable()) {
            return Response.error("Borrow Unavailable:\n\"" + item.getTitle()
                    + "\" is already checked out to user: " + user.getName());
        }

        if (item.borrowItem(userId)) {
            manager.addToCache(item); // Update frequent access cache
            manager.getUserDatabase().logBorrowAction(userId, itemId);
            // Update the central cache state registry and commit immediately
            manager.addItem(item);
            item.setAvailable(false);
            return Response.success("Success: " + item.getTitle() + " borrowed by " + userId);
        } else {
            manager.addToReservationQueue(userId + "-" + itemId);
            return Response.error("Item unavailable. Added to reservation waitlist.");
        }
    }

    public Response<Void> processReturn(String itemId) {
        LibraryItem item = manager.getItemById(itemId);
        if (item != null && item.returnItem()) {
            return Response.success("Item returned successfully.");
        }
        return Response.error("Item not found or already available.");
    }
}
