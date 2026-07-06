package com.miva.database;

import com.google.gson.*;
import com.miva.model.*;
import java.lang.reflect.Type;

public class LibraryItemAdapter implements JsonSerializer<LibraryItem>, JsonDeserializer<LibraryItem> {
    @Override
    public JsonElement serialize(LibraryItem src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = context.serialize(src).getAsJsonObject();
        jsonObject.addProperty("type", src.getItemType());
        return jsonObject;
    }

    @Override
    public LibraryItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonElement typeElem = jsonObject.get("type");
        
        // SAFE FALLBACK: If the 'type' attribute is missing, don't crash. 
        // Log a warning and safely default it to a plain Book container.
        if (typeElem == null) {
            System.err.println("⚠️ Warning: Encountered legacy data missing a 'type' property. Defaulting item to 'Book'.");
            return context.deserialize(jsonObject, Book.class);
        }
        
        String type = typeElem.getAsString();
        switch (type) {
            case "Book":     return context.deserialize(jsonObject, Book.class);
            case "Magazine": return context.deserialize(jsonObject, Magazine.class);
            case "Journal":  return context.deserialize(jsonObject, Journal.class);
            default:         throw new JsonParseException("Unknown item runtime type: " + type);
        }
    }
}
