// Borrowable.java
package com.miva.model;

public interface Borrowable {
    boolean borrowItem(String userId);
    boolean returnItem();
      boolean isAvailable();
}