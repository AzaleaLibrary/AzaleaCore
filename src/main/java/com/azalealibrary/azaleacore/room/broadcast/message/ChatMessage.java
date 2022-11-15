package com.azalealibrary.azaleacore.room.broadcast.message;

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
}
