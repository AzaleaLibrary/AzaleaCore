package com.azalealibrary.azaleacore.api.minigame.round;

import com.azalealibrary.azaleacore.api.broadcast.MinigameBroadcaster;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Round<M extends Minigame<?>> implements RoundLifeCycle<M> {

    private final List<Player> players;
    private final MinigameBroadcaster broadcaster;
    private int tick = 0;

    public Round(List<Player> players, MinigameBroadcaster broadcaster) {
        this.players = players;
        this.broadcaster = broadcaster;
    }

    public MinigameBroadcaster getBroadcaster() {
        return broadcaster;
    }

    public ImmutableList<Player> getPlayers() {
        return ImmutableList.copyOf(players);
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
