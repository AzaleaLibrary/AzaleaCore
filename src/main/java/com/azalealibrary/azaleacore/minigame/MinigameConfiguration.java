package com.azalealibrary.azaleacore.minigame;

import org.bukkit.plugin.java.JavaPlugin;

public final class MinigameConfiguration {

    private final JavaPlugin plugin;
    private final int tickRate;
    private final int graceTickDuration;
    private final int roundTickDuration;

    private MinigameConfiguration(JavaPlugin plugin, int tickRate, int graceTickDuration, int roundTickDuration) {
        this.plugin = plugin;
        this.tickRate = tickRate;
        this.graceTickDuration = graceTickDuration;
        this.roundTickDuration = roundTickDuration;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public int getTickRate() {
        return tickRate;
    }

    public int getGraceTickDuration() {
        return graceTickDuration;
    }

    public int getRoundTickDuration() {
        return roundTickDuration;
    }

    public static Builder create(JavaPlugin plugin) {
        return new Builder(plugin);
    }

    public static class Builder {

        private final JavaPlugin plugin;
        private int tickRate = 1;           // 1hz
        private int graceTickDuration = 10; // 10s
        private int roundTickDuration = 30; // 30s

        private Builder(JavaPlugin plugin) {
            this.plugin = plugin;
        }

        public Builder tickRate(int rate) {
            tickRate = 20 / rate;
            return this;
        }

        public Builder graceDuration(int ticks) {
            graceTickDuration = ticks;
            return this;
        }

        public Builder roundDuration(int ticks) {
            roundTickDuration = ticks;
            return this;
        }

        public MinigameConfiguration build() {
            return new MinigameConfiguration(plugin, tickRate, graceTickDuration, roundTickDuration);
        }
    }
}
