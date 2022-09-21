package com.azalealibrary.azaleacore.minigame;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public final class MinigameConfiguration {

    private final JavaPlugin plugin;
    private final World world;
    private final int tickRate;
    private final int graceTickDuration;
    private final int roundTickDuration;
    private final boolean showDefaultTimer;

    private MinigameConfiguration(JavaPlugin plugin, World world, int tickRate, int graceTickDuration, int roundTickDuration, boolean showDefaultTimer) {
        this.plugin = plugin;
        this.world = world;
        this.tickRate = tickRate;
        this.graceTickDuration = graceTickDuration;
        this.roundTickDuration = roundTickDuration;
        this.showDefaultTimer = showDefaultTimer;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public World getWorld() {
        return world;
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

    public boolean showDefaultTimer() {
        return showDefaultTimer;
    }

    public static Builder create(JavaPlugin plugin, World world) {
        return new Builder(plugin, world);
    }

    public static class Builder  {

        private final JavaPlugin plugin;
        private final World world;
        private int tickRate = 1;           // 1hz
        private int graceTickDuration = 10; // 10s
        private int roundTickDuration = 30; // 30s
        private boolean showDefaultTimer = true;

        private Builder(JavaPlugin plugin, World world) {
            this.plugin = plugin;
            this.world = world;
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

        public Builder hideDefaultTimer() {
            showDefaultTimer = false;
            return this;
        }

        public MinigameConfiguration build() {
            return new MinigameConfiguration(plugin, world, tickRate, graceTickDuration, roundTickDuration, showDefaultTimer);
        }
    }
}
