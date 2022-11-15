package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.Main;
import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.room.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import com.azalealibrary.azaleacore.round.RoundConfiguration;
import com.azalealibrary.azaleacore.round.RoundTicker;
import com.azalealibrary.azaleacore.util.FileUtil;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MinigameRoom {


    private final String name;
    private final World world;
    private final World lobby;
    private final Minigame minigame;
    private final RoundTicker ticker;
    private final Broadcaster broadcaster;
    private final RoundConfiguration configuration;
    private final List<Location> signs = new ArrayList<>();

    private boolean hasIssuedTask = false;

    public MinigameRoom(String name, World world, World lobby, Minigame minigame) {
        this.name = name;
        this.world = world;
        this.lobby = lobby;
        this.minigame = minigame;
        this.configuration = RoundConfiguration.create(Main.INSTANCE) // TODO - review
                .graceDuration(3)
                .roundDuration(30)
                .tickRate(1)
                .build();
        this.ticker = new RoundTicker(this, this.configuration);
        this.broadcaster = new Broadcaster(name, world, lobby);

        ScheduleUtil.doFor(20, () -> { // TODO - review
            for (Location location : signs) {
                BlockState state = world.getBlockAt(location).getState();

                if (state instanceof Sign sign) {
                    sign.setEditable(false);

                    sign.setLine(0, "- " + name + " -");
                    sign.setLine(1, ChatColor.ITALIC + minigame.getName());
                    ChatColor color = ticker.isRunning() ? ChatColor.RED : ChatColor.GREEN;
                    String running = ticker.isRunning() ? "Round ongoing" : "Round idle";
                    sign.setLine(2, "> " + color + running);
                    sign.setLine(3, world.getPlayers().size() + " / 100");

                    sign.update();
                }
            }
        });
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public World getLobby() {
        return lobby;
    }

    public <M extends Minigame> M getMinigame() {
        return (M) minigame;
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public List<Location> getSigns() {
        return signs;
    }

    public void start(@Nullable Message message) {
        if (ticker.isRunning()) {
            throw new RuntimeException("Attempting to begin round while round is already running.");
        }

        delay("Minigame starting in %s...", () -> start(world.getPlayers(), message));
    }

    private void start(List<Player> players, @Nullable Message message) {
        ticker.begin(minigame.newRound(configuration, players));

        if (message != null) {
            broadcaster.broadcast(message);
        }
    }

    public void stop(@Nullable Message message) {
        if (!ticker.isRunning()) {
            throw new RuntimeException("Attempting to end round while round is not running.");
        }

        ticker.cancel();

        if (message != null) {
            broadcaster.broadcast(message);
        }
    }

    public void restart(@Nullable Message message) {
        stop(null);
        start(world.getPlayers(), message);
    }

    public void teleportToLobby() {
        Location location = lobby.getSpawnLocation().clone().add(0.5, 0, 0.5);
        lobby.getPlayers().forEach(p -> p.teleport(location));
        world.getPlayers().forEach(p -> p.teleport(location));
    }

    public void teleportToWorld() {
        Location location = world.getSpawnLocation().clone().add(0.5, 0, 0.5);
        lobby.getPlayers().forEach(p -> p.teleport(location));
        world.getPlayers().forEach(p -> p.teleport(location));
    }

    public void terminate(@Nullable Message message) {
        if (ticker.isRunning()) {
            stop(message);
        }

        delay("Terminating room in %s...", () -> {
            teleportToLobby();
            AzaleaApi.getInstance().getRooms().remove(this);
            Bukkit.unloadWorld(world, false);
            FileUtil.delete(FileUtil.room(name));
        });
    }

    private void delay(String message, Runnable done) {
        if (hasIssuedTask) {
            throw new RuntimeException("Command already under way.");
        }

        hasIssuedTask = true;
        AtomicInteger countdown = new AtomicInteger(3);
        ScheduleUtil.doWhile(countdown.get() * 20, 20, () -> {
            String info = String.format(message, countdown.decrementAndGet() + 1);
            broadcaster.broadcast(new ChatMessage(ChatColor.YELLOW + info));
        }, () -> {
            done.run();
            hasIssuedTask = false;
        });
    }
}
