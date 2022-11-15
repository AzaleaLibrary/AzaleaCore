package com.azalealibrary.azaleacore.round;

public interface RoundLifeCycle {

    void onSetup(RoundEvent.Setup event);

    void onStart(RoundEvent.Start event);

    void onTick(RoundEvent.Tick event);

    void onWin(RoundEvent.Win event);

    void onEnd(RoundEvent.End event);
}
