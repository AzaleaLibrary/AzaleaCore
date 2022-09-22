package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.minigame.MinigameConfiguration;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import org.bukkit.World;

import java.util.*;

public final class AzaleaApi {

    private static final List<Minigame> REGISTERED_MINIGAME = new ArrayList<>();
    private static final HashMap<World, MinigameController<?>> RUNNING_MINIGAMES = new HashMap<>();

    public static void registerMinigame(Minigame minigame) {
        Optional<Minigame> current = REGISTERED_MINIGAME.stream().filter(m -> Objects.equals(m.getName(), minigame.getName())).findFirst();

        if (current.isPresent()) {
            throw new IllegalArgumentException("Minigame with name '" + minigame.getName() + "' already registered.");
        }

        REGISTERED_MINIGAME.add(minigame);
    }

    public static MinigameController<?> createController(String name, MinigameConfiguration configuration) {
        Optional<Minigame> minigame = REGISTERED_MINIGAME.stream().filter(m -> Objects.equals(m.getName(), name)).findFirst();

        if (minigame.isEmpty()) {
            throw new IllegalArgumentException("Minigame with name '" + name + "' not registered.");
        }

        return createController(minigame.get(), configuration);
    }

    public static MinigameController<?> createController(Minigame minigame, MinigameConfiguration configuration) {
        World world = configuration.getWorld();
        MinigameController<?> controller = new MinigameController<>(minigame, world.getPlayers(), configuration);
        RUNNING_MINIGAMES.put(world, controller);
        return controller;
    }
}
