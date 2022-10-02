package com.azalealibrary.azaleacore.broadcast;

import com.azalealibrary.azaleacore.broadcast.message.Message;
import org.bukkit.entity.Player;

import java.util.List;

public class Broadcaster {

    private final String prefix;
    private final List<Player> players;

    public Broadcaster(String prefix, List<Player> players) {
        this.prefix = prefix;
        this.players = players;
    }

    public void broadcast(Message message) {
        players.forEach(player -> message.post(prefix, player));
    }

    public void broadcast(Message message, Player player) {
        if (players.contains(player)) {
            message.post(prefix, player);
        }
    }
}
