package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.MinigameTeam;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.*;

public class RoundTeams {

    private final List<Player> players;
    private final Map<MinigameTeam, List<Player>> originalTeams;
    private final Map<MinigameTeam, List<Player>> teams;

    private RoundTeams(List<Player> players, Map<MinigameTeam, List<Player>> teams) {
        this.players = players;
        this.originalTeams = teams;
        this.teams = teams;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public ImmutableMap<MinigameTeam, List<Player>> getOriginalTeams() {
        return ImmutableMap.copyOf(originalTeams);
    }

    public ImmutableMap<MinigameTeam, List<Player>> getTeams() {
        return ImmutableMap.copyOf(teams);
    }

    public void prepareAll() {
        teams.forEach((team, players) -> players.forEach(team::prepare));
    }

    public void resetAll() {
        for (Player player : players) {
            player.getActivePotionEffects().forEach(potion -> player.removePotionEffect(potion.getType()));
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
//            player.setGameMode(GameMode.ADVENTURE); // TODO - add default GM config?
            player.getInventory().clear();
            player.setCollidable(true);
            player.setInvisible(false);
            player.setGlowing(false);
            player.setFoodLevel(20);
            player.setHealth(20);
            player.setLevel(0);
            player.setExp(0);
        }
    }

    public MinigameTeam getTeam(Player player) {
        return teams.entrySet().stream()
                .filter(entry -> entry.getValue().contains(player))
                .findFirst()
                .map(Map.Entry::getKey).orElse(null);
    }

    public List<Player> getAllInTeam(MinigameTeam minigameTeam) {
        return teams.getOrDefault(minigameTeam, new ArrayList<>());
    }

    public boolean isInTeam(Player player, MinigameTeam minigameTeam) {
        return getAllInTeam(minigameTeam).contains(player);
    }

    public void switchTeam(Player player, MinigameTeam minigameTeam) {
        if (!isInTeam(player, minigameTeam)) {
            teams.values().forEach(players -> players.remove(player)); // quick (slow) and dirty
            teams.get(minigameTeam).add(player);
            minigameTeam.prepare(player);
        }
    }

    public void removePlayer(Player player) {
        MinigameTeam team = getTeam(player);

        if (team != null) {
            players.remove(player);
            List<Player> players = teams.get(team);
            players.remove(player);
            teams.put(team, players);
        }
    }

    public static RoundTeams create(List<MinigameTeam> teams, List<Player> players) {
        Collections.shuffle(players);
        Collections.shuffle(teams);

        Map<MinigameTeam, List<Player>> originalTeams = new HashMap<>();
        for (List<Player> selection : Lists.partition(players, teams.size())) {
            originalTeams.put(teams.remove(0), selection);
        }

        originalTeams.forEach((team, members) -> members.forEach(team::prepare));
        return new RoundTeams(players, originalTeams);
    }
}
