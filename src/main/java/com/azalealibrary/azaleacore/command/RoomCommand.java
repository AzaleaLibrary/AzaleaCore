package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.room.MinigameRoom;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

@Commands(@Command(name = RoomCommand.NAME))
public class RoomCommand extends AzaleaCommand {

    protected static final String NAME = "!room";

    private static final String CREATE = "CREATE";
    private static final String TERMINATE = "TERMINATE";

    public RoomCommand(JavaPlugin plugin) {
        super(plugin, NAME);
    }

    @Override
    protected Message execute(@Nonnull CommandSender sender, List<String> params) {
        String actionInput = params.get(0);

        return switch (actionInput) {
            case CREATE -> handleCreate(sender, params);
            case TERMINATE -> handleTerminate(sender, params);
            default -> invalid("action", actionInput);
        };
    }

    private Message handleCreate(CommandSender sender, List<String> params) {
        String minigameInput = params.get(1);
        String templateInput = params.get(2);
        String nameInput = params.get(3);

        AzaleaApi.MinigameProvider provider = AzaleaApi.getInstance().getMinigame(minigameInput);
        if (provider == null) {
            return notFound("minigame", minigameInput);
        }

        File template = FileUtil.template(templateInput);
        if (!template.exists()) {
            return notFound("template", templateInput);
        }

        if (AzaleaApi.getInstance().getRoom(nameInput) != null) {
            return failure("Room '" + nameInput + "' already exists.");
        }

        MinigameRoom room = AzaleaApi.getInstance().createRoom(provider, nameInput, ((Player) sender).getWorld(), template);
        room.teleportToWorld();

        return success("Room '" + nameInput + "' created.");
    }

    private Message handleTerminate(CommandSender sender, List<String> params) {
        String roomInput = params.get(1);

        MinigameRoom room = AzaleaApi.getInstance().getRoom(roomInput);
        if (room == null) {
            return notFound("room", roomInput);
        }

        Message message = params.size() > 1
                ? new ChatMessage(String.join(" ", params.subList(1, params.size())))
                : new ChatMessage("Game ended by " + ChatColor.YELLOW + sender.getName() + ChatColor.RESET + ".");
        room.terminate(message);

        return success("Terminating room '" + roomInput + "'.");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return List.of(CREATE, TERMINATE);
        } else {
            String action = params.get(0);

            if (params.size() == 2) {
                if (action.equals(CREATE)) {
                    return AzaleaApi.getInstance().getMinigames().keySet().stream().toList();
                } else if (action.equals(TERMINATE)) {
                    return AzaleaApi.getInstance().getRooms().stream().map(MinigameRoom::getName).toList();
                }
            } else if (params.size() == 3 && action.equals(CREATE)) {
                return FileUtil.templates().stream().map(File::getName).toList();
            }
        }
        return List.of();
    }
}
