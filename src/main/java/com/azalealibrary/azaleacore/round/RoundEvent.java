package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.WinCondition;

public abstract class RoundEvent {

    private final Round round;

    protected RoundEvent(Round round) {
        this.round = round;
    }

    public Round getRound() {
        return round;
    }

    public static class Start extends RoundEvent {

        protected Start(Round round) {
            super(round);
        }
    }

    public static class Tick extends RoundEvent {

        private final com.azalealibrary.azaleacore.round.Tick tick;
        private WinCondition condition;

        protected Tick(Round round, int tick) {
            super(round);
            this.tick = new com.azalealibrary.azaleacore.round.Tick(tick);
        }

        public com.azalealibrary.azaleacore.round.Tick getTick() {
            return tick;
        }

        public WinCondition getCondition() {
            return condition;
        }

        public void setCondition(WinCondition condition) {
            this.condition = condition;
        }
    }

    public static class Win extends Tick {

        protected Win(Round round, int tick, WinCondition condition) {
            super(round, tick);
            setCondition(condition);
        }
    }

    public static class End extends Tick {

        protected End(Round round, int tick) {
            super(round, tick);
        }
    }
}
