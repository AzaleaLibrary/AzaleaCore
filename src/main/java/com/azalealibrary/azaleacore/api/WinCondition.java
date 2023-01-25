package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.round.Round;

public abstract class WinCondition {

    private final MinigameTeam winners;
    private final String reason;
    private final int winAward;

    public WinCondition(MinigameTeam winningMinigameTeam, String reason, int winAward) {
        this.winners = winningMinigameTeam;
        this.reason = reason;
        this.winAward = winAward;
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

    public abstract boolean evaluate(Round round);
}
