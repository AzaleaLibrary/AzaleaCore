package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.room.Room;

public abstract class RoundEvent {

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

    public enum Phase {
        IDLE, GRACE, ONGOING
    }

    public static class Setup extends RoundEvent {

        public Setup(Round round, Room room, Phase phase, int tick) {
            super(round, room, phase, tick);
        }
    }

    public static class Start extends RoundEvent {

        public Start(Round round, Room room, Phase phase, int tick) {
            super(round, room, phase, tick);
        }
    }

    public static class Tick extends RoundEvent {

        private WinCondition condition;

        public Tick(Round round, Room room, Phase phase, int tick) {
            super(round, room, phase, tick);
        }

        public WinCondition getCondition() {
            return condition;
        }

        public void setCondition(WinCondition condition) {
            this.condition = condition;
        }
    }

    public static class Win extends End {

        public Win(WinCondition condition, Round round, Room room, Phase phase, int tick) {
            super(round, room, phase, tick);
            setCondition(condition);
        }
    }

    public static class End extends Tick {

        private boolean restart = false;

        public End(Round round, Room room, Phase phase, int tick) {
            super(round, room, phase, tick);
        }

        public void restart() {
            setCondition(null);
            restart = true;
        }

        public boolean shouldRestart() {
            return restart;
        }
    }
}
