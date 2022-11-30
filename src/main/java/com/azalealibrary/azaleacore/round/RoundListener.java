package com.azalealibrary.azaleacore.round;

public interface RoundListener {

    void onSetup(RoundEvent event);

    void onStart(RoundEvent event);

    void onTick(RoundEvent event);

    void onWin(RoundEvent event);

    void onEnd(RoundEvent event);
}
