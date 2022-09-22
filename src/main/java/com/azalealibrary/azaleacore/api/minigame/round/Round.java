package com.azalealibrary.azaleacore.api.minigame.round;

import com.azalealibrary.azaleacore.api.minigame.Minigame;

public abstract class Round<M extends Minigame> implements RoundLifeCycle<M> {

    private int tick = 0;

    public int getTick() {
        return tick;
    }

    public void setTick(int tick) {
        this.tick = tick;
    }
}
