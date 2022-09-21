package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.event.MinigameEvent;

public interface MinigameLifeCycle {
    /**
     * Called when the minigame has started.
     * @param event Minigame event.
     */
    void onGrace(MinigameEvent<?> event);

    /**
     * Called when the minigame round has started.
     * @param event Minigame event.
     */
    void onStart(MinigameEvent<?> event);

    /**
     * Called on every minigame tick.
     * @param event Minigame event.
     */
    void onTick(MinigameEvent<?> event);

    /**
     * Called when minigame has ended without a win condition.
     * @param event Minigame event.
     */
    void onEnd(MinigameEvent<?> event);

    /**
     * Called when minigame has ended with a win condition.
     * @param event Minigame event.
     */
    void onWin(MinigameEvent<?> event);
}
