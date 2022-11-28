package com.azalealibrary.azaleacore.foundation.broadcast;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public final class AzaleaBroadcaster extends Broadcaster {

    private static final AzaleaBroadcaster BROADCASTER = new AzaleaBroadcaster();

    public static AzaleaBroadcaster getInstance() {
        return BROADCASTER;
    }

    private AzaleaBroadcaster() {
        super(AzaleaCore.PLUGIN_ID, null, AzaleaConfiguration.getInstance().getServerLobby());
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
        send(Bukkit.getConsoleSender(), ChatMessage.info(color + message));
    }
}
