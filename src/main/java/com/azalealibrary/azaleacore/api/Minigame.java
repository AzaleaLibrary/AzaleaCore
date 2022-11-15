package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.configuration.Configurable;
import com.azalealibrary.azaleacore.minigame.round.RoundConfiguration;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Minigame implements Configurable {

    public abstract String getName();

    public abstract ImmutableList<WinCondition<?>> getWinConditions();

    public abstract ImmutableList<Team> getPossibleTeams();

    public abstract Round newRound(RoundConfiguration configuration, List<Player> players);

    @Override
    public String getConfigName() {
        return getName();
    }
}
