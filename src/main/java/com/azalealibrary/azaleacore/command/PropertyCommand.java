package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.api.core.MinigameProperty;
import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.configuration.Property;
import com.azalealibrary.azaleacore.room.MinigameRoom;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import java.util.List;
import java.util.Optional;

@Commands(@Command(name = PropertyCommand.NAME))
public class PropertyCommand extends AzaleaCommand {

    protected static final String NAME = "!property";

    private static final String SET = "set";
    private static final String RESET = "reset";

    public PropertyCommand(JavaPlugin plugin) {
        super(plugin, NAME);
        completeWhen(arguments -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getRooms().stream().map(MinigameRoom::getName).toList());
        completeWhen(arguments -> arguments.size() == 2, (sender, arguments) -> {
            MinigameRoom room = arguments.parse(0, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().getRoom(input));
            List<MinigameProperty<?>> properties = room.getMinigame().getProperties();
            return properties.stream().map(Property::getConfigName).toList();
        });
        completeWhen(arguments -> arguments.size() == 3, (sender, arguments) -> List.of(SET, RESET));
        completeWhen(arguments -> arguments.size() == 4, (sender, arguments) -> {
            MinigameRoom room = arguments.parse(0, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().getRoom(input));
            List<MinigameProperty<?>> properties = room.getMinigame().getProperties();
            Optional<MinigameProperty<?>> property = properties.stream()
                    .filter(p -> p.getConfigName().equals(arguments.get(1)))
                    .findFirst();
            return property.isPresent() ? property.get().suggest((Player) sender) : List.of();
        });
        executeWhen(arguments -> true, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        MinigameRoom room = arguments.parse(0, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().getRoom(input));
        MinigameProperty<?> property = arguments.parse(1, "Could not find property '%s'.", input -> room.getMinigame().getProperties().stream()
                .filter(p -> p.getConfigName().equals(input))
                .findFirst().orElse(null));
        String action = arguments.matching(2, SET, RESET);

        switch (action) {
            case SET -> property.set((Player) sender, arguments.subList(3, arguments.size()).toArray(new String[0]));
            case RESET -> property.reset();
        }
        return success("Property '" + property.getConfigName() + "' " + action + ".");
    }
}
