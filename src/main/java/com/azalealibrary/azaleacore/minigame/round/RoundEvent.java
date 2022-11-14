package com.azalealibrary.azaleacore.minigame.round;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.room.MinigameRoom;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class RoundEvent {

    private final MinigameRoom room;

    protected RoundEvent(MinigameRoom room) {
        this.room = room;
    }

    public MinigameRoom getRoom() {
        return room;
    }

    public static class Setup extends RoundEvent {

        public Setup(MinigameRoom room) {
            super(room);
        }
    }

    public static class Start extends RoundEvent {

        public Start(MinigameRoom room) {
            super(room);
        }
    }

    public static class Tick extends RoundEvent {

        private @Nullable WinCondition<?> condition;
        // TODO - tick phase

        public Tick(MinigameRoom room) {
            super(room);
        }

        public @Nullable WinCondition<?> getCondition() {
            return condition;
        }

        public void setCondition(@Nullable WinCondition<?> condition) {
            this.condition = condition;
        }
    }

    public static class Win extends End {

        public Win(WinCondition<?> condition, MinigameRoom room) {
            super(room);
            setCondition(condition);
        }
    }

    public static class End extends Tick {

        private boolean restart = false;

        public End(MinigameRoom room) {
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
