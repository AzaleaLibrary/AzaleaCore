package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.minigame.MinigameConfiguration;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import org.bukkit.World;

import java.util.HashMap;

public final class AzaleaApi {

    public static final HashMap<World, MinigameController<?, ?>> MINIGAMES = new HashMap<>();

    public static <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameController<M, R>  createController(M minigame, MinigameConfiguration configuration) {
        World world = minigame.getWorld();
        MinigameController<M, R> controller = new MinigameController<>(minigame, configuration);
        MINIGAMES.put(world, controller);
        return controller;
    }
}
