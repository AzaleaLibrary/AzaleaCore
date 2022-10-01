package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.broadcast.MinigameBroadcaster;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.io.File;
import java.util.List;

public class MinigameRoom<M extends Minigame<? extends Round<M>>, R extends Round<M>> {

    private final String name;
    private final World world;
    private final World lobby;
    private final M minigame;
    private final RoundTicker<M, R> ticker;
    private MinigameBroadcaster broadcaster;

    public MinigameRoom(String name, World world, World lobby, M minigame) {
        this.name = name;
        this.world = world;
        this.lobby = lobby;
        this.minigame = minigame;
        this.ticker = new RoundTicker<>(minigame, minigame.getConfiguration());
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

    public MinigameBroadcaster getBroadcaster() {
        return broadcaster;
    }

    public void start(List<Player> players, @Nullable Message message) {
        if (ticker.isRunning()) {
            throw new RuntimeException("Attempting to begin round while round is already running.");
        }

        // TODO - remove players param and systematically use world players?
        ticker.begin((R) minigame.newRound(players));
        broadcaster = new MinigameBroadcaster(minigame.getName(), players);

        if (message != null) {
            getBroadcaster().broadcast(message);
        }
    }

    public void stop(@Nullable Message message) {
        if (!ticker.isRunning()) {
            throw new RuntimeException("Attempting to end round while round is not running.");
        }

        ticker.cancel();

        if (message != null) {
            getBroadcaster().broadcast(message);
        }
    }

    public void restart(@Nullable Message message) {
        // stop any running minigame and ignore exceptions
        // TODO - create MinigameException/AzaleaException exceptions
        try { stop(null); } catch (Exception ignored) { }

        if (ticker.getRound() == null) {
            throw new RuntimeException("Cannot restart a minigame that is not running.");
        }

        start(ticker.getRound().getPlayers(), message);
    }

    public void terminate() {
        if (ticker.isRunning()) {
            stop(null);
        }

        for (Player player : getWorld().getPlayers()) {
            player.teleport(getLobby().getSpawnLocation());
        }

        AzaleaApi.getInstance().getMinigameRooms().remove(this);
        Bukkit.unloadWorld(getWorld(), false);
        FileUtil.deleteDirectory(new File(Bukkit.getWorldContainer(), "rooms/" + getWorld().getName()));
    }
}
