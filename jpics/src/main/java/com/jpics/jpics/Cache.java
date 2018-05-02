package com.jpics.jpics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Cache {

    private Map<Object, Object> items = new HashMap<>();
    private List<Object> orderedKeys = new ArrayList<>();

    // Default capacity
    private int capacity = 10;

    Cache() {
    }

    void put(Object key, Object item) {
        items.put(key, item);
        orderedKeys.remove(key);
        orderedKeys.add(key);
        if (capacity > 0 && items.size() == capacity + 1) {
            Object oldItemKey = orderedKeys.remove(0);
            items.remove(oldItemKey);
        }
    }

    Object get(Object key) {
        return items.get(key);
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
