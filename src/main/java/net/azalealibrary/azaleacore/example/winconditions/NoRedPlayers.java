package net.azalealibrary.azaleacore.example.winconditions;

import net.azalealibrary.azaleacore.api.WinCondition;
import net.azalealibrary.azaleacore.example.Registry;
import net.azalealibrary.azaleacore.round.Round;

public class NoRedPlayers extends WinCondition {

    public NoRedPlayers() {
        super("All red players have been eliminated", 100, Registry.BLUE_TEAM);
    }

    @Override
    public boolean evaluate(Round round) {
        return round.getTeams().getAllInTeam(Registry.RED_TEAM).isEmpty();
    }
}
