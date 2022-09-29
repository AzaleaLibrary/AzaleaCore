package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import com.google.common.collect.ImmutableMap;
import org.bukkit.World;

import java.util.HashMap;

public final class AzaleaApi {

    private static final HashMap<String, MinigameProvider<?>> REGISTERED_MINIGAMES = new HashMap<>();
    private static final HashMap<World, MinigameController<?, ?>> MINIGAME_ROOMS = new HashMap<>();

    public static ImmutableMap<String, MinigameProvider<?>> getRegisteredMinigames() {
        return ImmutableMap.copyOf(REGISTERED_MINIGAMES);
    }

    public static ImmutableMap<World, MinigameController<?, ?>> getMinigameRooms() {
        return ImmutableMap.copyOf(MINIGAME_ROOMS);
    }

    public static void registerMinigame(String name, MinigameProvider<?> minigame) {
        if (REGISTERED_MINIGAMES.containsKey(name)) {
            throw new IllegalArgumentException("Minigame with name '" + name + "' already registered.");
        }
        REGISTERED_MINIGAMES.put(name, minigame);
    }

    @SuppressWarnings("unchecked")
    public static <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameController<M, R> createMinigameRoom(World world, MinigameProvider<?> provider) {
        MinigameController<M, R> controller = new MinigameController<>((M) provider.create(world));
        MINIGAME_ROOMS.put(world, controller);
        return controller;
    }

    @FunctionalInterface
    public interface MinigameProvider<M extends Minigame<?>> {
        M create(World world);
    }
}
