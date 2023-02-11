package net.azalealibrary.core.example.winconditions;

import net.azalealibrary.core.api.WinCondition;
import net.azalealibrary.core.example.Registry;
import net.azalealibrary.core.round.Round;

public class NoRedPlayers extends WinCondition {

    public NoRedPlayers() {
        super("All red players have been eliminated", 100, Registry.BLUE_TEAM);
    }

    @Override
    public boolean evaluate(Round round) {
        return round.getTeams().getAllInTeam(Registry.RED_TEAM).isEmpty();
    }
}
