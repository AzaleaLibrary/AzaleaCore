package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.Main;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.broadcast.message.TitleMessage;
import com.azalealibrary.azaleacore.api.configuration.MinigameProperty;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.minigame.MinigameConfiguration;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class ExampleMinigame extends Minigame<ExampleRound> {

    private final MinigameProperty<Location> spawn;

    public ExampleMinigame(World world) {
        this.spawn = MinigameProperty.location("spawn", world.getSpawnLocation()).build();
    }

    public Location getSpawn() {
        return spawn.get();
    }

    @Override
    public String getName() {
        return "ExampleMinigame";
    }

    @Override
    public MinigameConfiguration getConfiguration() {
        return MinigameConfiguration.create(Main.INSTANCE).build();
    }

    @Override
    public ImmutableList<WinCondition<ExampleRound>> getWinConditions() {
        return ImmutableList.of(new WinCondition<>(new TitleMessage("No players :("), 123, ExampleRound::isAllPlayersDead));
    }

    @Override
    public ExampleRound newRound(List<Player> players) {
        // TODO - remove players param and systematically use world players?
        return new ExampleRound(players);
    }

    @Override
    public List<MinigameProperty<?>> getProperties() {
        return List.of(spawn);
    }
}
