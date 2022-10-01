package com.azalealibrary.azaleacore.minigame.round;

import com.azalealibrary.azaleacore.api.Minigame;

public interface RoundLifeCycle<M extends Minigame<?>> {

    void onSetup(RoundEvent.Setup<M> event);

    void onStart(RoundEvent.Start<M> event);

    void onTick(RoundEvent.Tick<M> event);

    void onWin(RoundEvent.Win<M> event);

    void onEnd(RoundEvent.End<M> event);
}
