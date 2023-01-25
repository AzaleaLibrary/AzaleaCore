package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.api.MinigameItem;
import com.azalealibrary.azaleacore.api.MinigameTeam;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.command.GotoCommand;
import com.azalealibrary.azaleacore.example.items.BluePlayerSword;
import com.azalealibrary.azaleacore.example.items.RedPlayerAxe;
import com.azalealibrary.azaleacore.example.teams.BlueTeam;
import com.azalealibrary.azaleacore.example.teams.RedTeam;
import com.azalealibrary.azaleacore.example.winconditions.NoBluePlayers;
import com.azalealibrary.azaleacore.example.winconditions.NoRedPlayers;
import com.azalealibrary.azaleacore.foundation.configuration.property.CollectionProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.foundation.configuration.property.PropertyType;
import com.azalealibrary.azaleacore.foundation.registry.RegistryEvent;
import com.azalealibrary.azaleacore.minigame.MinigameIdentifier;
import com.google.common.eventbus.Subscribe;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.function.Supplier;

public final class Registry {

    public static final MinigameIdentifier MINIGAME = new MinigameIdentifier("Example_Minigame");

    // items
    public static final MinigameItem<?> RED_PLAYER_AXE = new RedPlayerAxe();
    public static final MinigameItem<?> BLUE_PLAYER_SWORD = new BluePlayerSword();

    // teams
    public static final MinigameTeam RED_TEAM = new RedTeam();
    public static final MinigameTeam BLUE_TEAM = new BlueTeam();

    // win conditions
    public static final WinCondition NO_BLUE_PLAYERS = new NoBluePlayers();
    public static final WinCondition NO_RED_PLAYERS = new NoRedPlayers();

    // properties
    public static final Supplier<Property<Vector>> SPAWN = () -> new Property<>(PropertyType.VECTOR, "spawn", "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.", null, true);
    public static final Supplier<CollectionProperty<Vector>> MOB_SPAWNS = () -> new CollectionProperty<>(PropertyType.VECTOR, "mobSpawns", "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.", new ArrayList<>(), false);
    public static final Supplier<Property<Integer>> RESPAWN_COUNT = () -> new Property<>(PropertyType.INTEGER, "respawnCount", "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.", 4, false);
    public static final Supplier<CollectionProperty<Integer>> NUMBERS = () -> new CollectionProperty<>(PropertyType.INTEGER, "numbers", "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.", new ArrayList<>(), false);

    @Subscribe
    public void registerMinigames(final RegistryEvent.Minigames event) {
        event.registerMinigame(MINIGAME);
    }

    @Subscribe
    public void registerRounds(final RegistryEvent.Rounds event) {
        event.register(MINIGAME.tag("round"), Round::new);
    }

    @Subscribe
    public void registerItems(final RegistryEvent.Items event) {
        event.register(MINIGAME.tag("red_player_axe"), RED_PLAYER_AXE);
        event.register(MINIGAME.tag("blue_player_sword"), BLUE_PLAYER_SWORD);
    }

    @Subscribe
    public void registerTeams(final RegistryEvent.Teams event) {
        event.register(MINIGAME.tag("red_team"), RED_TEAM);
        event.register(MINIGAME.tag("blue_team"), BLUE_TEAM);
    }

    @Subscribe
    public void registerWinConditions(final RegistryEvent.WinConditions event) {
        event.register(MINIGAME.tag("no_blue_players"), NO_BLUE_PLAYERS);
        event.register(MINIGAME.tag("no_red_players"), NO_RED_PLAYERS);
    }

    @Subscribe
    public void registerProperties(final RegistryEvent.Properties event) {
        event.register(MINIGAME.tag("spawn"), SPAWN::get);
        event.register(MINIGAME.tag("mob_spawns"), MOB_SPAWNS::get);
        event.register(MINIGAME.tag("respawn_count"), RESPAWN_COUNT::get);
        event.register(MINIGAME.tag("numbers"), NUMBERS::get);
    }

    @Subscribe
    public void registerCommands(final RegistryEvent.Commands event) {
        event.register(MINIGAME.tag("goto_command"), new GotoCommand());
    }
}
