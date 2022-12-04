package com.azalealibrary.azaleacore.foundation.message;

import com.google.common.base.Splitter;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.List;

public class ChatMessage extends Message {

    private final LogType logType;
    private final @Nullable TextComponent textComponent;

    public ChatMessage(String message, LogType logType) {
        super(message);
        this.logType = logType;
        this.textComponent = null;
    }

    public ChatMessage(TextComponent textComponent, LogType logType) {
        super(textComponent.getText());
        this.logType = logType;
        this.textComponent = textComponent;
    }

    @Override
    public void post(String name, CommandSender target) {
        String prefix = ChatColor.GRAY + "[" + name + "] " + ChatColor.RESET;
        if (logType != LogType.NONE) {
            prefix += logType.color + logType.prefix + ChatColor.GRAY + " : " + ChatColor.RESET;
        }

        // in case a clickable TextComponent has been set, send it to the player
        if (textComponent != null && target instanceof Player player) {
            TextComponent withPrefix = new TextComponent(prefix);
            withPrefix.addExtra(textComponent);
            player.spigot().sendMessage(withPrefix);
        } else {
            int width = 70 - prefix.length(); // width of output terminal
            List<String> messages = Splitter.fixedLength(width).splitToList(getMessage());

            for (String line : messages) {
                target.sendMessage(prefix + line.trim());
            }
        }
    }

    public static ChatMessage none(String message) {
        return new ChatMessage(message, LogType.NONE);
    }

    public static ChatMessage info(String message) {
        return new ChatMessage(message, LogType.INFO);
    }

    public static ChatMessage announcement(String message) {
        return new ChatMessage(message, LogType.ANNOUNCEMENT);
    }

    public static ChatMessage important(String message) {
        return new ChatMessage(message, LogType.IMPORTANT);
    }

    public static ChatMessage error(String message) {
        return new ChatMessage(message, LogType.ERROR);
    }

    public static ChatMessage magic(String message) {
        return new ChatMessage(message, LogType.MAGIC);
    }

    public enum LogType {
        NONE("", null),
        INFO("Info", ChatColor.YELLOW),
        ANNOUNCEMENT("Announcement", ChatColor.AQUA),
        IMPORTANT("Important", ChatColor.LIGHT_PURPLE),
        ERROR("Error", ChatColor.RED),
        MAGIC("!@#$%", ChatColor.MAGIC);

        private final String prefix;
        private final ChatColor color;

        LogType(String prefix, ChatColor color) {
            this.prefix = prefix;
            this.color = color;
        }
    }
}
