package com.azalealibrary.azaleacore.foundation.broadcast;

import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class Broadcaster {

    private final String prefix;
    private final World room;
    private final World lobby;

    public Broadcaster(String prefix, World room, World lobby) {
        this.prefix = prefix;
        this.room = room;
        this.lobby = lobby;
    }

    public void broadcast(Message message) {
        broadcast(message, Chanel.BOTH);
    }

    public void toRoom(Message message) {
        broadcast(message, Chanel.ROOM);
    }

    public void toLobby(Message message) {
        broadcast(message, Chanel.LOBBY);
    }

    public void send(CommandSender sender, Message message) {
        message.post(prefix, sender);
    }

    public void broadcast(Message message, Chanel chanel) {
        switch (chanel) {
            case ROOM -> room.getPlayers().forEach(player -> message.post(prefix, player));
            case LOBBY -> lobby.getPlayers().forEach(player -> message.post(prefix, player));
            case BOTH -> {
                toRoom(message);
                toLobby(message);
            }
        }
    }

    public enum Chanel {
        ROOM, LOBBY, BOTH
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
