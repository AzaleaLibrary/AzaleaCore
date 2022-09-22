package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

public class MinigameController<M extends Minigame> {

    private final M minigame;
    private final MinigameConfiguration configuration;
    private final RoundTicker<M> ticker;

    public MinigameController(M minigame, MinigameConfiguration configuration) {
        this.minigame = minigame;
        this.configuration = configuration;
        this.ticker = new RoundTicker<>(minigame, configuration);
    }

    public MinigameConfiguration getConfiguration() {
        return configuration;
    }

    public void start(List<Player> players, @Nullable Message message) {
        if (ticker.isRunning()) {
            throw new RuntimeException("Attempting to begin round while round is already running.");
        }

        ticker.begin(() -> minigame.newRound(players));
        ticker.getRound().getBroadcaster().broadcast(message);
    }

    public void stop(@Nullable Message message) {
        if (!ticker.isRunning()) {
            throw new RuntimeException("Attempting to end round while round is not running.");
        }

        ticker.cancel();
        ticker.getRound().getBroadcaster().broadcast(message);
    }

    public void restart(@Nullable Message message) {
        stop(null);
        start(ticker.getRound().getPlayers(), message);
    }
}
