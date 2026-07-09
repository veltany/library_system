package com.miva.utils;

import java.util.ArrayList;
import java.util.List;

import com.miva.model.Refreshable;

// Implements a refreshable object

public class RefreshObject {

    protected ArrayList<Refreshable> refreshables;

    public RefreshObject(List<Refreshable> initialRefreshables) {
        this.refreshables = new java.util.ArrayList<>(initialRefreshables);
    }

    public RefreshObject() {
        this.refreshables = new java.util.ArrayList<>();
    }

    // public hook to refresh all refreshables
    public void refresh() {
        for (Refreshable refreshable : refreshables) {
            if (refreshable != null) {
                refreshable.refreshData();
            }
        }
    }

    // dynamically add refreshable object
    public void add(Refreshable refreshable) {
        if (refreshable != null && !refreshables.contains(refreshable)) {
            refreshables.add(refreshable);
        }
    }

    // dynamically remove refreshable object
    public void remove(Refreshable refreshable) {
        refreshables.remove(refreshable);
    }

}
