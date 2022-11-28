package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.broadcast.AzaleaBroadcaster;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class AzaleaApi<T> implements Serializable {

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

    public @Nullable String getKey(T object) {
        return objects.entrySet().stream()
                .filter(entry -> object.equals(entry.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    public boolean hasKey(String key) {
        return getKeys().contains(key);
    }

    public void add(String key, T object) {
        if (hasKey(key)) {
            throw new AzaleaException("Object with key '" + key + "' already exists.");
        }
        update(key, object);
    }

    protected void update(String key, T object) {
        objects.put(key, object);
    }

    public void remove(String key) {
        if (!hasKey(key)) {
            throw new AzaleaException("Object with key '" + key + "' does not exists.");
        }
        objects.remove(key);
    }

    public void remove(T object) {
        while (objects.values().remove(object));
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        getEntries().forEach((key, entry) -> {
            try {
                ConfigurationSection section = configuration.createSection(key);
                serializeEntry(section, entry);
                configuration.set(key, section);
            } catch (Exception exception) {
                AzaleaBroadcaster.getInstance().warn("Could not serialize entry '" + key + "': " + exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        configuration.getKeys(false).forEach(key -> {
            try {
                add(key, deserializeEntry(configuration.getConfigurationSection(key)));
            } catch (Exception exception) {
                AzaleaBroadcaster.getInstance().warn("Could not deserialize entry '" + key + "': "+ exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    protected abstract void serializeEntry(ConfigurationSection section, T entry);

    protected abstract T deserializeEntry(ConfigurationSection section);
}
