package com.azalealibrary.azaleacore.api.core;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class MinigameTeam {

    private final String name;
    private final String objective;
    private final ChatColor color;
    private final Sound sound;
    private final Consumer<Player> prepare;

    public MinigameTeam(String name, String objective, ChatColor color, Sound sound, Consumer<Player> prepare) {
        this.name = name;
        this.objective = objective;
        this.color = color;
        this.sound = sound;
        this.prepare = prepare;
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

    public void prepare(Player player) {
        prepare.accept(player);
    }
}
