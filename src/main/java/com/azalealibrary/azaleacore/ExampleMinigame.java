package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.MinigameItem;
import com.azalealibrary.azaleacore.api.MinigameTeam;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.command.GotoCommand;
import com.azalealibrary.azaleacore.foundation.configuration.property.CollectionProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.foundation.configuration.property.PropertyType;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.registry.RegistryEvent;
import com.azalealibrary.azaleacore.minigame.MinigameIdentifier;
import com.azalealibrary.azaleacore.playground.Playground;
import com.azalealibrary.azaleacore.round.RoundEvent;
import com.google.common.eventbus.Subscribe;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ExampleMinigame {

    // items
    public static final MinigameItem<PlayerMoveEvent> AXE = new MinigameItem<>(PlayerMoveEvent.class, Material.IRON_AXE) {
        @Override
        protected ItemStack customize(Builder builder) {
            builder.addLore("example");
            return builder.build();
        }

        @EventHandler
        @Override
        protected void onEvent(PlayerMoveEvent event) {
            super.handleEvent(event);
        }

        @Override
        protected Player getPlayer(PlayerMoveEvent event) {
            return event.getPlayer();
        }

        @Override
        protected void onUse(PlayerMoveEvent event, Player player, Playground playground) {
            System.out.println("???");
        }
    };

    // teams
    public static final MinigameTeam RED_TEAM = new MinigameTeam("Red Team", "Kill all blue players.", ChatColor.RED, Sound.ENTITY_VILLAGER_AMBIENT, player -> player.getInventory().addItem(AXE.getItemStack()));
    public static final MinigameTeam BLUE_TEAM = new MinigameTeam("Blue Team", "Kill all red players.", ChatColor.BLUE, Sound.ENTITY_VILLAGER_AMBIENT, player -> player.getInventory().addItem(AXE.getItemStack()));

    // win conditions
    public static final WinCondition NO_BLUE_PLAYERS = new WinCondition(RED_TEAM, "No more blue players.", 123, minigame -> minigame.getTeams().getAllInTeam(BLUE_TEAM).isEmpty());
    public static final WinCondition NO_RED_PLAYERS = new WinCondition(BLUE_TEAM, "No more red players.", 312, minigame -> minigame.getTeams().getAllInTeam(RED_TEAM).isEmpty());

    // properties
    public static final Supplier<Property<Vector>> SPAWN = () -> new Property<>(PropertyType.VECTOR, "spawn", "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.", null, true);
    public static final Supplier<CollectionProperty<Vector>> MOB_SPAWNS = () -> new CollectionProperty<>(PropertyType.VECTOR, "mobSpawns", "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.", new ArrayList<>(), false);
    public static final Supplier<Property<Integer>> RESPAWN_COUNT = () -> new Property<>(PropertyType.INTEGER, "respawnCount", "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.", 4, false);
    public static final Supplier<CollectionProperty<Integer>> NUMBERS = () -> new CollectionProperty<>(PropertyType.INTEGER, "numbers", "In publishing and graphic design, Lorem ipsum is a placeholder text commonly used to demonstrate the visual form of a document or a typeface without relying on meaningful content.", new ArrayList<>(), false);

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
                event.getRound().getParty().broadcast(ChatMessage.info("start"));
            }

            @Subscribe
            public void onTick(RoundEvent.Tick event) {
                if (event.getTick().isFullSecond()) {
                    event.getRound().getParty().broadcast(ChatMessage.info(String.valueOf(event.getTick().getSeconds())));
                }
            }

            @Subscribe
            public void onWin(RoundEvent.Win event) {
                event.getRound().getParty().broadcast(ChatMessage.info("win"));
            }

            @Subscribe
            public void onEnd(RoundEvent.End event) {
                event.getRound().getParty().broadcast(ChatMessage.info("end"));
            }
        });
    }

    @Subscribe
    public void registerMinigameItems(final RegistryEvent.Items event) {
        event.register(EXAMPLE_MINIGAME.tag("example"), AXE);
    }

    @Subscribe
    public void registerMinigameTeams(final RegistryEvent.Teams event) {
        event.register(EXAMPLE_MINIGAME.tag("red_team"), RED_TEAM);
        event.register(EXAMPLE_MINIGAME.tag("blue_team"), BLUE_TEAM);
    }

    @Subscribe
    public void registerMinigameWinConditions(final RegistryEvent.WinConditions event) {
//        event.register(EXAMPLE_MINIGAME.tag("no_blue_players"), NO_BLUE_PLAYERS);
//        event.register(EXAMPLE_MINIGAME.tag("no_red_players"), NO_RED_PLAYERS);
    }

    @Subscribe
    public void registerMinigameProperties(final RegistryEvent.Properties event) {
        event.register(EXAMPLE_MINIGAME.tag("spawn"), SPAWN::get);
        event.register(EXAMPLE_MINIGAME.tag("mob_spawns"), MOB_SPAWNS::get);
        event.register(EXAMPLE_MINIGAME.tag("respawn_count"), RESPAWN_COUNT::get);
        event.register(EXAMPLE_MINIGAME.tag("numbers"), NUMBERS::get);
    }

    @Subscribe
    public void registerMinigameCommands(final RegistryEvent.Commands event) {
        event.register(EXAMPLE_MINIGAME.tag("goto_command"), new GotoCommand());
    }
}
