package com.azalealibrary.azaleacore.api.minigame;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.google.common.collect.ImmutableList;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Minigame {

    public abstract String getName();

    public abstract <M extends Minigame> ImmutableList<WinCondition<M>> getWinConditions();

    public abstract <M extends Minigame> Round<M> newRound(List<Player> players);
}
