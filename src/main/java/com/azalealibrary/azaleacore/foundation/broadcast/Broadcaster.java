package com.azalealibrary.azaleacore.foundation.broadcast;

import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

public class Broadcaster {

    private final String name;
    private final World room;
    private final World lobby;

    public Broadcaster(String name, World room, World lobby) {
        this.name = name;
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
        message.post(name, sender);
    }

    public void broadcast(Message message, Chanel chanel) {
        switch (chanel) {
            case ROOM -> room.getPlayers().forEach(player -> message.post(name, player));
            case LOBBY -> lobby.getPlayers().forEach(player -> message.post(name, player));
            case BOTH -> {
                toRoom(message);
                toLobby(message);
            }
        }
    }

    public enum Chanel {
        ROOM, LOBBY, BOTH
    }
}
