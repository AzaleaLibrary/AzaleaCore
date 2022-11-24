package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.api.core.Round;
import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.RoomConfiguration;
import org.bukkit.Bukkit;

import java.util.Optional;
import java.util.function.Consumer;

public class RoundTicker implements Runnable {

    private final Room room;
    private final RoomConfiguration configuration;

    private Round round;
    private Integer eventId;
    private int graceCountdown = -1;

    public RoundTicker(Room room, RoomConfiguration configuration) {
        this.room = room;
        this.configuration = configuration;
    }

    public boolean isRunning() {
        return eventId != null;
    }

    public void begin(Round newRound) {
        eventId = Bukkit.getScheduler().scheduleSyncRepeatingTask(AzaleaCore.INSTANCE, this, 0L, 20 / configuration.getRoundTickRate());
        round = newRound;
        graceCountdown = -1;
    }

    public void cancel() {
        Bukkit.getScheduler().cancelTask(eventId);
        eventId = null;
    }

    @Override
    public void run() {
        if (graceCountdown == -1) {
            round.onSetup(new RoundEvent.Setup(room));
            graceCountdown++;
        } else if (graceCountdown < configuration.getRoundGracePeriod()) {
            graceCountdown++;
        } else if (graceCountdown == configuration.getRoundGracePeriod()) {
            if (round.getTick() == 0) {
                round.onStart(new RoundEvent.Start(room));
                round.setTick(round.getTick() + 1);
            } else if (round.getTick() < configuration.getRoundDurationPeriod()) {
                RoundEvent.Tick tickEvent = new RoundEvent.Tick(room);
                round.onTick(tickEvent);
                round.setTick(round.getTick() + 1);

                Optional.ofNullable(tickEvent.getCondition())
                        .ifPresent(w -> handleWinCondition(round::onWin, new RoundEvent.Win(w, room)));
                room.getMinigame().getWinConditions().stream()
                        .filter(c -> ((WinCondition<Round>) c).evaluate(round))
                        .findFirst()
                        .ifPresent(w -> handleWinCondition(round::onWin, new RoundEvent.Win(w, room)));
            } else if (round.getTick() == configuration.getRoundDurationPeriod()) {
                handleWinCondition(round::onEnd, new RoundEvent.End(room));
            }
        }
    }

    private <E extends RoundEvent.End> void handleWinCondition(Consumer<E> dispatcher, E event) {
        dispatcher.accept(event);

        if (event.shouldRestart()) {
            round.setTick(0);
        } else {
            cancel();

            if (!(dispatcher instanceof RoundEvent.End)) {
                round.onEnd(event); // also call RoundLifeCycle#onEnd when #onWin is called
            }
        }
    }
}