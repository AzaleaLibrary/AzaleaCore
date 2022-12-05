package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.AzaCommand;
import com.azalealibrary.azaleacore.command.core.AzaleaCommand;
import com.azalealibrary.azaleacore.command.core.CommandConfigurator;
import com.azalealibrary.azaleacore.command.core.CommandDescriptor;

@AzaCommand(name = "!configure")
public class ConfigureCommand extends AzaleaCommand {

    private static final String ROOM = "getPlayground";
    private static final String MINIGAME = "minigame";

    private static final String SET = "set";
    private static final String RESET = "reset";
    private static final String INFO = "info";

    public ConfigureCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> List.of(ROOM, MINIGAME));
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 3, (sender, arguments) -> getProperties(arguments).stream().map(ConfigurableProperty::getName).toList());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 4, (sender, arguments) -> List.of(SET, RESET, INFO));
//        configurator.completeWhen((sender, arguments) -> arguments.is(3, SET), (sender, arguments) -> {
//            Optional<ConfigurableProperty<?>> property = getProperties(arguments).stream().filter(p -> arguments.is(2, p.getName())).findFirst();
//            return property.isPresent() ? property.get().suggest(sender, arguments.subArguments(4)) : List.of();
//        });
//        configurator.executeWhen((sender, arguments) -> arguments.size() > 3, this::execute);
    }

//    private Message execute(CommandSender sender, Arguments arguments) {
//        ConfigurableProperty<?> property = arguments.find(2, "property", input -> getProperties(arguments).stream().filter(p -> p.getName().equals(input)).findFirst().orElse(null));
//        String action = arguments.matchesAny(3, "action", SET, RESET, INFO);
//
//        if (action.equals(SET)) {
//            property.fromCommand(sender, arguments.subArguments(4));
//            return ChatMessage.info("Property '" + property.getName() + "' updated.");
//        } else if (action.equals(RESET)) {
//            property.reset();
//            return ChatMessage.info("Property '" + property.getName() + "' has been reset.");
//        } else {
//            return ChatMessage.info(property.toString());
//        }
//    }
//
//    private static List<ConfigurableProperty<?>> getProperties(Arguments arguments) {
//        Room getPlayground = arguments.find(0, "getPlayground", AzaleaRoomApi.getInstance()::get);
//        String action = arguments.matchesAny(1, "action", ROOM, MINIGAME);
//        return action.equals(ROOM) ? getPlayground.getConfiguration().getProperties() : getPlayground.getMinigame().getProperties();
//    }
}
