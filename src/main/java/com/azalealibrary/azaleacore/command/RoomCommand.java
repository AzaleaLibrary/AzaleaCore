package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.api.AzaleaMinigameApi;
import com.azalealibrary.azaleacore.api.AzaleaPlaygroundApi;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

@AzaCommand(name = "!room")
public class RoomCommand extends AzaleaCommand {

    private static final String CREATE = "create";
    private static final String TERMINATE = "terminate";

    private static final String NEW = "new";
    private static final String COPY = "copy";

    public RoomCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, TERMINATE));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, CREATE), (sender, arguments) -> List.of(NEW, COPY));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, TERMINATE), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && arguments.is(1, NEW), (sender, arguments) -> AzaleaMinigameApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 4 && arguments.is(1, NEW), (sender, arguments) -> FileUtil.maps().stream().map(File::getName).toList());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && arguments.is(1, COPY), (sender, arguments) -> AzaleaPlaygroundApi.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.is(0, TERMINATE), this::terminate);
        configurator.executeWhen((sender, arguments) -> arguments.is(1, NEW), this::createNew);
        configurator.executeWhen((sender, arguments) -> arguments.is(1, COPY), this::createCopy);
    }

    private Message createNew(CommandSender sender, Arguments arguments) {
        Minigame minigame = arguments.find(2, "minigame", AzaleaMinigameApi.getInstance()::get).get();
        File map = arguments.find(3, "map", FileUtil::map);
        String name = arguments.notMissing(4, "name");

        return createRoom(sender, name, minigame, map);
    }

    private Message createCopy(CommandSender sender, Arguments arguments) {
        Playground playground = arguments.find(2, "playground", AzaleaPlaygroundApi.getInstance()::get);
        String name = arguments.notMissing(3, "name");

        return createRoom(sender, name, playground.getMinigame(), playground.getMap());
    }

    private static Message createRoom(CommandSender sender, String name, Minigame minigame, File map) {
        if (AzaleaRoomApi.getInstance().hasKey(name)) {
            return ChatMessage.failure("Room '" + name + "' already exists.");
        }

        AzaleaCore.BROADCASTER.send(sender, ChatMessage.info("Creating room '" + name + "'..."));
        AzaleaRoomApi.getInstance().createRoom((Player) sender, name, map, minigame);

        return ChatMessage.success("Room '" + name + "' created.");
    }

    private Message terminate(CommandSender sender, Arguments arguments) {
        Room room = arguments.find(1, "room", AzaleaRoomApi.getInstance()::get);

        Message message = arguments.size() > 1
                ? new ChatMessage(String.join(" ", arguments.subList(1, arguments.size())))
                : new ChatMessage("Game ended by " + ChatColor.YELLOW + sender.getName() + ChatColor.RESET + ".");
        room.terminate(message);

        return ChatMessage.success("Terminating room '" + room.getName() + "'.");
    }
}
