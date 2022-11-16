package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.room.MinigameRoom;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import java.util.List;

@Commands(@Command(name = MinigameCommand.NAME))
public class MinigameCommand extends AzaleaCommand {

    protected static final String NAME = "!minigame";

    private static final String START = "start";
    private static final String END = "end";
    private static final String RESTART = "restart";

    public MinigameCommand(JavaPlugin plugin) {
        super(plugin, NAME);
        completeWhen(arguments -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        completeWhen(arguments -> arguments.size() == 2, (sender, arguments) -> List.of(START, END, RESTART));
        executeWhen(arguments -> arguments.size() == 2, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        MinigameRoom room = arguments.parse(0, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().get(input));
        String action = arguments.matching(1, START, END, RESTART);

        Message message = arguments.size() > 2
                ? new ChatMessage(String.join(" ", arguments.subList(2, arguments.size())))
                : new ChatMessage("Minigame " + action.toLowerCase() + "ed by " + ChatColor.YELLOW + sender.getName() + ChatColor.RESET + ".");

        switch (action) {
            case START -> room.start(message);
            case END -> room.stop(message);
            case RESTART -> room.restart(message);
        }
        return success("Minigame in room '" + room.getName() + "' " + action.toLowerCase() + "ed.");
    }
}
