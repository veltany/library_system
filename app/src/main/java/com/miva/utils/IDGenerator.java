// IDGenerator.java
package com.miva.utils;
import java.util.UUID;

public class IDGenerator {
    public static String generateUniqueID() {
        // Generates a short, unique 8-character alphanumeric ID
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}