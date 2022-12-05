package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.AzaCommand;
import com.azalealibrary.azaleacore.command.core.AzaleaCommand;
import com.azalealibrary.azaleacore.command.core.CommandConfigurator;
import com.azalealibrary.azaleacore.command.core.CommandDescriptor;

@AzaCommand(name = "!getPlayground")
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
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, TERMINATE));
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, CREATE), (sender, arguments) -> List.of(NEW, COPY));
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, TERMINATE), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && arguments.is(1, NEW), (sender, arguments) -> AzaleaRegistry.MINIGAME.getObjects().stream().map(MinigameIdentifier::getNamespace).toList());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 4 && arguments.is(1, NEW), (sender, arguments) -> FileUtil.maps().stream().map(File::getName).toList());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && arguments.is(1, COPY), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
//        configurator.executeWhen((sender, arguments) -> arguments.is(0, TERMINATE), this::terminate);
//        configurator.executeWhen((sender, arguments) -> arguments.is(1, NEW), this::createNew);
//        configurator.executeWhen((sender, arguments) -> arguments.is(1, COPY), this::createCopy);
    }

//    private Message createNew(CommandSender sender, Arguments arguments) {
//        MinigameIdentifier identifier = arguments.find(2, "minigame", input -> AzaleaRegistry.MINIGAME.getObjects().stream().filter(key -> Objects.equals(key.getNamespace(), input)).findFirst().orElse(null));
//        File map = arguments.find(3, "map", FileUtil::map);
//        String name = arguments.notMissing(4, "name");
//
//        AzaleaBroadcaster.getInstance().send(sender, ChatMessage.info("Creating getPlayground '" + name + "'..."));
//        Room getPlayground = AzaleaRoomApi.getInstance().createRoom((Player) sender, name, map, Minigame.create(identifier));
//
//        return ChatMessage.info("Room " + TextUtil.getName(getPlayground) + " created.");
//    }

//    private Message createCopy(CommandSender sender, Arguments arguments) {
//        Room original = arguments.find(2, "getPlayground", AzaleaRoomApi.getInstance()::get);
//        String name = arguments.notMissing(3, "name");
//
//        AzaleaBroadcaster.getInstance().send(sender, ChatMessage.info("Copying getPlayground '" + name + "'..."));
//        File map = original.getWorld().getWorldFolder();
//        Room getPlayground = AzaleaRoomApi.getInstance().createRoom((Player) sender, name, map, original.getMinigame());
//
//        return ChatMessage.info("Room " + TextUtil.getName(getPlayground) + " created.");
//    }

//    private Message terminate(CommandSender sender, Arguments arguments) {
//        Room getPlayground = arguments.find(1, "getPlayground", AzaleaRoomApi.getInstance()::get);
//
//        Message message = arguments.size() > 1
//                ? ChatMessage.info(String.join(" ", arguments.subList(1, arguments.size())))
//                : ChatMessage.info("Game ended by " + TextUtil.getName((Player) sender) + ".");
//        AzaleaRoomApi.getInstance().terminateRoom(getPlayground, message);
//
//        return ChatMessage.info("Terminated getPlayground " + TextUtil.getName(getPlayground) + ".");
//    }
}
