package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.serialization.Serializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class MinigameController<M extends Minigame<? extends Round<M>>, R extends Round<M>> implements Serializable {

    private final M minigame;
    private final RoundTicker<M, R> ticker;

    public MinigameController(M minigame) {
        this.minigame = minigame;
        this.ticker = new RoundTicker<>(minigame, minigame.getConfiguration());
    }

    public String getName() {
        return minigame.getWorld().getName() + ":" + minigame.getConfigName();
    }

    public M getMinigame() {
        return minigame;
    }

    public void start(List<Player> players, @Nullable Message message) {
        if (ticker.isRunning()) {
            throw new RuntimeException("Attempting to begin round while round is already running.");
        }

        ticker.begin((R) minigame.newRound(players));

        if (message != null) {
            ticker.getRound().getBroadcaster().broadcast(message);
        }
    }

    public void stop(@Nullable Message message) {
        if (!ticker.isRunning()) {
            throw new RuntimeException("Attempting to end round while round is not running.");
        }

        ticker.cancel();

        if (message != null) {
            ticker.getRound().getBroadcaster().broadcast(message);
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

    @Override
    public String getConfigName() {
        return minigame.getConfigName() + ":" + minigame.getWorld().getName();
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {

    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {

    }
}
