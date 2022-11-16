package com.azalealibrary.azaleacore.api.core;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class MinigameTeam {

    private final String name;
    private final String objective;
    private final Boolean disableWhileGrace;
    private final ChatColor color;
    private final Sound sound;
    private final Consumer<Player> prepare;

    public MinigameTeam(String name, String objective, Boolean disableWhileGrace, ChatColor color, Sound sound, Consumer<Player> prepare) {
        this.name = name;
        this.objective = objective;
        this.disableWhileGrace = disableWhileGrace;
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

    public Boolean isDisableWhileGrace() {
        return disableWhileGrace;
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
