package com.azalealibrary.azaleacore.api;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public abstract class MinigameTeam {

    private final String name;
    private final String objective;
    private final ChatColor color;
    private final Sound sound;

    public MinigameTeam(String name, String objective, ChatColor color, Sound sound) {
        this.name = name;
        this.objective = objective;
        this.color = color;
        this.sound = sound;
    }

    public String getName() {
        return name;
    }

    public String getObjective() {
        return objective;
    }

    public ChatColor getColor() {
        return color;
    }

    public Sound getSound() {
        return sound;
    }

    public abstract void prepare(Player player);
}
