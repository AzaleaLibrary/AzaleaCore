package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.foundation.Hooks;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.RoomConfiguration;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class RoundTicker implements Runnable {

    private final Room room;
    private final RoomConfiguration configuration;
    private final RoundLifeCycle listener;

    private Round round;
    private Integer eventId;
    private int tick = 0;

    public RoundTicker(Room room, RoomConfiguration configuration, List<RoundLifeCycle> listeners) {
        this.room = room;
        this.configuration = configuration;
        this.listener = new RoundLifeCycle() {
            @Override
            public void onSetup(RoundEvent.Setup event) {
                listeners.forEach(listener -> listener.onSetup(event));
            }

            @Override
            public void onStart(RoundEvent.Start event) {
                listeners.forEach(listener -> listener.onStart(event));
            }

            @Override
            public void onTick(RoundEvent.Tick event) {
                listeners.forEach(listener -> listener.onTick(event));
            }

            @Override
            public void onWin(RoundEvent.Win event) {
                listeners.forEach(listener -> listener.onWin(event));
            }

            @Override
            public void onEnd(RoundEvent.End event) {
                listeners.forEach(listener -> listener.onEnd(event));
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
    }

    @Override
    public void run() {
        try {
            if (tick == 0) {
                setupHook(room, round);
                listener.onSetup(new RoundEvent.Setup(round, getPhase(), tick));
            } else if (tick == configuration.getRoundGracePeriod()) {
                listener.onStart(new RoundEvent.Start(round, getPhase(), tick));
            } else if (tick == configuration.getRoundDurationPeriod() + configuration.getRoundGracePeriod()) {
                dispatchEndEvent(listener::onEnd, new RoundEvent.End(round, getPhase(), tick), false);
            }

            if (!isRunning()) return; // when round ends, sometimes runner hasn't stopped yet.
            RoundEvent.Tick tickEvent = new RoundEvent.Tick(round, getPhase(), tick);
            listener.onTick(tickEvent);

            // only check for win conditions once grace period ended
            if (getPhase() == RoundEvent.Phase.ONGOING) {
                // first check if the tick event has a win condition
                // if tick event has no win condition, check for any other win condition
                Optional.ofNullable(tickEvent.getCondition())
                        .or(() -> room.getMinigame().getWinConditions().stream()
                        .filter(condition -> condition.evaluate(round))
                        .findFirst()).ifPresent(condition -> dispatchEndEvent(
                                listener::onWin,
                                new RoundEvent.Win(condition, round, getPhase(), tick),
                                true
                        ));
            }

            tick++;
        } catch (Exception exception) {
            Bukkit.getScheduler().cancelTask(eventId);
            room.stop(ChatMessage.error(ChatColor.RED + "An error occurred which caused the game to end."));
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
    }

    private <E extends RoundEvent.End> void dispatchEndEvent(Consumer<E> dispatcher, E event, boolean alsoEnd) {
        dispatcher.accept(event);

        if (event.shouldRestart()) {
            tick = configuration.getRoundGracePeriod();
        } else {
            finish();
            endHook(room, round);

            // round end event should ideally always return a win condition
            if (event.getCondition() != null) {
                winHook(room, round, event.getCondition());
            }
            if (alsoEnd) {
                listener.onEnd(event);
            }
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