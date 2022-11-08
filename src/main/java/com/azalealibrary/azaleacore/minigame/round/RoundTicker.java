package com.azalealibrary.azaleacore.minigame.round;

import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.api.Round;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.minigame.MinigameConfiguration;
import com.azalealibrary.azaleacore.minigame.MinigameRoom;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.function.Consumer;

public class RoundTicker<M extends Minigame<?>, R extends Round<M>> implements Runnable {

    private final MinigameConfiguration configuration;
    private final MinigameRoom<M, R> room;

    private R round;
    private int graceCountdown = -1;
    private Integer eventId;

    public RoundTicker(MinigameRoom<M, R> room, MinigameConfiguration configuration) {
        this.room = room;
        this.configuration = configuration;
    }

    public R getRound() {
        return round;
    }

    public boolean isRunning() {
        return eventId != null;
    }

    public void begin(R newRound) {
        room.getWorld().getPlayers().forEach(player -> player.teleport(room.getWorld().getSpawnLocation()));
        eventId = Bukkit.getScheduler().scheduleSyncRepeatingTask(configuration.getPlugin(), this, 0L, configuration.getTickRate());
        round = newRound;
        graceCountdown = -1;
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(eventId);
        eventId = null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void run() {
        if (room.getWorld().getPlayers().size() != round.getPlayers().size()) {
            return; // wait until everyone is in the world
        }

        if (graceCountdown == -1) {
            round.onSetup(new RoundEvent.Setup<>(room.getMinigame()));
            graceCountdown++;
        } else if (graceCountdown < configuration.getGraceTickDuration()) {
            graceCountdown++;
        } else if (graceCountdown == configuration.getGraceTickDuration()) {
            if (round.getTick() == 0) {
                round.onStart(new RoundEvent.Start<>(room.getMinigame()));
                round.setTick(round.getTick() + 1);
            } else if (round.getTick() < configuration.getRoundTickDuration()) {
                RoundEvent.Tick<M> tickEvent = new RoundEvent.Tick<>(room.getMinigame());
                round.onTick(tickEvent);
                round.setTick(round.getTick() + 1);

                Optional.ofNullable(tickEvent.getCondition())
                        .ifPresent(w -> handleRestart(round::onWin, new RoundEvent.Win<>(room.getMinigame(), w)));
                room.getMinigame().getWinConditions().stream()
                        .filter(c -> ((WinCondition<R>) c).evaluate(round))
                        .findFirst()
                        .ifPresent(w -> handleRestart(round::onWin, new RoundEvent.Win<>(room.getMinigame(), w)));
            } else if (round.getTick() == configuration.getRoundTickDuration()) {
                handleRestart(round::onEnd, new RoundEvent.End<>(room.getMinigame()));
            }
        }
    }

    private <E extends RoundEvent.End<M>> void handleRestart(Consumer<E> dispatcher, E event) {
        dispatcher.accept(event);

        if (event.doRestart()) {
            round.setTick(0);
        } else {
            cancel();
            System.err.println("DONW");
        }
    }
}
