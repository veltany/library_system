package com.miva.controller;

import com.miva.database.DatabaseManager;
import com.miva.database.UserAccountDatabase;
import com.miva.model.UserAccount;
import java.util.List;
import java.util.Optional;

public class UserManager {
    private final DatabaseManager db;
    private final UserAccountDatabase userDb;

    public UserManager(DatabaseManager db) {
        this.db = db;
        this.userDb = new UserAccountDatabase(db);
    }

    public void createUser(UserAccount user) {
        userDb.createUser(user);
    }

    public Optional<UserAccount> getUserById(String id) {
        return userDb.getUserById(id);
    }

    public List<UserAccount> getAllUsers() {
        return userDb.getAllUsers();
    }

    public boolean updateUser(UserAccount user) {
        return userDb.updateUser(user);
    }

    public boolean deleteUser(String id) {
        return userDb.deleteUser(id);
    }

    public UserAccountDatabase getRawDatabase() {
        return userDb;
    }
}
