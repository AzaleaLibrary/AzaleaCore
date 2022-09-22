package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.api.minigame.round.RoundEvent;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.function.Consumer;

public class RoundTicker<M extends Minigame> implements Runnable {

    private final MinigameConfiguration configuration;
    private final M minigame;

    private Round<M> round;
    private Integer eventId;
    private int graceCountdown = -1;

    public RoundTicker(M minigame, MinigameConfiguration configuration) {
        this.minigame = minigame;
        this.configuration = configuration;
    }

    public boolean isRunning() {
        return eventId != null;
    }

    public <R extends Round<M>> void begin(R newRound) {
        eventId = Bukkit.getScheduler().scheduleSyncRepeatingTask(configuration.getPlugin(), this, 0L, configuration.getTickRate());
        round = newRound;
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(eventId);
        eventId = null;
    }

    @Override
    public void run() {
        if (graceCountdown != configuration.getGraceTickDuration()) {
            graceCountdown++;

            if (graceCountdown == 0) {
                round.onSetup(new RoundEvent.Setup<>(minigame));
            } else if (graceCountdown == configuration.getGraceTickDuration()) {
                round.onStart(new RoundEvent.Start<>(minigame));
            }
        } else {
            round.setTick(round.getTick() + 1);

            RoundEvent.Tick<M> tickEvent = new RoundEvent.Tick<>(minigame);
            round.onTick(tickEvent);

            Optional.ofNullable(tickEvent.getCondition()).ifPresent(w -> handleRestart(round::onWin, new RoundEvent.Win<>(minigame, w)));
            minigame.<M>getWinConditions().stream().filter(c -> c.meetsCondition(minigame)).findFirst()
                    .ifPresent(w -> handleRestart(round::onWin, new RoundEvent.Win<>(minigame, w)));

            if (round.getTick() == configuration.getRoundTickDuration()) {
                handleRestart(round::onEnd, new RoundEvent.End<>(minigame));
            }
        }
    }

    private <E extends RoundEvent.End<M>> void handleRestart(Consumer<E> dispatcher, E event) {
        dispatcher.accept(event);
        if (event.doRestart()) begin(round); else cancel();
    }
}
