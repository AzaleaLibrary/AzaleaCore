package com.azalealibrary.azaleacore.teleport;

import org.bukkit.Location;
import org.bukkit.entity.Player;


public interface Teleporter {

    String getName();

    Type getType();

    Location getPosition();

    Location getTo();

    default void teleport(Player player) {
        player.teleport(getTo().clone().add(.5, 0, .5));
    }

    enum Type {
        SIGN, STRUCTURE, PORTAL
    }
}
