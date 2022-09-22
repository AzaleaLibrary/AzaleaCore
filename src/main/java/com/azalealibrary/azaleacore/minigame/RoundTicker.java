package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.api.minigame.round.RoundEvent;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class RoundTicker<M extends Minigame> implements Runnable {

    private final MinigameConfiguration configuration;
    private final M minigame;

    private int graceCountdown = -1;
    private Supplier<Round<M>> roundSupplier;
    private Round<M> round;

    private Integer eventId;

    public RoundTicker(M minigame, MinigameConfiguration configuration) {
        this.minigame = minigame;
        this.configuration = configuration;
    }

    public Round<M> getRound() {
        return round;
    }

    public boolean isRunning() {
        return eventId != null;
    }

    public void begin(Supplier<Round<M>> newRound) {
        eventId = Bukkit.getScheduler().scheduleSyncRepeatingTask(configuration.getPlugin(), this, 0L, configuration.getTickRate());
        roundSupplier = newRound;
        round = newRound.get();
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(eventId);
        eventId = null;
    }

    @Override
    public void run() {
        if (graceCountdown == -1) {
            round.onSetup(new RoundEvent.Setup<>(minigame));
            graceCountdown++;
        } else if (graceCountdown < configuration.getGraceTickDuration()) {
            graceCountdown++;
        } else if (graceCountdown == configuration.getGraceTickDuration()) {
            if (round.getTick() == 0) {
                round.onStart(new RoundEvent.Start<>(minigame));
                round.setTick(round.getTick() + 1);
            } else if (round.getTick() < configuration.getRoundTickDuration()) {
                RoundEvent.Tick<M> tickEvent = new RoundEvent.Tick<>(minigame);
                round.onTick(tickEvent);

                Optional.ofNullable(tickEvent.getCondition())
                        .ifPresent(w -> handleRestart(round::onWin, new RoundEvent.Win<>(minigame, w)));
                minigame.<M>getWinConditions().stream().filter(c -> c.meetsCondition(minigame)).findFirst()
                        .ifPresent(w -> handleRestart(round::onWin, new RoundEvent.Win<>(minigame, w)));

                round.setTick(round.getTick() + 1);
            } else if (round.getTick() == configuration.getRoundTickDuration()) {
                handleRestart(round::onEnd, new RoundEvent.End<>(minigame));
            }
        }
    }

    private <E extends RoundEvent.End<M>> void handleRestart(Consumer<E> dispatcher, E event) {
        dispatcher.accept(event);
        cancel();

        if (event.doRestart()) {
            begin(roundSupplier);
        }
    }
}
