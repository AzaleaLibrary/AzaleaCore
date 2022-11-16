package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.room.MinigameRoom;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import java.io.File;
import java.util.List;

@Commands(@Command(name = RoomCommand.NAME))
public class RoomCommand extends AzaleaCommand {

    protected static final String NAME = "!room";

    private static final String CREATE = "create";
    private static final String TERMINATE = "terminate";
    private static final String BROADCAST = "broadcast";
    private static final String SIGN = "sign";
    private static final String WORLD = "world";
    private static final String LOBBY = "lobby";

    public RoomCommand(JavaPlugin plugin) {
        super(plugin, NAME);
        completeWhen(arguments -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, TERMINATE, BROADCAST, SIGN));
        completeWhen(arguments -> arguments.size() == 2, (sender, arguments) -> switch (arguments.get(0)) {
            case CREATE -> AzaleaApi.getInstance().getMinigames().keySet().stream().toList();
            case TERMINATE, BROADCAST, SIGN -> AzaleaApi.getInstance().getRooms().stream().map(MinigameRoom::getName).toList();
            default -> List.of();
        });
        completeWhen(arguments -> arguments.size() == 3, (sender, arguments) -> switch (arguments.get(0)) {
            case CREATE -> FileUtil.templates().stream().map(File::getName).toList();
            case SIGN -> List.of(WORLD, LOBBY);
            default -> List.of();
        });

        executeWhen(arguments -> arguments.get(0).equals(CREATE), this::handleCreate);
        executeWhen(arguments -> arguments.get(0).equals(TERMINATE), this::handleTerminate);
        executeWhen(arguments -> arguments.get(0).equals(BROADCAST), this::handleBroadcast);
        executeWhen(arguments -> arguments.get(0).equals(SIGN), this::handleSign);
    }

    private Message handleCreate(CommandSender sender, Arguments arguments) {
        AzaleaApi.MinigameProvider provider = arguments.parse(1, "", input -> AzaleaApi.getInstance().getMinigame(input));
        File template = arguments.parse(2, "", FileUtil::template);
        String nameInput = arguments.missing(3);

        if (AzaleaApi.getInstance().getRoom(nameInput) != null) {
            return failure("Room '" + nameInput + "' already exists.");
        }

        if (sender instanceof Player player) {
            AzaleaApi.getInstance().createRoom(provider, nameInput, player.getWorld(), template);

            return success("Room '" + nameInput + "' created.");
        }
        return failure("Command issuer not a player.");
    }

    private Message handleTerminate(CommandSender sender, Arguments arguments) {
        MinigameRoom room = arguments.parse(1, "Could not find room '%s'.", input -> AzaleaApi.getInstance().getRoom(input));

        Message message = arguments.size() > 1
                ? new ChatMessage(String.join(" ", arguments.subList(1, arguments.size())))
                : new ChatMessage("Game ended by " + ChatColor.YELLOW + sender.getName() + ChatColor.RESET + ".");
        room.terminate(message);

        return success("Terminating room '" + room.getName() + "'.");
    }

    private Message handleBroadcast(CommandSender sender, Arguments arguments) {
        MinigameRoom room = arguments.parse(1, "Could not find room '%s'.", input -> AzaleaApi.getInstance().getRoom(input));

        String input = String.join(" ", arguments.subList(2, arguments.size()));
        Message message = new ChatMessage(ChatColor.ITALIC + input);
        room.getBroadcaster().broadcast(message);

        return null;
    }

    public Message handleSign(CommandSender sender, Arguments arguments) {
        MinigameRoom room = arguments.parse(1, "Could not find room '%s'.", input -> AzaleaApi.getInstance().getRoom(input));
        String action = arguments.matching(2, WORLD, LOBBY);

        if (sender instanceof Player player) {
            Block target = player.getTargetBlock(null, 10);

            if (target.getState() instanceof Sign sign) {
                Location location = target.getLocation();
                List<Location> signs = action.equals(WORLD)
                        ? room.getSignTicker().getToWorldSigns()
                        : room.getSignTicker().getToLobbySigns();

                for (int i = 0; i < 4; i++) {
                    sign.setLine(i, "");
                }
                sign.update();

                if (!signs.contains(location)) {
                    signs.add(location);
                    return success("Added sign to " + room.getName() + " " + action + ".");
                } else {
                    if (signs.remove(location)) {
                        return success("Removed sign to " + room.getName() + " " + action + ".");
                    }
                    return warn("Could not removed sign...");
                }
            }
            return failure("Target block is not a sign.");
        }
        return failure("Command issuer not a player.");
    }
}
