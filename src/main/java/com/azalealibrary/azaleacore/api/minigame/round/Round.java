package com.azalealibrary.azaleacore.api.minigame.round;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Round<M extends Minigame> implements RoundLifeCycle<M> {

    private final List<Player> players;
    private int tick = 0;

    public Round(List<Player> players) {
        this.players = players;
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
