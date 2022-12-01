package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.core.WinCondition;

public class RoundEvent {

    private WinCondition condition;
    private final Round round;
    private final Phase phase;
    private final int tick;

    protected RoundEvent(Round round, Phase phase, int tick) {
        this.round = round;
        this.phase = phase;
        this.tick = tick;
    }

    public Round getRound() {
        return round;
    }

    public Phase getPhase() {
        return phase;
    }

    public int getTick() {
        return tick;
    }

    public WinCondition getCondition() {
        return condition;
    }

    public void setCondition(WinCondition condition) {
        this.condition = condition;
    }

    public enum Phase {
        IDLE, GRACE, ONGOING
    }
}
