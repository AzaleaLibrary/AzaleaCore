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
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.List;

@AzaCommand(name = "!room")
public class RoomCommand extends AzaleaCommand {

    private static final String CREATE_NEW = "create_new";
    private static final String FROM_PLAYGROUND = "from_playground";
    private static final String TERMINATE = "terminate";

    public RoomCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE_NEW, FROM_PLAYGROUND, TERMINATE));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(TERMINATE), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(CREATE_NEW), (sender, arguments) -> AzaleaMinigameApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(FROM_PLAYGROUND), (sender, arguments) -> AzaleaPlaygroundApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && arguments.get(0).equals(CREATE_NEW), (sender, arguments) -> FileUtil.maps().stream().map(File::getName).toList());
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(CREATE_NEW), this::createNew);
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(FROM_PLAYGROUND), this::fromPlayground);
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(TERMINATE), this::terminate);
    }

    private Message createNew(CommandSender sender, Arguments arguments) {
        Minigame minigame = arguments.find(1, "minigame", AzaleaMinigameApi.getInstance()::get).get();
        File map = arguments.find(2, "map", FileUtil::map);
        String name = arguments.notMissing(3, "name");

        return createRoom(sender, name, minigame, map);
    }

    private Message fromPlayground(CommandSender sender, Arguments arguments) {
        Playground playground = arguments.find(1, "playground", AzaleaPlaygroundApi.getInstance()::get);
        String name = arguments.notMissing(2, "name");

        return createRoom(sender, name, playground.getMinigame(), playground.getMap());
    }

    private static Message createRoom(CommandSender sender, String name, Minigame minigame, File map) {
        if (AzaleaRoomApi.getInstance().hasKey(name)) {
            return ChatMessage.failure("Room '" + name + "' already exists.");
        }

        ChatMessage message = ChatMessage.info("Creating room '" + name + "'...");
        message.post(AzaleaCore.PLUGIN_ID, sender); // TODO - create AzaleaBroadcast

        FileUtil.copyDirectory(map, new File(FileUtil.ROOMS, name));
        WorldCreator creator = new WorldCreator("azalea/rooms/" + name);

        Room room = new Room(name, minigame, Bukkit.createWorld(creator), map);
        AzaleaRoomApi.getInstance().add(name, room);

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
