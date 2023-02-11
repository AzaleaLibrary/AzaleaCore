package net.azalealibrary.core.api;

import net.azalealibrary.core.round.Round;

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
