package com.azalealibrary.azaleacore.foundation.registry;

import com.azalealibrary.azaleacore.api.core.MinigameItem;
import com.azalealibrary.azaleacore.api.core.MinigameTeam;
import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.round.RoundLifeCycle;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class RegistryEvent<T> {

    private final Map<MinigameIdentifier, T> objects = new HashMap<>();

    public abstract String getName();

    public Map<MinigameIdentifier, T> getObjects() {
        return objects;
    }

    public RegistryEvent<T> register(MinigameIdentifier identifier, T object) {
        if (objects.keySet().stream().anyMatch(key -> key.equals(identifier))) {
            throw new IllegalArgumentException("Registering " + getName() + " with duplicate key '" + identifier + "'.");
        }
        objects.put(identifier, object);
        return this;
    }

    public static class Rounds extends RegistryEvent<Supplier<RoundLifeCycle>> {
        @Override
        public String getName() {
            return "round";
        }
    }

    public static class Items extends RegistryEvent<MinigameItem> {
        @Override
        public String getName() {
            return "item";
        }
    }

    public static class Teams extends RegistryEvent<MinigameTeam> {
        @Override
        public String getName() {
            return "team";
        }
    }

    public static class WinConditions extends RegistryEvent<WinCondition<?>> {
        @Override
        public String getName() {
            return "win condition";
        }
    }

    public static class Properties extends RegistryEvent<ConfigurableProperty<?>> {
        @Override
        public String getName() {
            return "property";
        }
    }
}
