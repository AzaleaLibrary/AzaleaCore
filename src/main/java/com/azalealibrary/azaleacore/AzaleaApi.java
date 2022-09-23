package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.minigame.MinigameConfiguration;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import org.bukkit.World;

import java.util.*;

public final class AzaleaApi {

    private static final List<Minigame<?>> REGISTERED_MINIGAME = new ArrayList<>();
    private static final HashMap<World, MinigameController<?, ?>> RUNNING_MINIGAMES = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static <M extends Minigame<? extends Round<?>>, R extends Round<M>> void registerMinigame(M minigame) {
        Optional<M> current = (Optional<M>) REGISTERED_MINIGAME.stream().filter(m -> Objects.equals(m.getName(), minigame.getName())).findFirst();

        if (current.isPresent()) {
            throw new IllegalArgumentException("Minigame with name '" + minigame.getName() + "' already registered.");
        }

        REGISTERED_MINIGAME.add(minigame);
    }

    @SuppressWarnings("unchecked")
    public static <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameController<M, R> createController(String name, MinigameConfiguration configuration) {
        Optional<M> minigame = (Optional<M>) REGISTERED_MINIGAME.stream().filter(m -> Objects.equals(m.getName(), name)).findFirst();

        if (minigame.isEmpty()) {
            throw new IllegalArgumentException("Minigame with name '" + name + "' not registered.");
        }

        return createController(minigame.get(), configuration);
    }

    public static <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameController<M, R>  createController(M minigame, MinigameConfiguration configuration) {
        World world = minigame.getWorld();
        MinigameController<M, R> controller = new MinigameController<>(minigame, configuration);
        RUNNING_MINIGAMES.put(world, controller);
        return controller;
    }
}
