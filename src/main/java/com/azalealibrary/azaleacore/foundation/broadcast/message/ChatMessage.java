package com.azalealibrary.azaleacore.foundation.broadcast.message;

import com.google.common.base.Splitter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ChatMessage extends Message {

    private final LogType logType;

    public ChatMessage(String message, LogType logType) {
        super(message);
        this.logType = logType;
    }

    @Override
    public void post(String name, CommandSender target) {
        String prefix = ChatColor.GRAY + "[" + name + "] " + ChatColor.RESET;
        if (logType != LogType.NONE) {
            prefix += logType.color + logType.prefix + ChatColor.GRAY + " : " + ChatColor.RESET;
        }

        String message = ChatColor.stripColor(getMessage());
        int width = 68 - prefix.length(); // width of output terminal
        List<String> messages = Splitter.fixedLength(width).splitToList(message);

        for (String line : messages) {
            target.sendMessage(prefix + line.trim());
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
