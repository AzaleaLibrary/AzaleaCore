package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.event.MinigameEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Note;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Player;

import java.util.List;

public class MinigameTicker<M extends Minigame> implements Runnable {

    private static final int ROUND_END_COUNTDOWN = 10;

    private final MinigameConfiguration configuration;
    private final MinigameEvent<M> event;

    private Integer eventId;
    private boolean hasStarted = false;

    public MinigameTicker(M minigame, List<Player> participants, MinigameConfiguration configuration) {
        this.configuration = configuration;
        this.event = new MinigameEvent<>(minigame, participants, Bukkit.createBossBar(null, BarColor.BLUE, BarStyle.SEGMENTED_10));
    }

    public boolean isRunning() {
        return eventId != null;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public boolean isGracePeriod() {
        return isRunning() && !hasStarted();
    }

    public void start() {
        eventId = Bukkit.getScheduler().scheduleSyncRepeatingTask(configuration.getPlugin(), this, 0L, configuration.getTickRate());
        event.getParticipants().forEach(player -> event.getTimer().addPlayer(player));
        event.getMinigame().onGrace(event);
    }

    public void stop() {
        Bukkit.getScheduler().cancelTask(eventId);
        eventId = null;
        event.getTimer().removeAll();
        event.getMinigame().onEnd(event);
    }

    @Override
    public void run() {
        if (isGracePeriod()) {
            whileGrace();
        } else {
            whileRound();
        }

        nextTick();
    }

    private void nextTick() {
        event.setTick(event.getTick() + 1);
    }

    private void whileGrace() {
        if (configuration.showDefaultTimer()) {
            int countdown = configuration.getGraceTickDuration() - event.getTick();
            String time = String.format("%1dm, %1ds", (countdown % 3600) / 60, (countdown % 60));
            String title = ChatColor.AQUA + "Grace Period | " + time + " seconds left";
            updateTimer(event.getTick(), configuration.getGraceTickDuration(), title);

            if (countdown < 3) {
                playToAll(Instrument.XYLOPHONE);
            }
        }

        if (event.getTick() == configuration.getGraceTickDuration()) {
            hasStarted = true;
            event.setTick(0); // reset timer for round
            event.getMinigame().onStart(event);
        }
    }

    private void whileRound() {
        int countdown = configuration.getRoundTickDuration() - event.getTick();
        int untilEnd = ROUND_END_COUNTDOWN * configuration.getTickRate();

        if (configuration.showDefaultTimer()) {
            ChatColor color = countdown < untilEnd ? ChatColor.RED : ChatColor.WHITE;
            ChatColor sec = countdown < untilEnd ? ChatColor.RED : ChatColor.YELLOW;
            String time = String.format("%1dm, %1ds", (countdown % 3600) / 60, (countdown % 60));
            String title = color + "Round | " + sec + time + color + " seconds left";
            updateTimer(event.getTick(), configuration.getRoundTickDuration(), title);
            event.getTimer().setColor(BarColor.WHITE);
        }

        if (countdown < untilEnd) {
            event.getTimer().setColor(BarColor.RED);
            playToAll(Instrument.IRON_XYLOPHONE);
        }

        event.getMinigame().onTick(event);

        // TODO - win condition check
        if (event.getWinCondition() != null) {
            event.getMinigame().onWin(event);
            stop();
        }

        if (event.getTick() == configuration.getRoundTickDuration()) {
            stop();
        }
    }

    private void updateTimer(int countdown, int duration, String title) {
        event.getTimer().setProgress((float) countdown / duration);
        event.getTimer().setTitle(title);
    }

    private void playToAll(Instrument instrument) {
        for (Player player : event.getParticipants()) {
            Note note = Note.natural(1, Note.Tone.A);
            player.playNote(player.getLocation(), instrument, note);
        }
    }
}
