package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaPlaygroundApi;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.room.Room;
import org.bukkit.command.CommandSender;

import java.util.List;

@AzaCommand(name = "!playground")
public class PlaygroundCommand extends AzaleaCommand {

    private static final String CREATE = "create";
    private static final String DELETE = "delete";

    public PlaygroundCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, DELETE));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(CREATE), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(DELETE), (sender, arguments) -> AzaleaPlaygroundApi.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(CREATE), this::create);
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(DELETE), this::delete);
    }

    private Message create(CommandSender sender, Arguments arguments) {
        Room room = arguments.parse(1, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().get(input));
        String name = arguments.missing(2);

        if (AzaleaPlaygroundApi.getInstance().get(name) != null) {
            return ChatMessage.failure("Playground '" + name + "' already exists.");
        }

        Playground playground = new Playground(name, room.getMap(), room.getMinigame());
        AzaleaPlaygroundApi.getInstance().add(name, playground);

        return ChatMessage.success("Playground '" + name + "' created.");
    }

    private Message delete(CommandSender sender, Arguments arguments) {
        Playground playground = arguments.parse(1, "Could not find playground '%s'.", input -> AzaleaPlaygroundApi.getInstance().get(input));
        AzaleaPlaygroundApi.getInstance().remove(playground);

        return ChatMessage.success("Deleted '" + playground.getName() + "' playground.");
    }
}
