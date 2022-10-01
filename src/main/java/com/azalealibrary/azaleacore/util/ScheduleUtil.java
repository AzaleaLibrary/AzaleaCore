package com.azalealibrary.azaleacore.util;

import com.azalealibrary.azaleacore.Main;
import org.bukkit.Bukkit;

public final class ScheduleUtil {

    public static void doWhile(int duration, Runnable onInterval, Runnable onDone) {
        doWhile(duration, 1, onInterval, onDone);
    }

    public static void doWhile(int duration, int interval, Runnable onInterval) {
        doWhile(duration, interval, onInterval, () -> {
        });
    }

    public static void doWhile(int duration, int interval, Runnable onInterval, Runnable onDone) {
        int eventId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.INSTANCE, onInterval, 0, interval);
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, () -> {
            onDone.run();
            Bukkit.getScheduler().cancelTask(eventId);
        }, duration);
    }

    public static void doDelayed(int delay, Runnable onDone) {
        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.INSTANCE, onDone, delay);
    }
}