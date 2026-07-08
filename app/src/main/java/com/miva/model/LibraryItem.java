// LibraryItem.java
package com.miva.model;

public abstract class LibraryItem implements Borrowable {
    protected String id;
    protected String title;
    protected String author;
    protected int year;
    protected boolean isAvailable;

    public LibraryItem(String id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.isAvailable = true;
    }
    
    public String getId() { return id; }
    public boolean isAvailable() { return isAvailable; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public int getYear() { return year; }
    
    public abstract String getItemType();

    public void setAvailable(boolean available) {
    this.isAvailable = available;
}


// Return as string representation
@Override
public String toString() {
    return this.title + " (" + this.author + ")";
}


}