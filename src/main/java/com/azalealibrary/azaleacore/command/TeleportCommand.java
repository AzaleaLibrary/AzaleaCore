package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
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
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> List.of(LOBBY, ROOM));
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        Room room = arguments.parse(0, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().get(input));
        String action = arguments.matching(1, LOBBY, ROOM);

        if (sender instanceof Player player) {
            switch (action) {
                case LOBBY -> player.teleport(room.getLobby().getSpawnLocation());
                case ROOM -> player.teleport(room.getWorld().getSpawnLocation());
            }
        }
        return null;
    }
}
