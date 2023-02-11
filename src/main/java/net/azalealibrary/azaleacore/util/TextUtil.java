package net.azalealibrary.azaleacore.util;

import net.azalealibrary.azaleacore.api.MinigameItem;
import net.azalealibrary.azaleacore.foundation.AzaleaException;
import net.azalealibrary.azaleacore.foundation.configuration.property.CollectionProperty;
import net.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import net.azalealibrary.azaleacore.party.Party;
import net.azalealibrary.azaleacore.playground.Playground;
import net.azalealibrary.azaleacore.teleport.Teleporter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.map.MinecraftFont;

import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

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

    public static String getName(Teleporter teleporter) {
        return ChatColor.DARK_PURPLE + teleporter.getName() + ChatColor.RESET;
    }

    public static String getName(MinigameItem<?> item) {
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

        lines = lines.stream().map(String::trim).collect(Collectors.toList());
        lines.removeIf(l -> l.isEmpty() || l.isBlank());
        return lines;
    }

    public static String ensureSimple(String text, String thing) {
        if (!text.matches("^[a-zA-Z0-9-_]*$")) {
            throw new AzaleaException("Invalid " + thing + " provided '" + text + "'. Must be alphanumeric.");
        }
        return text;
    }
}
