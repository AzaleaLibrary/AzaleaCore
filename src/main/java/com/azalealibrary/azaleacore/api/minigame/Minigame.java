package com.azalealibrary.azaleacore.api.minigame;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.google.common.collect.ImmutableList;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public abstract class Minigame {

    private final String name;
    private final World world;

    protected Minigame(String name, World world, List<Player> players) {
        this.name = name;
        this.world = world;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public abstract <M extends Minigame> ImmutableList<WinCondition<M>> getWinConditions();

    public abstract <M extends Minigame> Round<M> newRound(List<Player> players);
}
