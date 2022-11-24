package com.azalealibrary.azaleacore.foundation.broadcast.message;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class ChatMessage extends Message {

    public ChatMessage(String message) {
        super(message);
    }

    @Override
    public void post(String prefix, CommandSender target) {
        target.sendMessage(ChatColor.GRAY + "[" + prefix + "] " + ChatColor.RESET + getMessage());
    }

    public static ChatMessage info(String message) {
        return new ChatMessage(ChatColor.GRAY + message);
    }

    public static ChatMessage success(String message) {
        return new ChatMessage(ChatColor.GREEN + message);
    }

    public static ChatMessage warn(String message) {
        return new ChatMessage(ChatColor.GOLD + message);
    }

    public static ChatMessage failure(String message) {
        return new ChatMessage(ChatColor.RED + message);
    }
}
