package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.Hooks;
import com.azalealibrary.azaleacore.minigame.round.RoundEvent;
import com.azalealibrary.azaleacore.minigame.round.RoundLifeCycle;
import com.azalealibrary.azaleacore.minigame.round.RoundTeams;

import java.util.Objects;

public abstract class Round<M extends Minigame> implements RoundLifeCycle<M> {

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
    public void onSetup(RoundEvent.Setup<M> event) {
        event.getRoom().teleportToWorld();
        getRoundTeams().prepareAll();
        Hooks.showStartScreen(getRoundTeams(), event.getRoom().getBroadcaster());
    }

    @Override
    public void onWin(RoundEvent.Win<M> event) {
        WinCondition<?> winCondition = Objects.requireNonNull(event.getCondition());
        Hooks.showEndScreen(getRoundTeams(), event.getRoom().getBroadcaster(), winCondition);
        Hooks.awardPoints(this, winCondition);
    }

    @Override
    public void onEnd(RoundEvent.End<M> event) {
        event.getRoom().teleportToWorld();
        getRoundTeams().resetAll();
    }
}
