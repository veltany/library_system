// Journal.java
package com.miva.model;

public class Journal extends LibraryItem {
    private String volume;


    public Journal() {
    super("", "", "", 0);
    this.volume = "";
}


    public Journal(String id, String title, String author, int year, String volume) {
        super(id, title, author, year);
        this.volume = volume;
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
        return "Journal";
    }

    @Override
public boolean isAvailable() {
    return this.isAvailable; 
}


public String getVolume() { return this.volume; }

}