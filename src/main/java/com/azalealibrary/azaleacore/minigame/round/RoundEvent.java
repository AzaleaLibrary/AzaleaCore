package com.azalealibrary.azaleacore.minigame.round;

import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.minigame.MinigameRoom;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class RoundEvent<M extends Minigame> {

    private final MinigameRoom<M, ?> room;

    protected RoundEvent(MinigameRoom<M, ?> room) {
        this.room = room;
    }

    public MinigameRoom<M, ?> getRoom() {
        return room;
    }

    public static class Setup<M extends Minigame> extends RoundEvent<M> {

        public Setup(MinigameRoom<M, ?> room) {
            super(room);
        }
    }

    public static class Start<M extends Minigame> extends RoundEvent<M> {

        public Start(MinigameRoom<M, ?> room) {
            super(room);
        }
    }

    public static class Tick<M extends Minigame> extends RoundEvent<M> {

        private @Nullable WinCondition<?> condition;
        // TODO - tick phase

        public Tick(MinigameRoom<M, ?> room) {
            super(room);
        }

        public @Nullable WinCondition<?> getCondition() {
            return condition;
        }

        public void setCondition(@Nullable WinCondition<?> condition) {
            this.condition = condition;
        }
    }

    public static class Win<M extends Minigame> extends End<M> {

        public Win(M minigame, WinCondition<?> condition, MinigameRoom<M, ?> room) {
            super(room);
            setCondition(condition);
        }
    }

    public static class End<M extends Minigame> extends Tick<M> {

        private boolean restart = false;

        public End(MinigameRoom<M, ?> room) {
            super(room);
        }

        public void restart() {
            restart = true;
        }

        public boolean doRestart() {
            return restart;
        }
    }
}
