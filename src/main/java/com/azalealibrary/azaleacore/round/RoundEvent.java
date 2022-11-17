package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.room.Room;
import org.checkerframework.checker.nullness.qual.Nullable;

public abstract class RoundEvent {

    private final Room room;

    protected RoundEvent(Room room) {
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public static class Setup extends RoundEvent {

        public Setup(Room room) {
            super(room);
        }
    }

    public static class Start extends RoundEvent {

        public Start(Room room) {
            super(room);
        }
    }

    public static class Tick extends RoundEvent {

        private @Nullable WinCondition<?> condition;
        // TODO - tick phase

        public Tick(Room room) {
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

        public Win(WinCondition<?> condition, Room room) {
            super(room);
            setCondition(condition);
        }
    }

    public static class End extends Tick {

        private boolean restart = false;

        public End(Room room) {
            super(room);
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
