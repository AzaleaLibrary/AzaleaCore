package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.broadcast.message.Message;
import com.azalealibrary.azaleacore.minigame.MinigameRoom;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;
import java.util.Optional;

import static com.azalealibrary.azaleacore.command.MinigameCommand.NAME;

@Commands(@Command(name = NAME))
public class MinigameCommand extends AzaleaCommand {

    protected static final String NAME = AzaleaCommand.COMMAND_PREFIX + "minigame";

    public static final String CREATE = "CREATE";
    public static final String TERMINATE = "TERMINATE";
    public static final String START = "START";
    public static final String END = "END";
    public static final String RESTART = "RESTART";

    public MinigameCommand(JavaPlugin plugin) {
        super(plugin, NAME);
    }

    @Override
    protected Message execute(@Nonnull CommandSender sender, List<String> params) {
        String actionInput = params.get(0);
        if (!List.of(CREATE, TERMINATE, START, END, RESTART).contains(actionInput)) {
            return invalid("action", actionInput);
        }

        if (actionInput.equals(CREATE)) {
            String minigameInput = params.get(1);
            AzaleaApi.MinigameProvider<?> provider = AzaleaApi.getInstance().getRegisteredMinigames().get(minigameInput);
            if (provider == null) {
                return notFound("minigame", minigameInput);
            }

            String templateInput = params.get(2);
            Optional<String> template = FileUtil.directories(new File(Bukkit.getWorldContainer(), "templates/")).stream().map(File::getName)
                    .filter(n -> n.equals(templateInput))
                    .findFirst();
            if (template.isEmpty()) {
                return notFound("template", templateInput);
            }

            String nameInput = params.size() > 3 ? params.get(3) : null;
            if (nameInput == null) {
                return invalid("name", ChatColor.ITALIC + "<empty>");
            }

            AzaleaApi.getInstance().createRoom(provider, ((Player) sender).getWorld(), nameInput, template.get());
            return success("New '" + nameInput + "' " + minigameInput + " room created with template '" + templateInput + "'.");
        } else {
            String roomInput = params.get(1);
            Optional<MinigameRoom<?, ?>> optionalRoom = AzaleaApi.getInstance().getMinigameRooms().stream()
                    .filter(r -> r.getName().equals(roomInput))
                    .findFirst();
            if (optionalRoom.isEmpty()) {
                return notFound("room", roomInput);
            }

            MinigameRoom<?, ?> room = optionalRoom.get();
            Message message = params.size() > 3 ? new ChatMessage(String.join(" ", params.subList(3, params.size()))) : null;

            switch (actionInput) {
                case START -> room.start(((Player) sender).getWorld().getPlayers(), message);
                case END -> room.stop(message);
                case RESTART -> room.restart(message);
                case TERMINATE -> room.terminate();
            }
            return success("Minigame in room '" + roomInput + "' " + actionInput.toLowerCase() + "ed.");
        }
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return List.of(CREATE, TERMINATE, START, END, RESTART);
        } else if (params.size() == 2) {
            return params.get(0).equals(CREATE)
                    ? AzaleaApi.getInstance().getRegisteredMinigames().keySet().stream().toList()
                    : AzaleaApi.getInstance().getMinigameRooms().stream().map(MinigameRoom::getName).toList();
        } else if (params.size() == 3) {
            String action = params.get(0);

            if (action.equals(CREATE)) {
                return FileUtil.directories(new File(Bukkit.getWorldContainer(), "templates/")).stream().map(File::getName).toList();
            }
            if (action.equals(TERMINATE)) {
                return AzaleaApi.getInstance().getMinigameRooms().stream().map(MinigameRoom::getName).toList();
            }
        }
        return List.of();
    }
}
