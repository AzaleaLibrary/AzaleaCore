package com.azalealibrary.azaleacore.foundation.registry;

import com.azalealibrary.azaleacore.api.core.MinigameItem;
import com.azalealibrary.azaleacore.api.core.MinigameTeam;
import com.azalealibrary.azaleacore.api.core.Round;
import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.EventBus;

import java.util.function.Supplier;

public final class AzaleaRegistry<T> {

    public static final EventBus EVENT_BUS = new EventBus("registry");

    public static final AzaleaRegistry<Supplier<Round>> ROUND = new AzaleaRegistry<>("minigame", RegistryEvent.Rounds::new);
    public static final AzaleaRegistry<MinigameItem> ITEM = new AzaleaRegistry<>("item", RegistryEvent.Items::new);
    public static final AzaleaRegistry<MinigameTeam> TEAM = new AzaleaRegistry<>("team", RegistryEvent.Teams::new);
    public static final AzaleaRegistry<WinCondition<?>> WIN_CONDITION = new AzaleaRegistry<>("win condition", RegistryEvent.WinConditions::new);
    public static final AzaleaRegistry<ConfigurableProperty<?>> PROPERTY = new AzaleaRegistry<>("property", RegistryEvent.Properties::new);

    private final String name;
    private final Supplier<?> event;

    private ImmutableMap<MinigameIdentifier, T> objects;

    public AzaleaRegistry(String name, Supplier<?> event) {
        this.name = name;
        this.event = event;
    }

    public ImmutableList<MinigameIdentifier> getKeys() {
        return objects.keySet().asList();
    }

    public ImmutableList<T> getObjects() {
        return objects.values().asList();
    }

    public ImmutableMap<MinigameIdentifier, T> getEntries() {
        return objects;
    }

    public T get(MinigameIdentifier key) {
        return objects.get(key);
    }

    public boolean hasKey(MinigameIdentifier key) {
        return objects.containsKey(key);
    }

    public <E extends RegistryEvent<T>> void bake() {
        if (objects != null) {
            throw new RuntimeException("Attempting to re-bake already baked " + name + " registry.");
        }

        E registryEvent = (E) event.get();
        AzaleaRegistry.EVENT_BUS.post(registryEvent);
        objects = ImmutableMap.copyOf(registryEvent.getObjects());
    }
}
