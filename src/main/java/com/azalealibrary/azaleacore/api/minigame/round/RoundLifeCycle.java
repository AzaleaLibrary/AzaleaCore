package com.azalealibrary.azaleacore.api.minigame.round;

import com.azalealibrary.azaleacore.api.minigame.Minigame;

public interface RoundLifeCycle<M extends Minigame> {

    void onGrace(RoundEvent.Start<M> event);

    void onStart(RoundEvent.Start<M> event);

    void onTick(RoundEvent.Tick<M> event);

    void onWin(RoundEvent.Win<M> event);

    void onEnd(RoundEvent.End<M> event);
}
