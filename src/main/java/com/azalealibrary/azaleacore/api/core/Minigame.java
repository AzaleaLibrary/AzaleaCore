package com.azalealibrary.azaleacore.api.core;

import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.round.RoundConfiguration;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Minigame implements Configurable {

    public abstract String getName();

    public abstract ImmutableList<WinCondition<?>> getWinConditions();

    public abstract ImmutableList<MinigameTeam> getPossibleTeams();

    public abstract Round newRound(RoundConfiguration configuration, List<Player> players);

    @Override
    public String getConfigName() {
        return getName();
    }
}
