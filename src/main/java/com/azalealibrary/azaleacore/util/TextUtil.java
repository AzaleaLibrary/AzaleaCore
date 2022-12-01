package com.azalealibrary.azaleacore.util;

import com.azalealibrary.azaleacore.api.core.MinigameItem;
import com.azalealibrary.azaleacore.room.Room;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;
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

    public static List<String> colorize(List<String> elements) {
        return elements.stream().map(element -> net.md_5.bungee.api.ChatColor.of(new Color((int) (Math.random() * 0x1000000))) + element + ChatColor.RESET).toList();
    }
}
