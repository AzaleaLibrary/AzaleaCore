package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import org.bukkit.World;

import javax.annotation.Nullable;

public class MinigameRoom<M extends Minigame<? extends Round<M>>, R extends Round<M>> {

    private final String name;
    private final World world;
    private final M minigame;
    private final RoundTicker<M, R> ticker;

    public MinigameRoom(String name, World world, M minigame) {
        this.name = name;
        this.world = world;
        this.minigame = minigame;
        this.ticker = new RoundTicker<>(minigame, minigame.getConfiguration());
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public M getMinigame() {
        return minigame;
    }

    public void start(@Nullable Message message) {
        if (ticker.isRunning()) {
            throw new RuntimeException("Attempting to begin round while round is already running.");
        }

        // TODO - remove players param and systematically use world players?
        ticker.begin((R) minigame.newRound(world.getPlayers()));

        if (message != null) {
            ticker.getRound().getBroadcaster().broadcast(message);
        }
    }

    public void stop(@Nullable Message message) {
        if (!ticker.isRunning()) {
            throw new RuntimeException("Attempting to end round while round is not running.");
        }

        ticker.cancel();

        if (message != null) {
            ticker.getRound().getBroadcaster().broadcast(message);
        }
    }

    public void restart(@Nullable Message message) {
        // stop any running minigame and ignore exceptions
        // TODO - create MinigameException/AzaleaException exceptions
        try { stop(null); } catch (Exception ignored) { }

        if (ticker.getRound() == null) {
            throw new RuntimeException("Cannot restart a minigame that is not running.");
        }

        start(message);
    }
}
