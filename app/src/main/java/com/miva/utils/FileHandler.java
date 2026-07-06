// FileHandler.java
package com.miva.utils;
import com.miva.model.LibraryItem;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Just  a mock

public class FileHandler {
    private static final String FILE_NAME = "library_data.ser";

    // Uses Java Serialization to save the state of the catalogue
    public static void saveCatalogue(List<LibraryItem> catalogue) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            oos.writeObject(catalogue);
            System.out.println("Data saved successfully.");
        } catch (IOException e) {
            System.err.println("Error saving data: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<LibraryItem> loadCatalogue() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_NAME))) {
            return (ArrayList<LibraryItem>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading data: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}