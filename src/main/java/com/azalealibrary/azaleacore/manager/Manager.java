package com.azalealibrary.azaleacore.manager;

import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.Serializable;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public abstract class Manager<T> implements Serializable {

    private final String thing;
    private final Map<String, T> objects = new HashMap<>();

    protected Manager(String thing) {
        this.thing = thing;
    }

    public ImmutableList<T> getAll() {
        return ImmutableList.copyOf(objects.values());
    }

    public ImmutableList<String> getKeys() {
        return ImmutableList.copyOf(objects.keySet());
    }

    public @Nullable T get(String name) {
        return objects.get(name);
    }

    public @Nullable String getKey(T object) {
        return objects.entrySet().stream()
                .filter(e -> object.equals(e.getValue()))
                .map(Map.Entry::getKey)
                .findFirst().orElse(null);
    }

    public boolean exists(String name) {
        return objects.containsKey(name);
    }

    public void add(String name, T object) {
        if (exists(name)) {
            throw new AzaleaException(String.format("%s '%s' already exists.", StringUtils.capitalize(thing), name));
        }
        objects.put(name, object);
    }

    public void remove(T object) {
        String key = getKey(object);
        if (key == null) {
            throw new AzaleaException(String.format("%s '%s' does not exists.", StringUtils.capitalize(thing), key));
        }
        objects.remove(key);
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        objects.forEach((key, entry) -> {
            try {
                YamlConfiguration section = new YamlConfiguration();
                serializeEntry(section, entry);
                configuration.set(key, section);
            } catch (Exception exception) {
                System.err.println("Could not serialize entry '" + key + "': " + exception.getMessage());
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
                System.err.println("Could not deserialize entry '" + key + "': "+ exception.getMessage());
                exception.printStackTrace();
            }
        });
    }

    protected abstract void serializeEntry(ConfigurationSection section, T entry);

    protected abstract T deserializeEntry(ConfigurationSection section);
}
