// Book.java
package com.miva.model;

public class Book extends LibraryItem {
    private String isbn;

    public Book() {
        super("", "", "", 0);
        this.isbn = "";
    }

    public Book(String id, String title, String author, int year, String isbn) {
        super(id, title, author, year);
        this.isbn = isbn;
    }

    @Override
    public boolean borrowItem(String userId) {
        if (isAvailable) {
            isAvailable = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean returnItem() {
        isAvailable = true;
        return true;
    }

    @Override
    public String getItemType() {
        return "Book";
    }

    @Override
public boolean isAvailable() {
    return this.isAvailable; // Ensure it matches the 'boolean' type from your interface
}

}