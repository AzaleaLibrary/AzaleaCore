package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.MinigameItem;
import com.azalealibrary.azaleacore.api.MinigameTeam;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.foundation.configuration.property.CollectionProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.foundation.configuration.property.PropertyType;
import com.azalealibrary.azaleacore.foundation.registry.MinigameIdentifier;
import com.azalealibrary.azaleacore.foundation.registry.RegistryEvent;
import com.azalealibrary.azaleacore.round.RoundEvent;
import com.google.common.eventbus.Subscribe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ExampleMinigame {

    // items
    public static final MinigameItem RED_PLAYER_AXE = MinigameItem.create(Material.IRON_AXE, 1).called(ChatColor.RED + "Red Player Axe").addLore(ChatColor.GRAY + "This the Red team's weapon.").build();
    public static final MinigameItem BLUE_PLAYER_SWORD = MinigameItem.create(Material.IRON_SWORD, 1).called(ChatColor.BLUE + "Blue Player Axe").addLore(ChatColor.GRAY + "This the Blue team's weapon.").build();

    // teams
    public static final MinigameTeam RED_TEAM = new MinigameTeam("Red Team", "Kill all blue players.", ChatColor.RED, Sound.ENTITY_VILLAGER_AMBIENT, player -> player.getInventory().addItem(RED_PLAYER_AXE.getItemStack()));
    public static final MinigameTeam BLUE_TEAM = new MinigameTeam("Blue Team", "Kill all red players.", ChatColor.BLUE, Sound.ENTITY_VILLAGER_AMBIENT, player -> player.getInventory().addItem(BLUE_PLAYER_SWORD.getItemStack()));

    // win conditions
    public static final WinCondition NO_BLUE_PLAYERS = new WinCondition(RED_TEAM, "No more blue players.", 123, minigame -> minigame.getTeams().getAllInTeam(BLUE_TEAM).isEmpty());
    public static final WinCondition NO_RED_PLAYERS = new WinCondition(BLUE_TEAM, "No more red players.", 312, minigame -> minigame.getTeams().getAllInTeam(RED_TEAM).isEmpty());

    // properties
    public static final Supplier<Property<Vector>> SPAWN = () -> new Property<>(PropertyType.VECTOR, "spawn", null, true);
    public static final Supplier<CollectionProperty<Vector>> MOB_SPAWNS = () -> new CollectionProperty<>(PropertyType.VECTOR, "mobSpawns", new ArrayList<>(), false);
    public static final Supplier<Property<Integer>> RESPAWN_COUNT = () -> new Property<>(PropertyType.INTEGER, "respawnCount", 4, false);

    public static final MinigameIdentifier EXAMPLE_MINIGAME = new MinigameIdentifier("Example_Minigame");

    @Subscribe
    public void registerMinigames(final RegistryEvent.Minigames event) {
        event.registerMinigame(EXAMPLE_MINIGAME);
    }

    @Subscribe
    public void registerRounds(final RegistryEvent.Rounds event) {
        event.register(EXAMPLE_MINIGAME.tag("round"), () -> new Object() {
            @Subscribe
            public void onStart(RoundEvent.Start event) {
                System.out.println("start");
            }

            @Subscribe
            public void onTick(RoundEvent.Tick event) {
                System.out.println("tick");
            }

            @Subscribe
            public void onWin(RoundEvent.Win event) {
                System.out.println("win");
            }

            @Subscribe
            public void onEnd(RoundEvent.End event) {
                System.out.println("end");
            }
        });
    }

    @Subscribe
    public void registerMinigameItems(final RegistryEvent.Items event) {
        event.register(EXAMPLE_MINIGAME.tag("red_axe"), RED_PLAYER_AXE);
        event.register(EXAMPLE_MINIGAME.tag("blue_sword"), BLUE_PLAYER_SWORD);
    }

    @Subscribe
    public void registerMinigameTeams(final RegistryEvent.Teams event) {
        event.register(EXAMPLE_MINIGAME.tag("red_team"), RED_TEAM);
        event.register(EXAMPLE_MINIGAME.tag("blue_team"), BLUE_TEAM);
    }

    @Subscribe
    public void registerMinigameWinConditions(final RegistryEvent.WinConditions event) {
        event.register(EXAMPLE_MINIGAME.tag("no_blue_players"), NO_BLUE_PLAYERS);
        event.register(EXAMPLE_MINIGAME.tag("no_red_players"), NO_RED_PLAYERS);
    }

    @Subscribe
    public void registerMinigameProperties(final RegistryEvent.Properties event) {
        event.register(EXAMPLE_MINIGAME.tag("spawn"), SPAWN::get);
        event.register(EXAMPLE_MINIGAME.tag("mob_spawns"), MOB_SPAWNS::get);
        event.register(EXAMPLE_MINIGAME.tag("respawn_count"), RESPAWN_COUNT::get);
    }
}