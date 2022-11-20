package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

@AzaCommand(name = "!minigame")
public class MinigameCommand extends AzaleaCommand {

    private static final String START = "start";
    private static final String END = "end";
    private static final String RESTART = "restart";

    public MinigameCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> List.of(START, END, RESTART));
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        Room room = arguments.parse(0, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().get(input));
        String action = arguments.matching(1, START, END, RESTART);

        Message message = arguments.size() > 2
                ? new ChatMessage(String.join(" ", arguments.subList(2, arguments.size())))
                : new ChatMessage("Minigame " + action.toLowerCase() + "ed by " + ChatColor.YELLOW + sender.getName() + ChatColor.RESET + ".");

        switch (action) {
            case START -> room.start(message);
            case END -> room.stop(message);
            case RESTART -> room.restart(message);
        }
        return ChatMessage.success("Minigame in room '" + room.getName() + "' " + action.toLowerCase() + "ed.");
    }
}
