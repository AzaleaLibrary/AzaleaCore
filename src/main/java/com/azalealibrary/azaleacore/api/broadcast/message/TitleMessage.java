package com.azalealibrary.azaleacore.api.broadcast.message;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TitleMessage extends Message {

    private final int fadein;
    private final int duration;
    private final int fadeout;
    private final String subtitle;

    public TitleMessage(String title) {
        this(title, "");
    }

    public TitleMessage(String title, String subtitle) {
        this(5, 60, 15, title, subtitle);
    }

    public TitleMessage(int fadein, int duration, int fadeout, String title, String subtitle) {
        super(title);
        this.fadein = fadein;
        this.duration = duration;
        this.fadeout = fadeout;
        this.subtitle = subtitle;
    }

    @Override
    public void post(String prefix, CommandSender target) {
        if (target instanceof Player player) {
            player.sendTitle(getMessage(), subtitle, fadein, duration, fadeout);
        }
    }
}
