package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.command.core.CommandHandler;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import java.util.Arrays;

@Commands(@Command(name = BroadcastCommand.NAME))
public class BroadcastCommand {

    protected static final String NAME = "!broadcast";

    public BroadcastCommand(CommandHandler handler) {
        handler.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        handler.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> Arrays.stream(Broadcaster.Chanel.values()).map(v -> v.toString().toLowerCase()).toList());
        handler.executeWhen((sender, arguments) -> arguments.size() > 2, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        Room room = arguments.parse(0, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().get(input));
        Broadcaster.Chanel chanel = arguments.parse(1, "'%s' is not a valid chanel.", input -> Broadcaster.Chanel.valueOf(input.toUpperCase()));

        String input = String.join(" ", arguments.subList(2, arguments.size()));
        Message message = new ChatMessage(ChatColor.ITALIC + input);
        room.getBroadcaster().broadcast(message, chanel);

        return null;
    }
}
