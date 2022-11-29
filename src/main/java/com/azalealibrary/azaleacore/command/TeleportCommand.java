package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
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
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, ROOM), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.size() == 1 && arguments.is(0, LOBBY), this::toLobby);
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2, this::toRoom);
    }

    private Message toLobby(CommandSender sender, Arguments arguments) {
        if (sender instanceof Player player) {
            Room room = AzaleaRoomApi.getInstance().getObjects().stream()
                    .filter(r -> r.getWorld().getPlayers().contains(player))
                    .findFirst().orElse(null);

            if (room != null) {
                room.removePlayer(player); // remove player from their room
            } else {
                player.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation());
            }
        }
        return null;
    }

    private Message toRoom(CommandSender sender, Arguments arguments) {
        Room room = arguments.find(1, "room", AzaleaRoomApi.getInstance()::get);

        if (sender instanceof Player player) {
            room.addPlayer(player);
        }
        return null;
    }
}
