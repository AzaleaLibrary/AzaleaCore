package com.azalealibrary.azaleacore.api.minigame;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.configuration.Configurable;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.minigame.MinigameConfiguration;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Minigame<R extends Round<? extends Minigame<R>>> implements Configurable {

    public abstract String getName();

    public abstract MinigameConfiguration getConfiguration();

    public abstract ImmutableList<WinCondition<R>> getWinConditions();

    public abstract R newRound(List<Player> players);

    @Override
    public String getConfigName() {
        return getName();
    }
}
