package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.api.core.MinigameItem;
import com.azalealibrary.azaleacore.api.core.MinigameTeam;
import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.foundation.configuration.property.ListProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.foundation.configuration.property.VectorListProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.VectorProperty;
import com.azalealibrary.azaleacore.room.RoomConfiguration;
import com.azalealibrary.azaleacore.round.RoundTeams;
import com.google.common.collect.ImmutableList;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExampleMinigame extends Minigame {

    // items
    public static final MinigameItem RED_PLAYER_AXE = MinigameItem.create(Material.IRON_AXE, 1)
            .called(ChatColor.DARK_RED + "Red Player Axe")
            .addLore(ChatColor.GRAY + "This the Red team's weapon.")
            .build();
    public static final MinigameItem BLUE_PLAYER_SWORD = MinigameItem.create(Material.IRON_SWORD, 1)
            .called(ChatColor.DARK_BLUE + "Blue Player Axe")
            .addLore(ChatColor.GRAY + "This the Blue team's weapon.")
            .build();

    // teams
    public static final MinigameTeam RED_TEAM = new MinigameTeam("Red Team", "Kill all blue players.", true, ChatColor.RED, Sound.ENTITY_VILLAGER_AMBIENT, player -> {
        player.setHealth(1);
        player.getInventory().addItem(RED_PLAYER_AXE.getItemStack());
    });
    public static final MinigameTeam BLUE_TEAM = new MinigameTeam("Blue Team", "Kill all red players.", false, ChatColor.BLUE, Sound.ENTITY_VILLAGER_AMBIENT, player -> {
        player.setHealth(1);
        player.getInventory().addItem(BLUE_PLAYER_SWORD.getItemStack());
    });

    // win conditions
    public static final WinCondition<ExampleRound> NO_BLUE_PLAYERS = new WinCondition<>(RED_TEAM, "No more blue players.", 123, round -> {
        return round.getRoundTeams().getAllInTeam(BLUE_TEAM).isEmpty();
    });
    public static final WinCondition<ExampleRound> NO_RED_PLAYERS = new WinCondition<>(BLUE_TEAM, "No more red players.", 312, round -> {
        return round.getRoundTeams().getAllInTeam(RED_TEAM).isEmpty();
    });

    private final VectorProperty spawn = new VectorProperty("spawn", null, true);
    private final VectorListProperty spawns = new VectorListProperty("spawns", new ArrayList<>(), false);
    private final ListProperty<Integer> counts = new ListProperty<>("counts", new ArrayList<>(), false);

    public Location getSpawn(World world) {
        return spawn.get().toLocation(world); // example
    }

    @Override
    public String getName() {
        return "ExampleMinigame";
    }

    @Override
    public ImmutableList<WinCondition<?>> getWinConditions() {
        return ImmutableList.of(NO_RED_PLAYERS, NO_BLUE_PLAYERS);
    }

    @Override
    public ImmutableList<MinigameTeam> getPossibleTeams() {
        return ImmutableList.of(RED_TEAM, BLUE_TEAM);
    }

    @Override
    public ExampleRound newRound(RoomConfiguration configuration, List<Player> players) {
        return new ExampleRound(RoundTeams.generate(configuration, new ArrayList<>(getPossibleTeams()), players));
    }

    @Override
    public List<Property<?>> getProperties() {
        return List.of(spawn, spawns, counts);
    }
}
