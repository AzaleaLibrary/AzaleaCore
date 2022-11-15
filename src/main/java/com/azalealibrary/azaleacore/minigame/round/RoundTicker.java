package com.azalealibrary.azaleacore.minigame.round;

import com.azalealibrary.azaleacore.api.Round;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.room.MinigameRoom;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.function.Consumer;

public class RoundTicker {

    private final MinigameRoom room;
    private final RoundConfiguration configuration;

    private Round round;
    private Integer graceEventId;
    private Integer roundEventId;

    public RoundTicker(MinigameRoom room, RoundConfiguration configuration) {
        this.room = room;
        this.configuration = configuration;
    }

    public boolean isRunning() {
        return graceEventId != null || roundEventId != null;
    }

    public void begin(Round round) {
        this.round = round;

        /* SETUP */ round.onSetup(new RoundEvent.Setup(room));
        graceEventId = ScheduleUtil.doDelayed(configuration.getGraceTickDuration() * configuration.getTickRate(), () -> {
            /* START */ round.onStart(new RoundEvent.Start(room));
            roundEventId = ScheduleUtil.doWhile(configuration.getRoundTickDuration(), configuration.getTickRate(), () -> {
                RoundEvent.Tick tickEvent = new RoundEvent.Tick(room);
                /* TICK */ round.onTick(tickEvent);
                round.setTick(round.getTick() + 1);

                /* WIN */ Optional.ofNullable(tickEvent.getCondition())
                        .ifPresent(w -> handleShouldRestart(round::onWin, new RoundEvent.Win(w, room), true));
                /* WIN */ room.getMinigame().getWinConditions().stream()
                        .filter(c -> ((WinCondition<Round>) c).evaluate(round))
                        .findFirst()
                        .ifPresent(w -> handleShouldRestart(round::onWin, new RoundEvent.Win(w, room), true));
            }, /* END */ this::cancel);
        });
    }

    public void cancel() {
        handleShouldRestart(round::onEnd, new RoundEvent.End(room), false);

        if (graceEventId != null && Bukkit.getScheduler().isCurrentlyRunning(graceEventId)) {
            Bukkit.getScheduler().cancelTask(graceEventId);
            graceEventId = null;
        }
        if (roundEventId != null && Bukkit.getScheduler().isCurrentlyRunning(roundEventId)) {
            Bukkit.getScheduler().cancelTask(roundEventId);
            roundEventId = null;
        }
    }

    private <E extends RoundEvent.End> void handleShouldRestart(Consumer<E> dispatcher, E event, boolean doCancel) {
        if (isRunning()) {
            dispatcher.accept(event);

            if (event.doRestart()) {
                round.setTick(0);
            } else if (doCancel) {
                cancel();
            }
        }
    }
}
