package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.api.MinigameProperty;
import com.azalealibrary.azaleacore.api.Team;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.minigame.round.RoundConfiguration;
import com.azalealibrary.azaleacore.minigame.round.RoundTeams;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExampleMinigame extends Minigame {

    public static final Team RED_TEAM = new Team("Red Team", player -> player.setHealth(1), "Kill all blue players.", true, ChatColor.RED, Sound.ENTITY_VILLAGER_AMBIENT);
    public static final Team BLUE_TEAM = new Team("Blue Team", player -> player.setHealth(1), "Kill all red players.", false, ChatColor.BLUE, Sound.ENTITY_VILLAGER_AMBIENT);

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
    public String getBroadcasterName() {
        return "EM";
    }

    @Override
    public ImmutableList<WinCondition<?>> getWinConditions() {
        return ImmutableList.of(
                new WinCondition<ExampleRound>(RED_TEAM, "No more blue players.", 123, round -> round.getRoundTeams().getAllInTeam(BLUE_TEAM).isEmpty()),
                new WinCondition<ExampleRound>(BLUE_TEAM, "No more red players.", 123, round -> round.getRoundTeams().getAllInTeam(RED_TEAM).isEmpty())
        );
    }

    @Override
    public ImmutableList<Team> getPossibleTeams() {
        return ImmutableList.of(RED_TEAM, BLUE_TEAM);
    }

    @Override
    public ExampleRound newRound(RoundConfiguration configuration, List<Player> players) {
        return new ExampleRound(RoundTeams.generate(configuration, new ArrayList<>(getPossibleTeams()), players));
    }

    @Override
    public List<MinigameProperty<?>> getProperties() {
        return List.of(spawn);
    }
}
