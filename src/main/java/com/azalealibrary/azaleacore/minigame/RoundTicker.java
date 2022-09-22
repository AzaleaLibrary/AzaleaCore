package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.api.minigame.round.RoundEvent;
import org.bukkit.Bukkit;

import java.util.Optional;

public class RoundTicker<M extends Minigame> implements Runnable {

    private final MinigameConfiguration configuration;
    private final M minigame;

    private Round<M> round;
    private Integer eventId;

    public RoundTicker(M minigame, MinigameConfiguration configuration) {
        this.minigame = minigame;
        this.configuration = configuration;
    }

    public boolean isRunning() {
        return eventId != null;
    }

    public void begin() {
        eventId = Bukkit.getScheduler().scheduleSyncRepeatingTask(configuration.getPlugin(), this, 0L, configuration.getTickRate());
        round = minigame.newRound();
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(eventId);
        eventId = null;
    }

    @Override
    public void run() {
        if (round.getTick() == 0) {
            round.onGrace(new RoundEvent.Start<>(minigame));
        } else if (round.getTick() == configuration.getGraceTickDuration()) {
            round.onStart(new RoundEvent.Start<>(minigame));
        } else if (round.getTick() > configuration.getGraceTickDuration()) {
            RoundEvent.Tick<M> tickEvent = new RoundEvent.Tick<>(minigame);
            round.onTick(tickEvent);

            // check for manual win conditions
            if (tickEvent.getCondition() != null) {
                round.onWin(new RoundEvent.Win<>(minigame, tickEvent.getCondition()));
                cancel();
            }

            // check for runtime win conditions
            Optional<WinCondition<M>> winCondition = minigame.<M>getWinConditions().stream()
                    .filter(condition -> condition.meetsCondition(minigame)).findFirst();

            if (winCondition.isPresent()) {
                round.onWin(new RoundEvent.Win<>(minigame, winCondition.get()));
                cancel();
            }

            if (round.getTick() == configuration.getRoundTickDuration() + configuration.getGraceTickDuration()) {
                RoundEvent.End<M> endEvent = new RoundEvent.End<>(minigame);
                round.onEnd(endEvent);
                cancel();

                if (endEvent.doRestart()) {
                    begin();
                }
            }
        }

        round.setTick(round.getTick() + 1);
    }
}
