package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.minigame.round.RoundLifeCycle;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Round<M extends Minigame<?>> implements RoundLifeCycle<M> {

    private final List<Player> players;
    private final Broadcaster broadcaster;
    private int tick = 0;

    public Round(List<Player> players, Broadcaster broadcaster) {
        this.players = players;
        this.broadcaster = broadcaster;
    }

    public ImmutableList<Player> getPlayers() {
        return ImmutableList.copyOf(players);
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
