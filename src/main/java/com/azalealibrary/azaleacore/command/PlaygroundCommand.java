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
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, CREATE), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, DELETE), (sender, arguments) -> AzaleaPlaygroundApi.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.is(0, CREATE), this::create);
        configurator.executeWhen((sender, arguments) -> arguments.is(0, DELETE), this::delete);
    }

    private Message create(CommandSender sender, Arguments arguments) {
        Room room = arguments.find(1, "room", AzaleaRoomApi.getInstance()::get);
        String name = arguments.notMissing(2, "name");

        if (AzaleaPlaygroundApi.getInstance().hasKey(name)) {
            return ChatMessage.failure("Playground '" + name + "' already exists.");
        }

        Playground playground = new Playground(name, room.getMap(), room.getMinigame());
        AzaleaPlaygroundApi.getInstance().add(name, playground);

        return ChatMessage.success("Playground '" + name + "' created.");
    }

    private Message delete(CommandSender sender, Arguments arguments) {
        Playground playground = arguments.find(1, "playground", AzaleaPlaygroundApi.getInstance()::get);
        AzaleaPlaygroundApi.getInstance().remove(playground);

        return ChatMessage.success("Deleted '" + playground.getName() + "' playground.");
    }
}
