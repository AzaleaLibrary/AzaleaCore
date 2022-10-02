package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.api.Round;
import com.azalealibrary.azaleacore.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.broadcast.message.Message;
import com.azalealibrary.azaleacore.minigame.round.RoundTicker;
import com.azalealibrary.azaleacore.util.FileUtil;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class MinigameRoom<M extends Minigame<? extends Round<M>>, R extends Round<M>> {

    private final String name;
    private final World world;
    private final World lobby;
    private final M minigame;
    private final RoundTicker<M, R> ticker;
    private Broadcaster broadcaster;

    public MinigameRoom(String name, World world, World lobby, M minigame) {
        this.name = name;
        this.world = world;
        this.lobby = lobby;
        this.minigame = minigame;
        this.ticker = new RoundTicker<>(this, minigame.getConfiguration());
        this.broadcaster = new Broadcaster(minigame.getName(), world.getPlayers());
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

    public M getMinigame() {
        return minigame;
    }

    public void start(@Nullable Message message) {
        broadcaster = new Broadcaster(minigame.getName(), lobby.getPlayers());
        delay(ChatColor.YELLOW + "Minigame starting in %s...", () -> start(lobby.getPlayers(), message));
    }

    private void start(List<Player> players, @Nullable Message message) {
        if (ticker.isRunning()) {
            throw new RuntimeException("Attempting to begin round while round is already running.");
        }

        ticker.begin((R) minigame.newRound(players, broadcaster));

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
        start(ticker.getRound().getPlayers(), message);
    }

    public void terminate(@Nullable Message message) {
        if (ticker.isRunning()) {
            stop(message);
        }

        delay(ChatColor.YELLOW + "Terminating room in %s...", () -> {
            world.getPlayers().forEach(p -> p.teleport(lobby.getSpawnLocation()));
            AzaleaApi.getInstance().getMinigameRooms().remove(this);
            Bukkit.unloadWorld(world, false);
            FileUtil.delete(FileUtil.room(name));
        });
    }

    private void delay(String message, Runnable done) {
        AtomicInteger countdown = new AtomicInteger(3);

        ScheduleUtil.doWhile(countdown.get() * 20, 20, () -> {
            Message info = new ChatMessage(String.format(message, countdown.decrementAndGet() + 1));
            broadcaster.broadcast(info);
        }, done);
    }
}
