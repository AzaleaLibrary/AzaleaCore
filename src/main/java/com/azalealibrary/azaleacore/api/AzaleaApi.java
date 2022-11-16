package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class AzaleaApi<T> {

    private final Map<String, T> objects = new HashMap<>();

    public ImmutableList<String> getKeys() {
        return ImmutableList.copyOf(objects.keySet());
    }

    public ImmutableList<T> getObjects() {
        return ImmutableList.copyOf(objects.values());
    }

    public ImmutableMap<String, T> getEntries() {
        return ImmutableMap.copyOf(objects);
    }

    public T get(String key) {
        return objects.get(key);
    }

    public void add(String key, T object) {
        if (objects.containsKey(key)) {
            throw new AzaleaException("Object with key '" + key + "' already exists.");
        }
        objects.put(key, object);
    }

    public void remove(String key) {
        if (!objects.containsKey(key)) {
            throw new AzaleaException("Object with key '" + key + "' does not exists.");
        }
        objects.remove(key);
    }

    public void remove(T object) {
        while (objects.values().remove(object));
    }
}
