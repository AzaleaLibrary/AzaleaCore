package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.AzaCommand;
import com.azalealibrary.azaleacore.command.core.AzaleaCommand;
import com.azalealibrary.azaleacore.command.core.CommandConfigurator;
import com.azalealibrary.azaleacore.command.core.CommandDescriptor;

@AzaCommand(name = "!minigame")
public class MinigameCommand extends AzaleaCommand {

    private static final String START = "start";
    private static final String END = "end";
    private static final String RESTART = "restart";

    public MinigameCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> List.of(START, END, RESTART));
//        configurator.executeWhen((sender, arguments) -> arguments.size() == 2, this::execute);
    }

//    private Message execute(CommandSender sender, Arguments arguments) {
//        Room getPlayground = arguments.find(0, "getPlayground", AzaleaRoomApi.getInstance()::get);
//        String action = arguments.matchesAny(1, "action", START, END, RESTART);
//
//        Message message = arguments.size() > 2
//                ? ChatMessage.info(String.join(" ", arguments.subList(2, arguments.size())))
//                : ChatMessage.info("Minigame " + action.toLowerCase() + "ed by " + TextUtil.getName((Player) sender) + ".");
//
//        switch (action) {
//            case START -> getPlayground.start(message);
//            case END -> getPlayground.stop(message);
//            case RESTART -> getPlayground.restart(message);
//        }
//        return ChatMessage.announcement("Minigame in getPlayground " + TextUtil.getName(getPlayground) + " " + action.toLowerCase() + "ing.");
//    }
}
