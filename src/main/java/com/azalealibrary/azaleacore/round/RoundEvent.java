package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.core.WinCondition;

public abstract class RoundEvent {

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

    public enum Phase {
        IDLE, GRACE, ONGOING
    }

    public static class Setup extends RoundEvent {

        public Setup(Round round, Phase phase, int tick) {
            super(round, phase, tick);
        }
    }

    public static class Start extends RoundEvent {

        public Start(Round round, Phase phase, int tick) {
            super(round, phase, tick);
        }
    }

    public static class Tick extends RoundEvent {

        private WinCondition condition;

        public Tick(Round round, Phase phase, int tick) {
            super(round, phase, tick);
        }

        public WinCondition getCondition() {
            return condition;
        }

        public void setCondition(WinCondition condition) {
            this.condition = condition;
        }
    }

    public static class Win extends End {

        public Win(WinCondition condition, Round round, Phase phase, int tick) {
            super(round, phase, tick);
            setCondition(condition);
        }
    }

    public static class End extends Tick {

        private boolean restart = false;

        public End(Round round, Phase phase, int tick) {
            super(round, phase, tick);
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
