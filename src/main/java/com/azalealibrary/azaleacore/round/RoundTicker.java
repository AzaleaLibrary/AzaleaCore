package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.foundation.Hooks;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.RoomConfiguration;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class RoundTicker implements Runnable {

    private final Room room;
    private final RoomConfiguration configuration;
    private final RoundListener listener;

    private Round round;
    private Integer eventId;
    private int tick = 0;

    public RoundTicker(Room room, RoomConfiguration configuration, List<Supplier<RoundListener>> listeners) {
        this.room = room;
        this.configuration = configuration;

        List<RoundListener> newListeners = listeners.stream().map(Supplier::get).toList();
        this.listener = new RoundListener() {
            @Override
            public void onSetup(RoundEvent event) {
                newListeners.forEach(listener -> listener.onSetup(event));
            }

            @Override
            public void onStart(RoundEvent event) {
                newListeners.forEach(listener -> listener.onStart(event));
            }

            @Override
            public void onTick(RoundEvent event) {
                newListeners.forEach(listener -> listener.onTick(event));
            }

            @Override
            public void onWin(RoundEvent event) {
                newListeners.forEach(listener -> listener.onWin(event));
            }

            @Override
            public void onEnd(RoundEvent event) {
                newListeners.forEach(listener -> listener.onEnd(event));
            }
        };
    }

    public boolean isRunning() {
        return eventId != null;
    }

    public Round getRound() {
        return round;
    }

    public int getTick() {
        return tick;
    }

    public RoundEvent.Phase getPhase() {
        return !isRunning() ? RoundEvent.Phase.IDLE : tick < configuration.getRoundGracePeriod()
                ? RoundEvent.Phase.GRACE
                : RoundEvent.Phase.ONGOING;
    }

    public void begin(Round newRound) {
        eventId = ScheduleUtil.doEvery(20 / configuration.getRoundTickRate(), this);
        round = newRound;
        tick = 0;
    }

    public void finish() {
        Bukkit.getScheduler().cancelTask(eventId);
        eventId = null;
        listener.onEnd(new RoundEvent(round, room, getPhase(), tick));
        endHook(room, round);
    }

    @Override
    public void run() {
        try {
            if (tick == 0) {
                setupHook(room, round);
                listener.onSetup(new RoundEvent(round, room, getPhase(), tick));
            } else if (tick == configuration.getRoundGracePeriod()) {
                listener.onStart(new RoundEvent(round, room, getPhase(), tick));
            } else if (tick == configuration.getRoundDurationPeriod() + configuration.getRoundGracePeriod()) {
                finish();
            }

            if (!isRunning()) return; // when round ends, sometimes runner hasn't stopped yet.
            RoundEvent tickEvent = new RoundEvent(round, room, getPhase(), tick);
            listener.onTick(tickEvent);

            // only check for win conditions once grace period ended
            if (getPhase() == RoundEvent.Phase.ONGOING) {
                // first check if the tick event has a win condition
                // if tick event has no win condition, check for any other win condition
                WinCondition condition = Optional.ofNullable(tickEvent.getCondition())
                        .or(() -> room.getMinigame().getWinConditions().stream()
                        .filter(c -> c.evaluate(round))
                        .findFirst()).orElse(null);

                if (condition != null) {
                    winHook(room, round, condition);
                    RoundEvent winEvent = new RoundEvent(round, room, getPhase(), tick);
                    winEvent.setCondition(condition);
                    listener.onWin(winEvent);
                }
            }

            tick++;
        } catch (Exception exception) {
            Bukkit.getScheduler().cancelTask(eventId);
            room.stop(ChatMessage.error("An error occurred which caused the game to end."));
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    // TODO - review
    private static void setupHook(Room room, Round round) {
        room.teleportAllToRoomSpawn();
        round.getTeams().prepareAll();
        Hooks.showStartScreen(round.getTeams(), room.getBroadcaster());
    }

    // TODO - review
    private static void winHook(Room room, Round round, WinCondition condition) {
        Hooks.showWinScreen(round.getTeams(), room.getBroadcaster(), condition);
        Hooks.awardPoints(round.getTeams(), condition);
    }

    // TODO - review
    private static void endHook(Room room, Round round) {
        room.teleportAllToRoomSpawn();
        round.getTeams().resetAll();
    }
}