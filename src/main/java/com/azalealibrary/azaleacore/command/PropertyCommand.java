package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import javax.annotation.Nonnull;
import java.util.List;

@Commands(@Command(name = AzaleaCommand.COMMAND_PREFIX + "property"))
public class PropertyCommand extends AzaleaCommand {

    @Override
    protected Message execute(@Nonnull CommandSender sender, List<String> params) {
        return new ChatMessage(params.toString());
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() < 2) {
            return AzaleaApi.MINIGAMES.values().stream().map(controller -> controller.getMinigame().getName()).toList();
        }
        return List.of("N/A");
    }
}
