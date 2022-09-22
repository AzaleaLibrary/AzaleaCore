package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.broadcast.message.TitleMessage;
import com.azalealibrary.azaleacore.api.minigame.Minigame;

import java.util.function.Predicate;

public class WinCondition<M extends Minigame> {

    private final TitleMessage titleMessage;
    private final int winAward;
    private final Predicate<M> condition;

    public WinCondition(TitleMessage titleMessage, int winAward, Predicate<M> condition) {
        this.titleMessage = titleMessage;
        this.winAward = winAward;
        this.condition = condition;
    }

    public TitleMessage getTitleMessage() {
        return titleMessage;
    }

    public int getWinAward() {
        return winAward;
    }

    public boolean meetsCondition(M minigame) {
        return condition.test(minigame);
    }
}
