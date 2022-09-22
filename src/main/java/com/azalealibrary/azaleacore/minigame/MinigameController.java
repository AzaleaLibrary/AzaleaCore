package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.broadcast.MinigameBroadcaster;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

public class MinigameController<M extends Minigame> {

    private final RoundTicker<M> ticker;
    private final MinigameConfiguration configuration;
    private final MinigameBroadcaster broadcaster;
    private final List<Player> participants;

    public MinigameController(M minigame, List<Player> participants, MinigameConfiguration configuration) {
        this.broadcaster = new MinigameBroadcaster(minigame.getName(), participants);
        this.ticker = new RoundTicker<>(minigame, configuration);
        this.configuration = configuration;
        this.participants = participants;
    }

    public MinigameConfiguration getConfiguration() {
        return configuration;
    }

    public MinigameBroadcaster getBroadcaster() {
        return broadcaster;
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public void start(@Nullable Message message) {
        if (ticker.isRunning()) {
            throw new RuntimeException("Attempting to end round while round is not running.");
        }

        broadcaster.broadcast(message);
        ticker.begin();
    }

    public void stop(@Nullable Message message) {
        if (!ticker.isRunning()) {
            throw new RuntimeException("Attempting to begin round while round is already running.");
        }

        broadcaster.broadcast(message);
        ticker.cancel();
    }

    public void restart(@Nullable Message message) {
        stop(null);
        start(message);
    }
}
