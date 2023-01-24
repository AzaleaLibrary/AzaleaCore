package com.azalealibrary.azaleacore.foundation.registry;

import com.azalealibrary.azaleacore.api.MinigameItem;
import com.azalealibrary.azaleacore.api.MinigameTeam;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.command.CommandNode;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.minigame.MinigameIdentifier;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class RegistryEvent<T> {

    private final Map<MinigameIdentifier.Tag, T> objects = new HashMap<>();

    public Map<MinigameIdentifier.Tag, T> getObjects() {
        return objects;
    }

    public RegistryEvent<T> register(MinigameIdentifier.Tag tag, T object) {
        // TODO - allow mechanism to override entries?
        if (objects.keySet().stream().anyMatch(key -> key.equals(tag))) {
            throw new IllegalArgumentException("Registering " + getName() + " with duplicate tag '" + tag + "'.");
        }
        objects.put(tag, object);
        return this;
    }

    public abstract String getName();

    public static class Minigames extends RegistryEvent<MinigameIdentifier> {
        @Override
        public String getName() {
            return "minigame";
        }

        @Override
        public RegistryEvent<MinigameIdentifier> register(MinigameIdentifier.Tag tag, MinigameIdentifier object) {
            throw new UnsupportedOperationException();
        }

        public void registerMinigame(MinigameIdentifier minigame) {
            super.register(minigame.tag("minigame"), minigame);
        }
    }

    public static class Rounds extends RegistryEvent<Supplier<Object>> {
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

    public static class WinConditions extends RegistryEvent<WinCondition> {
        @Override
        public String getName() {
            return "win condition";
        }
    }

    public static class Properties extends RegistryEvent<Supplier<ConfigurableProperty<?>>> {
        @Override
        public String getName() {
            return "property";
        }
    }

    public static class Commands extends RegistryEvent<CommandNode> {
        @Override
        public String getName() {
            return "commands";
        }
    }
}
