package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.broadcast.message.TitleMessage;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;

import java.util.function.Predicate;

public class WinCondition<R extends Round<? extends Minigame<?>>> {

    private final TitleMessage titleMessage;
    private final int winAward;
    private final Predicate<R> condition;

    public WinCondition(TitleMessage titleMessage, int winAward, Predicate<R> condition) {
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

    public boolean evaluate(R round) {
        return condition.test(round);
    }
}
