package com.azalealibrary.azaleacore.minigame.round;

import com.azalealibrary.azaleacore.api.Team;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.bukkit.entity.Player;

import java.util.*;

public class RoundTeams {

    private final ImmutableList<Player> players;
    private final ImmutableMap<Team, List<Player>> originalTeams;
    private final Map<Team, List<Player>> teams;

    private RoundTeams(List<Player> players, Map<Team, List<Player>> teams) {
        this.players = ImmutableList.copyOf(players);
        this.originalTeams = ImmutableMap.copyOf(teams);
        this.teams = originalTeams;
    }

    public ImmutableList<Player> getPlayers() {
        return players;
    }

    public ImmutableMap<Team, List<Player>> getOriginalTeams() {
        return originalTeams;
    }

    public Map<Team, List<Player>> getTeams() {
        return teams;
    }

    public boolean isInTeam(Player player, Team team) {
        return getTeams().getOrDefault(team, new ArrayList<>()).contains(player);
    }

    public void switchTeam(Player player, Team team) {
        teams.values().forEach(players -> players.remove(player)); // quick and dirty
        teams.get(team).add(player);
        team.prepare(player);
    }

    public static RoundTeams generate(List<Player> players, List<Team> teams) {
        Collections.shuffle(players);
        Collections.shuffle(teams);

        Map<Team, List<Player>> originalTeams = new HashMap<>();
        for (List<Player> selection : Lists.partition(players, teams.size())) {
            originalTeams.put(teams.remove(0), selection);
        }
        return new RoundTeams(players, originalTeams);
    }
}
