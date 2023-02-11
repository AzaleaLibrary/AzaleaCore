package net.azalealibrary.core.round;

import net.azalealibrary.core.api.WinCondition;

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

        private final net.azalealibrary.core.round.Tick tick;
        private WinCondition condition;

        protected Tick(Round round, int tick) {
            super(round);
            this.tick = new net.azalealibrary.core.round.Tick(tick);
        }

        public net.azalealibrary.core.round.Tick getTick() {
            return tick;
        }

        public WinCondition getWinCondition() {
            return condition;
        }

        public void setWinCondition(WinCondition condition) {
            this.condition = condition;
        }
    }

    public static class Win extends Tick {

        protected Win(Round round, int tick, WinCondition condition) {
            super(round, tick);
            setWinCondition(condition);
        }
    }

    public static class End extends Tick {

        protected End(Round round, int tick) {
            super(round, tick);
        }
    }
}
