package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.broadcast.MinigameBroadcaster;
import com.azalealibrary.azaleacore.api.broadcast.message.TitleMessage;
import com.azalealibrary.azaleacore.api.configuration.CommandProperty;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.google.common.collect.ImmutableList;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class ExampleMinigame extends Minigame<ExampleRound> {

    private final CommandProperty<Location> spawn;

    public ExampleMinigame(World world) {
        super(world);
        this.spawn = CommandProperty.location("spawn", world.getSpawnLocation()).build();
    }

    public Location getSpawn() {
        return spawn.get();
    }

    @Override
    public String getName() {
        return "ExampleMinigame";
    }

    @Override
    public ImmutableList<WinCondition<ExampleRound>> getWinConditions() {
        return ImmutableList.of(new WinCondition<>(new TitleMessage("No players :("), 123, ExampleRound::isAllPlayersDead));
    }

    @Override
    public ExampleRound newRound(List<Player> players) {
        // TODO - remove players param and systematically use world players?
        return new ExampleRound(players, new MinigameBroadcaster(getName(), players));
    }

    @Override
    public List<CommandProperty<?>> getProperties() {
        return List.of(spawn);
    }
}
