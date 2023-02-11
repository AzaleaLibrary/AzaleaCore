package net.azalealibrary.core.example.winconditions;

import net.azalealibrary.core.api.WinCondition;
import net.azalealibrary.core.example.Registry;
import net.azalealibrary.core.round.Round;

public class NoBluePlayers extends WinCondition {

    public NoBluePlayers() {
        super("All blue players have been eliminated", 100, Registry.RED_TEAM);
    }

    @Override
    public boolean evaluate(Round round) {
        return round.getTeams().getAllInTeam(Registry.BLUE_TEAM).isEmpty();
    }
}
