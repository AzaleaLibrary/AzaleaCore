package com.azalealibrary.azaleacore.foundation.broadcast.message;

import org.bukkit.command.CommandSender;

public abstract class Message {

    private final String message;

    public Message(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public abstract void post(String prefix, CommandSender target);
}
