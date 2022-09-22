package com.azalealibrary.azaleacore.api.minigame.round;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.minigame.Minigame;

public abstract class RoundEvent<M extends Minigame> {

    private final M minigame;

    protected RoundEvent(M minigame) {
        this.minigame = minigame;
    }

    public M getMinigame() {
        return minigame;
    }

    public static class Start<M extends Minigame> extends RoundEvent<M> {

        public Start(M minigame) {
            super(minigame);
        }
    }

    public static class Tick<M extends Minigame> extends RoundEvent<M> {

        private WinCondition<M> condition;

        public Tick(M minigame) {
            super(minigame);
        }

        public WinCondition<M> getCondition() {
            return condition;
        }

        public void setCondition(WinCondition<M> condition) {
            this.condition = condition;
        }
    }

    public static class End<M extends Minigame> extends Tick<M> {

        private boolean restart = false;

        public End(M minigame) {
            super(minigame);
        }

        public void restart() {
            restart = true;
        }

        public boolean doRestart() {
            return restart;
        }
    }

    public static class Win<M extends Minigame> extends End<M> {

        public Win(M minigame, WinCondition<M> condition) {
            super(minigame);
            setCondition(condition);
        }
    }
}
