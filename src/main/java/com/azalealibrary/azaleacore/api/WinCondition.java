package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.round.Round;

import java.util.Set;

public abstract class WinCondition {

    private final Set<MinigameTeam> winners;
    private final String reason;
    private final int winAward;

    public WinCondition(String reason, int winAward, MinigameTeam... winners) {
        this.winners = Set.of(winners);
        this.reason = reason;
        this.winAward = winAward;
    }

    public Set<MinigameTeam> getWinners() {
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
