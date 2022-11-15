package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;

import java.util.ArrayList;
import java.util.List;

public class SignTicker {

    private final MinigameRoom room;
    private final List<Location> signs = new ArrayList<>();

    public SignTicker(MinigameRoom room) {
        this.room = room;
        ScheduleUtil.doFor(20, this::updateSigns);
    }

    public List<Location> getSigns() {
        return signs;
    }

    private void updateSigns() {
        for (int i = 0; i < signs.size(); i++) {
            Location location = signs.get(i);
            BlockState state = room.getWorld().getBlockAt(location).getState();

            if (state instanceof Sign sign) {
                updateSign(sign);
            } else {
                signs.remove(location);
            }
        }
    }

    private void updateSign(Sign sign) {
        sign.setLine(0, "- " + room.getName() + " -");
        sign.setLine(1, ChatColor.ITALIC + room.getMinigame().getName());
        sign.setLine(2, room.getWorld().getPlayers().size() + " / 100");

        ChatColor color = room.getRoundTicker().isRunning() ? ChatColor.RED : ChatColor.GREEN;
        String running = room.getRoundTicker().isRunning() ? "Round ongoing" : "Round idle";
        sign.setLine(3, "> " + color + running);

        sign.setEditable(false);
        sign.update();
    }
}
