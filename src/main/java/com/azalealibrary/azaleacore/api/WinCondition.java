package com.azalealibrary.azaleacore.api;

import java.util.function.Predicate;

public class WinCondition<R extends Round> {

    private final MinigameTeam winningMinigameTeam;
    private final String reason;
    private final int winAward;
    private final Predicate<R> condition;

    public WinCondition(MinigameTeam winningMinigameTeam, String reason, int winAward, Predicate<R> condition) {
        this.winningMinigameTeam = winningMinigameTeam;
        this.reason = reason;
        this.winAward = winAward;
        this.condition = condition;
    }

    public MinigameTeam getWinningTeam() {
        return winningMinigameTeam;
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
