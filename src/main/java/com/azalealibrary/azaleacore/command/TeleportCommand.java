package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.room.Room;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AzaCommand(name = "!teleport")
public class TeleportCommand extends AzaleaCommand {

    private static final String LOBBY = "lobby";
    private static final String ROOM = "room";

    public TeleportCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(LOBBY, ROOM));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(ROOM), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.size() == 1 && arguments.get(0).equals(LOBBY), this::toLobby);
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2, this::toRoom);
    }

    private Message toLobby(CommandSender sender, Arguments arguments) {
        if (sender instanceof Player player) {
            player.teleport(AzaleaCore.getLobby().getSpawnLocation());
        }
        return null;
    }

    private Message toRoom(CommandSender sender, Arguments arguments) {
        Room room = arguments.parse(1, "Could not find room '%s'.", AzaleaRoomApi.getInstance()::get);

        if (sender instanceof Player player) {
            player.teleport(room.getWorld().getSpawnLocation().clone().add(.5, .5, .5));
        }
        return null;
    }
}
