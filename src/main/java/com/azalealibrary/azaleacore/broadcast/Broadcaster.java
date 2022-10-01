package com.azalealibrary.azaleacore.broadcast;

import com.azalealibrary.azaleacore.broadcast.message.Message;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class Broadcaster {

    private final String prefix;
    private final World world;

    public Broadcaster(String prefix, World world) {
        this.prefix = prefix;
        this.world = world;
    }

    public void broadcast(Message message) {
        world.getPlayers().forEach(player -> message.post(prefix, player));
    }

    public void broadcast(Message message, Player player) {
        if (world.getPlayers().contains(player)) {
            message.post(prefix, player);
        }
    }
}
