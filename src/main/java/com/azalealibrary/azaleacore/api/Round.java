package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.minigame.round.RoundLifeCycle;
import com.azalealibrary.azaleacore.minigame.round.RoundTeams;

public abstract class Round<M extends Minigame<?>> implements RoundLifeCycle<M> {

    private final RoundTeams teams;
    private final Broadcaster broadcaster;
    private int tick = 0;

    public Round(RoundTeams teams, Broadcaster broadcaster) {
        this.teams = teams;
        this.broadcaster = broadcaster;
    }

    public RoundTeams getRoundTeams() {
        return teams;
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
