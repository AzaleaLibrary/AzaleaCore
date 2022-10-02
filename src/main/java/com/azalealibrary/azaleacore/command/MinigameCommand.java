package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.broadcast.message.Message;
import com.azalealibrary.azaleacore.minigame.MinigameRoom;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Commands(@Command(name = MinigameCommand.NAME))
public class MinigameCommand extends AzaleaCommand {

    protected static final String NAME = AzaleaCommand.COMMAND_PREFIX + "minigame";

    private static final String START = "START";
    private static final String END = "END";
    private static final String RESTART = "RESTART";

    public MinigameCommand(JavaPlugin plugin) {
        super(plugin, NAME);
    }

    @Override
    protected Message execute(@Nonnull CommandSender sender, List<String> params) {
        String actionInput = params.get(0);
        if (!List.of(START, END, RESTART).contains(actionInput)) {
            return invalid("action", actionInput);
        }

        String roomInput = params.get(1);
        Optional<MinigameRoom<?, ?>> room = AzaleaApi.getInstance().getMinigameRooms().stream()
                .filter(r -> r.getName().equals(roomInput))
                .findFirst();
        if (room.isEmpty()) {
            return notFound("room", roomInput);
        }

        Message message = params.size() > 3 ? new ChatMessage(String.join(" ", params.subList(3, params.size()))) : null;

        switch (actionInput) {
            case START -> room.get().start(message);
            case END -> room.get().stop(message);
            case RESTART -> room.get().restart(message);
        }
        return success("Minigame in room '" + roomInput + "' " + actionInput.toLowerCase() + "ed.");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return List.of(START, END, RESTART);
        } else if (params.size() == 2) {
            return AzaleaApi.getInstance().getMinigameRooms().stream().map(MinigameRoom::getName).toList();
        }
        return List.of();
    }
}
