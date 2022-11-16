package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.core.MinigameTeam;
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
    private final ImmutableMap<MinigameTeam, List<Player>> originalTeams;
    private final Map<MinigameTeam, List<Player>> teams;

    private RoundTeams(RoundConfiguration configuration, List<Player> players, Map<MinigameTeam, List<Player>> teams) {
        this.configuration = configuration;
        this.players = ImmutableList.copyOf(players);
        this.originalTeams = ImmutableMap.copyOf(teams);
        this.teams = originalTeams;
    }

    public ImmutableList<Player> getPlayers() {
        return players;
    }

    public ImmutableMap<MinigameTeam, List<Player>> getOriginalTeams() {
        return originalTeams;
    }

    public Map<MinigameTeam, List<Player>> getTeams() {
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

    public static RoundTeams generate(RoundConfiguration configuration, List<MinigameTeam> minigameTeams, List<Player> players) {
        Collections.shuffle(players);
        Collections.shuffle(minigameTeams);

        Map<MinigameTeam, List<Player>> originalTeams = new HashMap<>();
        for (List<Player> selection : Lists.partition(players, minigameTeams.size())) {
            originalTeams.put(minigameTeams.remove(0), selection);
        }
        return new RoundTeams(configuration, players, originalTeams);
    }
}
