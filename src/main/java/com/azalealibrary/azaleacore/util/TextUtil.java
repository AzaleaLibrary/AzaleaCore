package com.azalealibrary.azaleacore.util;

import com.azalealibrary.azaleacore.api.core.MinigameItem;
import com.azalealibrary.azaleacore.room.Room;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Objects;

public final class TextUtil {

    public static String getName(Player player) {
        ChatColor color = player.isOp() ? ChatColor.LIGHT_PURPLE : ChatColor.YELLOW;
        return color + player.getDisplayName() + ChatColor.RESET;
    }

    public static String getName(Room room) {
        return ChatColor.AQUA + room.getName() + ChatColor.RESET;
    }

    public static String getName(MinigameItem item) {
        String name = Objects.requireNonNull(item.getItemStack().getItemMeta()).getDisplayName();
        return name + ChatColor.RESET;
    }
}
