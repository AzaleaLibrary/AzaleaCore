package com.azalealibrary.azaleacore.foundation.teleport;

import com.azalealibrary.azaleacore.room.Room;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;

public class RoomTeleportSign extends TeleportSign {

    private final Room room;

    public RoomTeleportSign(Location signLocation, Room room) {
        super(signLocation, room.getWorld().getSpawnLocation());
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    @Override
    protected void paint(Sign sign) {
        sign.setGlowingText(false);
        sign.setColor(DyeColor.BLACK);
        sign.setLine(0, "- " + room.getName() + " -");
        sign.setLine(1, ChatColor.ITALIC + room.getMinigame().getName());
        sign.setLine(2, room.getWorld().getPlayers().size() + " / 100"); // TODO - player count
        String running = room.getRoundTicker().isRunning()
                ? ChatColor.RED + "Round ongoing"
                : ChatColor.GREEN + "Round idle";
        sign.setLine(3, "> " + running);
    }
}
