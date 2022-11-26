package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.teleport.SignTicker;
import com.azalealibrary.azaleacore.round.RoundTicker;
import com.azalealibrary.azaleacore.util.FileUtil;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
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

    private boolean hasIssuedTask = false;

    public Room(Player owner, String name, Minigame minigame, World world, File map) {
        this.name = name;
        this.minigame = minigame;
        this.world = world;
        this.map = map;
        this.configuration = new RoomConfiguration(owner);
        this.roundTicker = new RoundTicker(this, this.configuration);
        this.broadcaster = new Broadcaster(name, world, AzaleaConfiguration.getInstance().getServerLobby());
    }

    public String getName() {
        return name;
    }

    public <M extends Minigame> M getMinigame() {
        return (M) minigame;
    }

    public World getWorld() {
        return world;
    }

    public File getMap() {
        return map;
    }

    public RoomConfiguration getConfiguration() {
        return configuration;
    }

    public RoundTicker getRoundTicker() {
        return roundTicker;
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public void start(@Nullable Message message) {
        if (roundTicker.isRunning()) {
            throw new AzaleaException("Cannot begin round while round is already running.");
        }

        checkCanStart(); // do all checks before

        delay("Minigame starting in %s...", () -> start(world.getPlayers(), message));
    }

    private void start(List<Player> players, @Nullable Message message) {
        roundTicker.begin(minigame.newRound(configuration, players));

        if (message != null) {
            broadcaster.broadcast(message);
        }
    }

    public void stop(@Nullable Message message) {
        if (!roundTicker.isRunning()) {
            throw new AzaleaException("Cannot end round while round is not running.");
        }

        roundTicker.cancel();

        if (message != null) {
            broadcaster.broadcast(message);
        }
    }

    public void restart(@Nullable Message message) {
        stop(null);
        start(world.getPlayers(), message);
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
            broadcaster.toRoom(new ChatMessage(ChatColor.YELLOW + info));
        }, () -> {
            done.run();
            hasIssuedTask = false;
        });
    }

    private void checkCanStart() {
        checkProperties("room", configuration);
        checkProperties("minigame", minigame);

        if (world.getPlayers().size() < configuration.getMinimumPlayer()) {
            throw new AzaleaException("Not enough players in room to start.");
        }
        if (world.getPlayers().size() > configuration.getMaximumPlayer()) {
            throw new AzaleaException("Too many players in room to start.");
        }
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
