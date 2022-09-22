package com.azalealibrary.azaleacore.api.minigame;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.google.common.collect.ImmutableList;

public abstract class Minigame {

    public abstract String getName();

    public abstract <M extends Minigame> Round<M> newRound();

    public abstract <M extends Minigame> ImmutableList<WinCondition<M>> getWinConditions();
}
