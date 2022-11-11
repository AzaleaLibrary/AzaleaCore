package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.configuration.Configurable;
import com.azalealibrary.azaleacore.minigame.MinigameConfiguration;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Minigame implements Configurable {

    public abstract String getName();

    public abstract String getBroadcasterName();

    public abstract MinigameConfiguration getConfiguration();

    public abstract ImmutableList<WinCondition<?>> getWinConditions();

    public abstract ImmutableList<Team> getPossibleTeams();

    public abstract Round<?> newRound(List<Player> players, Broadcaster broadcaster);

    @Override
    public String getConfigName() {
        return getName();
    }
}
