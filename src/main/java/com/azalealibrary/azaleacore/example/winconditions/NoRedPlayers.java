package com.azalealibrary.azaleacore.example.winconditions;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.example.Registry;
import com.azalealibrary.azaleacore.round.Round;

public class NoRedPlayers extends WinCondition {

    public NoRedPlayers() {
        super("All red players have been eliminated", 100, Registry.BLUE_TEAM);
    }

    @Override
    public boolean evaluate(Round round) {
        return round.getTeams().getAllInTeam(Registry.RED_TEAM).isEmpty();
    }
}
