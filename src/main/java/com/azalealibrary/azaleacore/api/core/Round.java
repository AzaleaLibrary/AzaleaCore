package com.azalealibrary.azaleacore.api.core;

import com.azalealibrary.azaleacore.foundation.Hooks;
import com.azalealibrary.azaleacore.round.RoundEvent;
import com.azalealibrary.azaleacore.round.RoundLifeCycle;
import com.azalealibrary.azaleacore.round.RoundTeams;

import java.util.Objects;

public abstract class Round implements RoundLifeCycle {

    private final RoundTeams teams;
    private int tick = 0;

    public Round(RoundTeams teams) {
        this.teams = teams;
    }

    public RoundTeams getRoundTeams() {
        return teams;
    }

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }

    @Override
    public void onSetup(RoundEvent.Setup event) {
        event.getRoom().teleportAllToWorld();
        getRoundTeams().prepareAll();
        Hooks.showStartScreen(getRoundTeams(), event.getRoom().getBroadcaster());
    }

    @Override
    public void onWin(RoundEvent.Win event) {
        WinCondition<?> winCondition = Objects.requireNonNull(event.getCondition());
        Hooks.showEndScreen(getRoundTeams(), event.getRoom().getBroadcaster(), winCondition);
        Hooks.awardPoints(getRoundTeams(), winCondition);
    }

    @Override
    public void onEnd(RoundEvent.End event) {
        event.getRoom().teleportAllToWorld();
        getRoundTeams().resetAll();
    }
}
