package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.api.AzaleaMinigameApi;
import com.azalealibrary.azaleacore.api.AzaleaPlaygroundApi;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.WorldCreator;
import org.bukkit.command.CommandSender;

import java.io.File;
import java.util.List;

@AzaCommand(name = "!room")
public class RoomCommand extends AzaleaCommand {

    private static final String CREATE = "create";
    private static final String TEMPLATE = "template";
    private static final String TERMINATE = "terminate";

    public RoomCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, TEMPLATE, TERMINATE));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(TERMINATE), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(CREATE), (sender, arguments) -> AzaleaMinigameApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.get(0).equals(TEMPLATE), (sender, arguments) -> AzaleaPlaygroundApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && arguments.get(0).equals(CREATE), (sender, arguments) -> FileUtil.templates().stream().map(File::getName).toList());
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(CREATE), this::create);
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(TEMPLATE), this::template);
        configurator.executeWhen((sender, arguments) -> arguments.get(0).equals(TERMINATE), this::terminate);
    }

    private Message create(CommandSender sender, Arguments arguments) {
        Minigame minigame = arguments.parse(1, "Could not find minigame '%s'.", input -> AzaleaMinigameApi.getInstance().get(input)).get();
        File template = arguments.parse(2, "Could not find template '%s'.", FileUtil::template);
        String name = arguments.missing(3);

        return createRoom(sender, name, minigame, template);
    }

    private Message template(CommandSender sender, Arguments arguments) {
        Playground playground = arguments.parse(1, "Could not find playground '%s'.", input -> AzaleaPlaygroundApi.getInstance().get(input));
        String name = arguments.missing(2);

        return createRoom(sender, name, playground.getMinigame(), playground.getTemplate());
    }

    private static Message createRoom(CommandSender sender, String name, Minigame minigame, File template) {
        if (AzaleaRoomApi.getInstance().get(name) != null) {
            return ChatMessage.failure("Room '" + name + "' already exists.");
        }

        ChatMessage message = new ChatMessage(ChatColor.GRAY + "Creating room '" + name + "'...");
        message.post(AzaleaCore.PLUGIN_ID, sender); // TODO - create AzaleaBroadcast

        FileUtil.copyDirectory(template, new File(FileUtil.ROOMS, name));
        WorldCreator creator = new WorldCreator("azalea/rooms/" + name);

        Room room = new Room(name, minigame, Bukkit.createWorld(creator), template);
        AzaleaRoomApi.getInstance().add(name, room);

        return ChatMessage.success("Room '" + name + "' created.");
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
