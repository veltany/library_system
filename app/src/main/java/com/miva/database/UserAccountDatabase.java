package com.miva.database;

import com.miva.model.UserAccount;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserAccountDatabase {
    private final DatabaseManager db;

    public UserAccountDatabase(DatabaseManager db) {
        this.db = db;
    }

    // CREATE
    public void createUser(UserAccount user) {
        db.getUsersTable().put(user.getUserId(), user);
        db.saveAllData();
    }

    // READ
    public Optional<UserAccount> getUserById(String id) {
        return Optional.ofNullable(db.getUsersTable().get(id));
    }

    public List<UserAccount> getAllUsers() {
        return new ArrayList<>(db.getUsersTable().values());
    }

    // UPDATE
    public boolean updateUser(UserAccount updatedUser) {
        if (db.getUsersTable().containsKey(updatedUser.getUserId())) {
            db.getUsersTable().put(updatedUser.getUserId(), updatedUser);
            db.saveAllData();
            return true;
        }
        return false;
    }

    // DELETE
    public boolean deleteUser(String id) {
        if (db.getUsersTable().containsKey(id)) {
            db.getUsersTable().remove(id);
            db.saveAllData();
            return true;
        }
        return false;
    }

    // HELPER: Append checked out resources straight to history vectors
    public boolean logBorrowAction(String userId, String itemId) {
        UserAccount user = db.getUsersTable().get(userId);
        if (user != null) {
            user.addToHistory(itemId);
            db.saveAllData();
            return true;
        }
        return false;
    }
}
