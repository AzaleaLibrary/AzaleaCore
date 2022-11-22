package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.api.core.ConfigurableProperty;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.foundation.configuration.Property;
import com.azalealibrary.azaleacore.room.Room;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

@AzaCommand(name = "!property")
public class PropertyCommand extends AzaleaCommand {

    private static final String SET = "set";
    private static final String RESET = "reset";

    public PropertyCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> {
            Room room = arguments.parse(0, "Could not find room '%s'.", AzaleaRoomApi.getInstance()::get);
            List<ConfigurableProperty<?>> properties = room.getMinigame().getProperties();
            return properties.stream().map(Property::getConfigName).toList();
        });
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3, (sender, arguments) -> List.of(SET, RESET));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 4 && arguments.get(2).equals(SET), (sender, arguments) -> {
            Room room = arguments.parse(0, "Could not find room '%s'.", AzaleaRoomApi.getInstance()::get);
            List<ConfigurableProperty<?>> properties = room.getMinigame().getProperties();
            Optional<ConfigurableProperty<?>> property = properties.stream()
                    .filter(p -> p.getConfigName().equals(arguments.get(1)))
                    .findFirst();
            return property.isPresent() ? property.get().suggest((Player) sender) : List.of();
        });
        configurator.executeWhen((sender, arguments) -> true, this::execute);

    }

    private Message execute(CommandSender sender, Arguments arguments) {
        Room room = arguments.parse(0, "Could not find room '%s'.", AzaleaRoomApi.getInstance()::get);
        ConfigurableProperty<?> property = arguments.parse(1, "Could not find property '%s'.", input -> room.getMinigame().getProperties().stream()
                .filter(p -> p.getConfigName().equals(input))
                .findFirst().orElse(null));
        String action = arguments.matching(2, SET, RESET);

        switch (action) {
            case SET -> property.set((Player) sender, new Arguments(arguments.getCommand(), arguments.subList(3, arguments.size())));
            case RESET -> property.reset();
        }
        return ChatMessage.success("Property '" + property.getConfigName() + "' " + action + ".");
    }
}
