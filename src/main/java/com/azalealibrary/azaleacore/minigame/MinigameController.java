package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.api.broadcast.MinigameBroadcaster;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class MinigameController<M extends Minigame> {

    private final MinigameTicker<M> ticker;
    private final MinigameConfiguration configuration;
    private final MinigameBroadcaster broadcaster;
    private final List<Player> participants;

    public MinigameController(M minigame, List<Player> participants, MinigameConfiguration configuration) {
        this.broadcaster = new MinigameBroadcaster(minigame.getName(), participants);
        this.ticker = new MinigameTicker<>(minigame, participants, configuration);
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

    public void startMinigame(Message message) {
        if (ticker.isRunning()) {
            throw new RuntimeException("Attempting to end minigame while minigame is not running.");
        }

        ticker.start();
    }

    public void stopMinigame(Message message) {
        if (!ticker.isRunning()) {
            throw new RuntimeException("Attempting to start minigame while minigame is already running.");
        }

        ticker.stop();
    }
}
