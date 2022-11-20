package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaMinigameApi;
import com.azalealibrary.azaleacore.api.AzaleaPlaygroundApi;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AzaCommand(name = "!room")
public class RoomCommand extends AzaleaCommand {

    private static final String CREATE = "create";
    private static final String TERMINATE = "terminate";

    public RoomCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, TERMINATE));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(CREATE), (sender, arguments) -> AzaleaMinigameApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(TERMINATE), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && arguments.get(0).equals(CREATE), (sender, arguments) -> AzaleaPlaygroundApi.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(CREATE), this::create);
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(TERMINATE), this::terminate);
    }

    private Message create(CommandSender sender, Arguments arguments) {
        AzaleaMinigameApi.MinigameProvider provider = arguments.parse(1, "", input -> AzaleaMinigameApi.getInstance().get(input));
        Playground playground = arguments.parse(2, "Could not find playground '%s'", input -> AzaleaPlaygroundApi.getInstance().get(input));
        String name = arguments.missing(3);

        if (AzaleaRoomApi.getInstance().get(name) != null) {
            return ChatMessage.failure("Room '" + name + "' already exists.");
        }

        if (sender instanceof Player player) {
            Room room = Room.create(name, playground, player.getWorld(), provider);
            AzaleaRoomApi.getInstance().add(name, room);

            return ChatMessage.success("Room '" + name + "' created.");
        }
        return ChatMessage.failure("Command issuer not a player.");
    }

    private Message terminate(CommandSender sender, Arguments arguments) {
        Room room = arguments.parse(1, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().get(input));

        Message message = arguments.size() > 1
                ? new ChatMessage(String.join(" ", arguments.subList(1, arguments.size())))
                : new ChatMessage("Game ended by " + ChatColor.YELLOW + sender.getName() + ChatColor.RESET + ".");
        room.terminate(message);

        return ChatMessage.success("Terminating room '" + room.getName() + "'.");
    }
}
