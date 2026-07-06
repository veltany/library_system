// Magazine.java
package com.miva.model;

public class Magazine extends LibraryItem {
    private int issueNumber;



    // No aegument constructor for Magazine ( used by gson deserialization)
    public Magazine() {
    super("", "", "", 0);
    this.issueNumber = 0;
}


    public Magazine(String id, String title, String author, int year, int issueNumber) {
        super(id, title, author, year);
        this.issueNumber = issueNumber;
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
        return "Magazine";
    }

    @Override
public boolean isAvailable() {
    return this.isAvailable; 
}

public int getIssueNumber() { return this.issueNumber; }


}