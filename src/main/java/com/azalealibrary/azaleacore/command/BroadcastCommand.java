package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.AzaCommand;
import com.azalealibrary.azaleacore.command.core.AzaleaCommand;
import com.azalealibrary.azaleacore.command.core.CommandConfigurator;
import com.azalealibrary.azaleacore.command.core.CommandDescriptor;

@AzaCommand(name = "!broadcast")
public class BroadcastCommand extends AzaleaCommand {

    public BroadcastCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> Arrays.stream(Broadcaster.Chanel.values()).map(v -> v.toString().toLowerCase()).toList());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 3, (sender, arguments) -> Arrays.stream(ChatMessage.LogType.values()).map(v -> v.toString().toLowerCase()).toList());
//        configurator.executeWhen((sender, arguments) -> arguments.size() > 2, this::execute);
    }

//    private Message execute(CommandSender sender, Arguments arguments) {
//        Room getPlayground = arguments.find(0, "getPlayground", AzaleaRoomApi.getInstance()::get);
//        Broadcaster.Chanel chanel = arguments.find(1, "broadcast chanel", input -> Broadcaster.Chanel.valueOf(input.toUpperCase()));
//        ChatMessage.LogType logType = arguments.find(2, "log type", input -> ChatMessage.LogType.valueOf(input.toUpperCase()));
//
//        String input = String.join(" ", arguments.subList(3, arguments.size()));
//        Message message = new ChatMessage(input, logType);
//        getPlayground.getBroadcaster().broadcast(message, chanel);
//
//        return null;
//    }
}
