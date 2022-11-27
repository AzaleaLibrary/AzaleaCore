package com.azalealibrary.azaleacore.api.core;

import com.azalealibrary.azaleacore.round.Round;

import java.util.function.Predicate;

public class WinCondition {

    private final MinigameTeam winners;
    private final Predicate<Round> condition;
    private final String reason;
    private final int winAward;

    public WinCondition(MinigameTeam winningMinigameTeam, String reason, int winAward, Predicate<Round> condition) {
        this.winners = winningMinigameTeam;
        this.reason = reason;
        this.winAward = winAward;
        this.condition = condition;
    }

    public MinigameTeam getWinningTeam() {
        return winners;
    }

    public String getReason() {
        return reason;
    }

    public int getWinAward() {
        return winAward;
    }

    public <R extends Round> boolean evaluate(R round) {
        return condition.test(round);
    }
}
