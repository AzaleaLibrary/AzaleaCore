package net.azalealibrary.core.foundation.message;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ActionMessage extends Message {

    public ActionMessage(String message) {
        super(message);
    }

    @Override
    public void post(String name, CommandSender target) {
        if (target instanceof Player player) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(getMessage()));
        }
    }
}
