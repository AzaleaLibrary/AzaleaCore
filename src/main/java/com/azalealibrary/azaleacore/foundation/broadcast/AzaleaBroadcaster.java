package com.azalealibrary.azaleacore.foundation.broadcast;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class AzaleaBroadcaster extends Broadcaster {

    public AzaleaBroadcaster() {
        super(AzaleaCore.PLUGIN_ID, null, null);
    }

    @Override
    public void broadcast(Message message, Chanel chanel) {
        if (chanel != Chanel.LOBBY) {
            throw new UnsupportedOperationException("Azalea broadcaster can only broadcast to the lobby or a player.");
        }
        super.broadcast(message, chanel);
    }

    public void debug(String message) {
        log(ChatColor.GRAY, message);
    }

    public void warn(String message) {
        log(ChatColor.GOLD, message);
    }

    public void error(String message) {
        log(ChatColor.RED, message);
    }

    private void log(ChatColor color, String message) {
        send(Bukkit.getConsoleSender(), new ChatMessage(color + message));
    }
}
