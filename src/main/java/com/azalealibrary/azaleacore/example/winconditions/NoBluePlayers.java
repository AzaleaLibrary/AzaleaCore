package com.azalealibrary.azaleacore.example.winconditions;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.round.Round;

public class NoBluePlayers extends WinCondition {

    public NoBluePlayers() {
        super(null, "All blue players have been eliminated", 100);
    }

    @Override
    public boolean evaluate(Round round) {
        return round.getTeams().getAllInTeam(null).isEmpty();
    }
}
