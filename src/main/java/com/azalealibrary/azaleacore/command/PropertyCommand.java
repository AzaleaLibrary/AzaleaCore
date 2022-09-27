package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@Commands(@Command(name = AzaleaCommand.COMMAND_PREFIX + "property"))
public class PropertyCommand extends AzaleaCommand {

    @Override
    protected Message execute(@Nonnull CommandSender sender, String[] params) {
        return new ChatMessage(Arrays.toString(params));
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String option, String[] params) {
        return List.of("wer", "ert", "ertrty", "rty", "rtyytui", "tyu", "tyuuyio", "yui", "iuo", "oip");
    }
}
