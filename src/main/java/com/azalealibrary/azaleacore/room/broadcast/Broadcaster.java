package com.azalealibrary.azaleacore.room.broadcast;

import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Broadcaster {

    private final String prefix;
    private final World playground;
    private final World lobby;

    public Broadcaster(String prefix, World playground, World lobby) {
        this.prefix = prefix;
        this.playground = playground;
        this.lobby = lobby;
    }

    public void broadcast(Message message) {
        broadcast(message, Chanel.BOTH);
    }

    public void toPlayground(Message message) {
        broadcast(message, Chanel.PLAYGROUND);
    }

    public void toLobby(Message message) {
        broadcast(message, Chanel.LOBBY);
    }

    public void toPlayer(Player player, Message message) {
        message.post(prefix, player);
    }

    public void broadcast(Message message, Chanel chanel) {
        switch (chanel) {
            case PLAYGROUND -> playground.getPlayers().forEach(player -> message.post(prefix, player));
            case LOBBY -> lobby.getPlayers().forEach(player -> message.post(prefix, player));
            case BOTH -> {
                toPlayground(message);
                toLobby(message);
            }
        }
    }

    public enum Chanel {
        PLAYGROUND, LOBBY, BOTH
    }

    public enum LogType { // TODO - [EXE] Important : Some message...
        NONE("", ChatColor.WHITE),
        INFO("Info", ChatColor.GRAY),
        ANNOUNCEMENT("Announcement", ChatColor.AQUA),
        IMPORTANT("Important", ChatColor.LIGHT_PURPLE),
        MAGIC("!@#$%", ChatColor.MAGIC);

        private final String prefix;
        private final ChatColor color;

        LogType(String prefix, ChatColor color) {
            this.prefix = prefix;
            this.color = color;
        }
    }
}
