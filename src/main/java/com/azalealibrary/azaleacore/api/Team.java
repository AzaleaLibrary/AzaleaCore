package com.azalealibrary.azaleacore.api;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class Team {

    private final String name;
    private final Consumer<Player> prepare;
    private final String objective;
    private final Boolean disableWhileGrace;
    private final ChatColor color;
    private final Sound sound;

    public Team(String name, Consumer<Player> prepare, String objective, Boolean disableWhileGrace, ChatColor color, Sound sound) {
        this.name = name;
        this.prepare = prepare;
        this.objective = objective;
        this.disableWhileGrace = disableWhileGrace;
        this.color = color;
        this.sound = sound;
    }

    public String getName() {
        return name;
    }

    public void prepare(Player player) {
        prepare.accept(player);
    }

    public String getObjective() {
        return objective;
    }

    public Boolean isDisableWhileGrace() {
        return disableWhileGrace;
    }

    public ChatColor getColor() {
        return color;
    }

    public Sound getSound() {
        return sound;
    }
}
