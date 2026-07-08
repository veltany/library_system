// UserAccount.java


package com.miva.model;
import java.util.ArrayList;
import java.util.List;

public class UserAccount {
    private String userId;
    private String name;
    private List<String> borrowingHistory;

    public UserAccount() {
    this.userId = "";
    this.name = "";
    this.borrowingHistory = new java.util.ArrayList<>();
}


    public UserAccount(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.borrowingHistory = new ArrayList<>();
    }

    public void addToHistory(String itemId) {
        borrowingHistory.add(itemId);
    }

// GETTERS
 public String getUserId() { return userId; }
public String getName() { return name; }
public List<String> getBorrowingHistory() { return borrowingHistory; }



// Return as string representation
@Override
public String toString() {
    return this.name + " (" + this.userId + ")";
}


}