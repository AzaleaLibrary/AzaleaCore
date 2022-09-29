package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.minigame.MinigameConfiguration;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import org.bukkit.World;

import java.util.HashMap;

public final class AzaleaApi {

    public static final HashMap<String, MinigameProvider<?>> REGISTERED_MINIGAME = new HashMap<>();
    public static final HashMap<World, MinigameController<?, ?>> RUNNING_MINIGAMES = new HashMap<>();

    public static void registerMinigame(String name, MinigameProvider<?> minigame) {
        if (REGISTERED_MINIGAME.containsKey(name)) {
            throw new IllegalArgumentException("Minigame with name '" + name + "' already registered.");
        }
        REGISTERED_MINIGAME.put(name, minigame);
    }

    @SuppressWarnings("unchecked")
    public static <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameController<M, R>  createController(Minigame<?> minigame, MinigameConfiguration configuration) {
        World world = minigame.getWorld();
        MinigameController<M, R> controller = new MinigameController<>((M) minigame, configuration);
        RUNNING_MINIGAMES.put(world, controller);
        return controller;
    }

    @FunctionalInterface
    public interface MinigameProvider<M extends Minigame<?>> {
        M create(World world);
    }
}
