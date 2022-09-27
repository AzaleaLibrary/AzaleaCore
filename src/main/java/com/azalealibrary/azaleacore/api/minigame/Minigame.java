package com.azalealibrary.azaleacore.api.minigame;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.configuration.Configurable;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.google.common.collect.ImmutableList;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Minigame<R extends Round<? extends Minigame<R>>> implements Configurable {

    private final World world;

    protected Minigame(World world) {
        this.world = world;
    }

    public World getWorld() {
        return world;
    }

    @Override
    public abstract String getName();

    public abstract ImmutableList<WinCondition<R>> getWinConditions();

    public abstract R newRound(List<Player> players);
}
