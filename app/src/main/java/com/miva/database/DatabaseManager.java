package com.miva.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.miva.model.LibraryItem;
import com.miva.model.UserAccount;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DatabaseManager {
    private static final String DATA_DIR = "data";
    private static final String ITEMS_FILE = DATA_DIR + "/items.json";
    private static final String USERS_FILE = DATA_DIR + "/users.json";
    private final Gson gson;

    private Map<String, LibraryItem> itemsTable = new LinkedHashMap<>();
    private Map<String, UserAccount> usersTable = new LinkedHashMap<>();

    public DatabaseManager() {
        // Register our polymorphic model mapping parser
        this.gson = new GsonBuilder()
                .registerTypeAdapter(LibraryItem.class, new LibraryItemAdapter())
                .setPrettyPrinting()
                .create();
        initStorageFiles();
        loadAllData();
    }

    private void initStorageFiles() {
        try {
            Files.createDirectories(Paths.get(DATA_DIR));
            File items = new File(ITEMS_FILE);
            File users = new File(USERS_FILE);
            if (!items.exists()) Files.writeString(items.toPath(), "[]");
            if (!users.exists()) Files.writeString(users.toPath(), "[]");
        } catch (IOException e) {
            System.err.println("Failed to establish local JSON database paths: " + e.getMessage());
        }
    }

    private void loadAllData() {
        try {
            // 1. Read Items Catalogue
            String itemsJson = Files.readString(Paths.get(ITEMS_FILE));
            List<LibraryItem> itemsList = gson.fromJson(itemsJson, new TypeToken<List<LibraryItem>>(){}.getType());
            if (itemsList != null) {
                itemsList.forEach(item -> itemsTable.put(item.getId(), item));
            }

            // 2. Read Users List
            String usersJson = Files.readString(Paths.get(USERS_FILE));
            List<UserAccount> usersList = gson.fromJson(usersJson, new TypeToken<List<UserAccount>>(){}.getType());
            if (usersList != null) {
                usersList.forEach(user -> usersTable.put(user.getUserId(), user));
            }
        } catch (IOException e) {
            System.err.println("Error synchronizing JSON caches: " + e.getMessage());
        }
    }

    public synchronized void saveAllData() {
        try {
            Files.writeString(Paths.get(ITEMS_FILE), gson.toJson(itemsTable.values()));
            Files.writeString(Paths.get(USERS_FILE), gson.toJson(usersTable.values()));
        } catch (IOException e) {
            System.err.println("Failed compiling changes to disk: " + e.getMessage());
        }
    }

    // Exposed Table Engine accessors
    public Map<String, LibraryItem> getItemsTable() { return itemsTable; }
    public Map<String, UserAccount> getUsersTable() { return usersTable; }
}
