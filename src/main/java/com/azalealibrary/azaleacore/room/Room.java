package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.teleport.SignTicker;
import com.azalealibrary.azaleacore.minigame.Minigame;
import com.azalealibrary.azaleacore.round.Round;
import com.azalealibrary.azaleacore.round.RoundTeams;
import com.azalealibrary.azaleacore.round.RoundTicker;
import com.azalealibrary.azaleacore.util.FileUtil;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class Room {

    private final String name;
    private final Minigame minigame;
    private final World world;
    private final File map;

    private final RoundTicker roundTicker;
    private final Broadcaster broadcaster;
    private final RoomConfiguration configuration;
    private final List<Player> invitations;

    private boolean hasIssuedTask = false;

    public Room(Player owner, String name, Minigame minigame, World world, File map) {
        this.name = name;
        this.minigame = minigame;
        this.world = world;
        this.map = map;
        this.configuration = new RoomConfiguration(owner);
        this.roundTicker = new RoundTicker(this, configuration, minigame.getListeners());
        this.broadcaster = new Broadcaster(name, world, AzaleaConfiguration.getInstance().getServerLobby());
        this.invitations = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public Minigame getMinigame() {
        return minigame;
    }

    public World getWorld() {
        return world;
    }

    public File getMap() {
        return map;
    }

    public RoundTicker getRoundTicker() {
        return roundTicker;
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public RoomConfiguration getConfiguration() {
        return configuration;
    }

    public List<Player> getInvitations() {
        return invitations;
    }

    public void addPlayer(Player player) {
        if (world.getPlayers().contains(player)) {
            throw new AzaleaException("Already in room.");
        } else if (world.getPlayers().size() >= configuration.getMaximumPlayer()) {
            throw new AzaleaException("Room is currently full.");
        } else if (configuration.joinWithInvitation() && !invitations.contains(player)) {
            throw new AzaleaException("Sorry, you have not been invited :(");
        }

        broadcaster.toRoom(ChatMessage.announcement(TextUtil.getName(player) + " has joined the room."));
        invitations.remove(player); // TODO - remove player from whitelist?
        player.teleport(world.getSpawnLocation().clone().add(.5, 0, .5));

        if (roundTicker.isRunning()) {
            player.setGameMode(GameMode.SPECTATOR);
            broadcaster.send(player, ChatMessage.info("A round is currently running. You are now a spectator."));
        }
    }

    public void removePlayer(Player player) {
        player.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation());
        broadcaster.toRoom(ChatMessage.announcement(TextUtil.getName(player) + " has left the room."));

        if (roundTicker.isRunning()) {
            roundTicker.getRound().getTeams().removePlayer(player);
        }
    }

    public void start(@Nullable Message message) {
        if (roundTicker.isRunning()) {
            throw new AzaleaException("Cannot begin round while round is already running.");
        }
        checkCanStart(); // do all checks before

        if (message != null) {
            broadcaster.broadcast(message);
        }

        delay("Minigame starting in %s...", () -> start(world.getPlayers()));
    }

    private void start(List<Player> players) {
        RoundTeams teams = RoundTeams.generate(minigame.getPossibleTeams(), players);
        roundTicker.begin(new Round(world, broadcaster, teams, minigame.getProperties()));
    }

    public void stop(@Nullable Message message) {
        if (!roundTicker.isRunning()) {
            throw new AzaleaException("Cannot end round while round is not running.");
        }

        roundTicker.finish();

        if (message != null) {
            broadcaster.broadcast(message);
        }
    }

    public void restart(@Nullable Message message) {
        stop(message);
        start(world.getPlayers());
    }

    public void teleportAllToLobby() {
        world.getPlayers().forEach(p -> p.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation().clone().add(.5, .5, .5)));
    }

    public void teleportAllToRoomSpawn() {
        world.getPlayers().forEach(p -> p.teleport(world.getSpawnLocation().clone().add(.5, .5, .5)));
    }

    public void terminate(@Nullable Message message) {
        if (roundTicker.isRunning()) {
            stop(message);
        }

        delay("Terminating room in %s...", () -> {
            teleportAllToLobby();
            SignTicker.getInstance().removeAll(this);
            AzaleaRoomApi.getInstance().remove(this);
            Bukkit.unloadWorld(world, true);
            FileUtil.delete(Objects.requireNonNull(FileUtil.room(name)));
        });
    }

    private void delay(String message, Runnable done) {
        if (hasIssuedTask) {
            throw new AzaleaException("Command already under way.");
        }

        hasIssuedTask = true;
        AtomicInteger countdown = new AtomicInteger(3);
        ScheduleUtil.doWhile(countdown.get() * 20, 20, () -> {
            String info = String.format(message, countdown.decrementAndGet() + 1);
            broadcaster.toRoom(ChatMessage.announcement(info));
        }, () -> {
            done.run();
            hasIssuedTask = false;
        });
    }

    private void checkCanStart() {
        checkProperties("room", configuration);
        checkProperties("minigame", minigame);

        // TODO - enable
//        if (world.getPlayers().size() < configuration.getMinimumPlayer()) {
//            throw new AzaleaException("Not enough players in room to start.");
//        }
//        if (world.getPlayers().size() > configuration.getMaximumPlayer()) {
//            throw new AzaleaException("Too many players in room to start.");
//        }
    }

    private void checkProperties(String type, Configurable configurable) {
        String[] properties = configurable.getProperties().stream()
                .filter(property -> property.isRequired() & !property.isSet())
                .map(property -> "> " + ChatColor.ITALIC + property.getName())
                .toArray(String[]::new);

        if (properties.length > 0) {
            throw new AzaleaException("Some required " + type + " properties are missing:", properties);
        }
    }
}
