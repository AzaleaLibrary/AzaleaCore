package net.azalealibrary.core.round;

public final class Tick {

    private final int tick;

    public Tick(int tick) {
        this.tick = tick;
    }

    public int getTicks() {
        return tick;
    }

    public int getSeconds() {
        return tick / 20;
    }

    public boolean isFullSecond() {
        return tick % 20 == 0;
    }
}
