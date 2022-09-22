package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.broadcast.MinigameBroadcaster;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class MinigameController<M extends Minigame> {

    private final M minigame;
    private final List<Player> participants;
    private final MinigameConfiguration configuration;
    private final MinigameBroadcaster broadcaster;
    private final RoundTicker<M> ticker;

    public MinigameController(M minigame, List<Player> participants, MinigameConfiguration configuration) {
        this.minigame = minigame;
        this.participants = participants;
        this.configuration = configuration;
        this.broadcaster = new MinigameBroadcaster(minigame.getName(), participants);
        this.ticker = new RoundTicker<>(minigame, configuration);
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
        if (ticker != null && ticker.isRunning()) {
            throw new RuntimeException("Attempting to begin round while round is already running.");
        }

        broadcaster.broadcast(message);
        Optional.ofNullable(ticker).ifPresent(ticker -> ticker.begin(minigame.newRound(participants)));
    }

    public void stop(@Nullable Message message) {
        if (ticker != null && !ticker.isRunning()) {
            throw new RuntimeException("Attempting to end round while round is not running.");
        }

        broadcaster.broadcast(message);
        Optional.ofNullable(ticker).ifPresent(RoundTicker::cancel);
    }

    public void restart(@Nullable Message message) {
        stop(null);
        start(message);
    }
}
