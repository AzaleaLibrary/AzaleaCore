package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.room.Room;

public class RoundEvent {

    private WinCondition condition;
    private final Round round;
    private final Room room;
    private final Phase phase;
    private final int tick;

    protected RoundEvent(Round round, Room room, Phase phase, int tick) {
        this.round = round;
        this.room = room;
        this.phase = phase;
        this.tick = tick;
    }

    public Round getRound() {
        return round;
    }

    public Room getRoom() {
        return room;
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
