package com.azalealibrary.azaleacore.api;

import java.util.function.Predicate;

public class WinCondition<R extends Round<? extends Minigame<?>>> {

    private final Team winningTeam;
    private final String reason;
    private final int winAward;
    private final Predicate<R> condition;

    public WinCondition(Team winningTeam, String reason, int winAward, Predicate<R> condition) {
        this.winningTeam = winningTeam;
        this.reason = reason;
        this.winAward = winAward;
        this.condition = condition;
    }

    public Team getWinningTeam() {
        return winningTeam;
    }

    public String getReason() {
        return reason;
    }

    public int getWinAward() {
        return winAward;
    }

    public boolean evaluate(R round) {
        return condition.test(round);
    }
}
