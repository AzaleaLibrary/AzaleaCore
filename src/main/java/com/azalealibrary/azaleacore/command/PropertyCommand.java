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

@AzaCommand(name = "!property")
public class PropertyCommand extends AzaleaCommand {

    private static final String SET = "set";
    private static final String RESET = "reset";
    private static final String INFO = "info";

    public PropertyCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> {
            Room room = arguments.find(0, "room", AzaleaRoomApi.getInstance()::get);
            List<Property<?>> properties = room.getMinigame().getProperties();
            return properties.stream().map(Property::getName).toList();
        });
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3, (sender, arguments) -> List.of(SET, RESET, INFO));
        configurator.completeWhen((sender, arguments) -> arguments.get(2).equals(SET), (sender, arguments) -> {
            Room room = arguments.find(0, "room", AzaleaRoomApi.getInstance()::get);
            List<Property<?>> properties = room.getMinigame().getProperties();
            Optional<Property<?>> property = properties.stream()
                    .filter(p -> p.getName().equals(arguments.get(1)))
                    .findFirst();
            return property.isPresent() ? property.get().suggest(sender, new Arguments(arguments.getCommand(), arguments.subList(3, arguments.size()))) : List.of();
        });
        configurator.executeWhen((sender, arguments) -> true, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        Room room = arguments.find(0, "room", AzaleaRoomApi.getInstance()::get);
        Property<?> property = arguments.find(1, "property", input -> room.getMinigame().getProperties().stream().filter(p -> p.getName().equals(input)).findFirst().orElse(null));
        String action = arguments.matchesAny(2, "action", SET, RESET, INFO);

        if (action.equals(SET)) {
            property.set(sender, new Arguments(arguments.getCommand(), arguments.subList(3, arguments.size())));
            return ChatMessage.success("Property '" + property.getName() + "' updated.");
        } else if (action.equals(RESET)) {
            property.reset();
            return ChatMessage.success("Property '" + property.getName() + "' has been reset to its initial value.");
        } else {
            return ChatMessage.success("Property: " + property);
        }
    }
}
