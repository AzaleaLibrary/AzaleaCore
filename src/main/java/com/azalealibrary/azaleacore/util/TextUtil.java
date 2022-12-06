package com.azalealibrary.azaleacore.util;

import com.azalealibrary.azaleacore.api.MinigameItem;
import com.azalealibrary.azaleacore.foundation.configuration.property.CollectionProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.party.Party;
import com.azalealibrary.azaleacore.playground.Playground;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MinecraftFont;

import java.awt.*;
import java.util.List;
import java.util.*;

public final class TextUtil {

    public static String getName(Player player) {
        ChatColor color = player.isOp() ? ChatColor.LIGHT_PURPLE : ChatColor.YELLOW;
        return color + player.getDisplayName() + ChatColor.RESET;
    }

    public static String getName(Party party) {
        return ChatColor.AQUA + party.getName() + ChatColor.RESET;
    }

    public static String getName(Playground playground) {
        return ChatColor.BLUE + playground.getName() + ChatColor.RESET;
    }

    public static String getName(MinigameItem item) {
        String name = Objects.requireNonNull(item.getItemStack().getItemMeta()).getDisplayName();
        return name + ChatColor.RESET;
    }

    public static String getName(ConfigurableProperty<?> property) {
        return ChatColor.LIGHT_PURPLE + property.getName() + ChatColor.RESET;
    }

    public static String getString(ConfigurableProperty<?> property) {
        StringBuilder builder = new StringBuilder(getName(property) + "=");

        if (property instanceof CollectionProperty<?> list) {
            String raw = list.toString();
            List<String> values = List.of(raw.substring(1, raw.length() - 1).split(","));

            List<String> colored = new ArrayList<>();
            for (String value : values) {
                Color color = new Color((int) (Math.random() * 0x1000000));
                colored.add(net.md_5.bungee.api.ChatColor.of(color) + value + ChatColor.RESET);
            }
            builder.append(colored);
        } else {
            builder.append(ChatColor.GREEN).append(property);
        }
        return builder.append(ChatColor.RESET).toString();
    }

    public static List<String> split(String text, int max) {
        List<String> words = new ArrayList<>(Arrays.stream(text.split("\\s+")).toList());

        String raw = ChatColor.stripColor(text);
        int width = MinecraftFont.Font.getWidth(raw);
        int copies = (int) Math.ceil((float) width / max);
        List<String> lines = new ArrayList<>(Collections.nCopies(copies, ""));

        for (int i = 0; i < lines.size(); i++) {
            while (words.size() > 0 && lines.get(i).length() + words.get(0).length() <= max) {
                lines.add(i, lines.remove(i) + " " + words.remove(0));
            }
        }
        lines.removeIf(l -> l.isEmpty() || l.isBlank());
        return lines;
    }
}
