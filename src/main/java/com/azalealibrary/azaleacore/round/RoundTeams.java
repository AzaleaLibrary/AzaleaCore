package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.Team;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class RoundTeams {

    private final RoundConfiguration configuration;
    private final ImmutableList<Player> players;
    private final ImmutableMap<Team, List<Player>> originalTeams;
    private final Map<Team, List<Player>> teams;

    private RoundTeams(RoundConfiguration configuration, List<Player> players, Map<Team, List<Player>> teams) {
        this.configuration = configuration;
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

    public void prepareAll() {
        teams.forEach((team, players) -> {
            for (Player player : players) {
                team.prepare(player);

                if (team.isDisableWhileGrace()) {
                    int duration = (configuration.getGraceTickDuration() + 1) * configuration.getTickRate();
                    player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, duration, 100));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, duration, 10));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, duration, 250));
                }
            }
        });
    }

    public void resetAll() {
        for (Player player : players) {
            player.getActivePotionEffects().forEach(potion -> player.removePotionEffect(potion.getType()));
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            player.setGameMode(GameMode.ADVENTURE); // TODO - add default GM config?
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

    public List<Player> getAllInTeam(Team team) {
        return teams.getOrDefault(team, new ArrayList<>());
    }

    public boolean isInTeam(Player player, Team team) {
        return getAllInTeam(team).contains(player);
    }

    public void switchTeam(Player player, Team team) {
        if (!isInTeam(player, team)) {
            teams.values().forEach(players -> players.remove(player)); // quick (slow) and dirty
            teams.get(team).add(player);
            team.prepare(player);
        }
    }

    public static RoundTeams generate(RoundConfiguration configuration, List<Team> teams, List<Player> players) {
        Collections.shuffle(players);
        Collections.shuffle(teams);

        Map<Team, List<Player>> originalTeams = new HashMap<>();
        for (List<Player> selection : Lists.partition(players, teams.size())) {
            originalTeams.put(teams.remove(0), selection);
        }
        return new RoundTeams(configuration, players, originalTeams);
    }
}
