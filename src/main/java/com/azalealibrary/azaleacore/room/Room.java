package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.room.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import com.azalealibrary.azaleacore.round.RoundConfiguration;
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
import java.util.concurrent.atomic.AtomicInteger;

public class Room {

    private final String name;
    private final Minigame minigame;
    private final World world;

    private final File template;

    private final RoundTicker roundTicker;
    private final SignTicker signTicker;
    private final Broadcaster broadcaster;
    private final RoundConfiguration configuration;

    private boolean hasIssuedTask = false;

    public Room(String name, Minigame minigame, World world, File template) {
        this.name = name;
        this.minigame = minigame;
        this.world = world;
        this.template = template;

        this.configuration = RoundConfiguration.create(AzaleaCore.INSTANCE) // TODO - review
                .graceDuration(3)
                .roundDuration(30)
                .tickRate(1)
                .build();
        this.roundTicker = new RoundTicker(this, this.configuration);
        this.signTicker = new SignTicker(this);
        this.broadcaster = new Broadcaster(name, world, AzaleaCore.getLobby());
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

    public File getTemplate() {
        return template;
    }

    public RoundTicker getRoundTicker() {
        return roundTicker;
    }

    public SignTicker getSignTicker() {
        return signTicker;
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public void start(@Nullable Message message) {
        if (roundTicker.isRunning()) {
            throw new AzaleaException("Cannot begin round while round is already running.");
        }

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
        world.getPlayers().forEach(p -> p.teleport(AzaleaCore.getLobby().getSpawnLocation()));
    }

    public void teleportAllToWorld() {
        world.getPlayers().forEach(p -> p.teleport(world.getSpawnLocation()));
    }

    public void terminate(@Nullable Message message) {
        if (roundTicker.isRunning()) {
            stop(message);
        }

        delay("Terminating room in %s...", () -> {
            teleportAllToLobby();
            signTicker.discardAll();
            AzaleaRoomApi.getInstance().remove(this);
            Bukkit.unloadWorld(world, true);
            FileUtil.delete(FileUtil.room(name));
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
            broadcaster.toPlayground(new ChatMessage(ChatColor.YELLOW + info));
        }, () -> {
            done.run();
            hasIssuedTask = false;
        });
    }
}
