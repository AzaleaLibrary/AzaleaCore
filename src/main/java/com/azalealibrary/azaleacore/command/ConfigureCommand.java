package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.room.Room;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

@AzaCommand(name = "!configure")
public class ConfigureCommand extends AzaleaCommand {

    private static final String ROOM = "room";
    private static final String MINIGAME = "minigame";

    private static final String SET = "set";
    private static final String RESET = "reset";
    private static final String INFO = "info";

    public ConfigureCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> List.of(ROOM, MINIGAME));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3, (sender, arguments) -> getProperties(arguments).stream().map(Property::getName).toList());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 4, (sender, arguments) -> List.of(SET, RESET, INFO));
        configurator.completeWhen((sender, arguments) -> arguments.is(3, SET), (sender, arguments) -> {
            Optional<Property<?>> property = getProperties(arguments).stream().filter(p -> p.getName().equals(arguments.get(2))).findFirst();
            return property.isPresent() ? property.get().suggest(sender, arguments.subArguments(4)) : List.of();
        });
        configurator.executeWhen((sender, arguments) -> arguments.size() > 3, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        Property<?> property = arguments.find(2, "property", input -> getProperties(arguments).stream().filter(p -> p.getName().equals(input)).findFirst().orElse(null));
        String action = arguments.matchesAny(3, "action", SET, RESET, INFO);

        if (action.equals(SET)) {
            property.set(sender, arguments.subArguments(4));
            return ChatMessage.success("Property '" + property.getName() + "' updated.");
        } else if (action.equals(RESET)) {
            property.reset();
            return ChatMessage.success("Property '" + property.getName() + "' has been reset.");
        } else {
            return ChatMessage.info("Property: " + property);
        }
    }

    private static List<Property<?>> getProperties(Arguments arguments) {
        Room room = arguments.find(0, "room", AzaleaRoomApi.getInstance()::get);
        String action = arguments.matchesAny(1, "action", ROOM, MINIGAME);
        return action.equals(ROOM) ? room.getConfiguration().getProperties() : room.getMinigame().getProperties();
    }
}
