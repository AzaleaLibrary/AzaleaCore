package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.MinigameProperty;
import com.azalealibrary.azaleacore.broadcast.message.Message;
import com.azalealibrary.azaleacore.configuration.Property;
import com.azalealibrary.azaleacore.minigame.MinigameRoom;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

@Commands(@Command(name = PropertyCommand.NAME))
public class PropertyCommand extends AzaleaCommand {

    protected static final String NAME = AzaleaCommand.COMMAND_PREFIX + "property";

    private static final String SET = "SET";
    private static final String RESET = "RESET";

    public PropertyCommand(JavaPlugin plugin) {
        super(plugin, NAME);
    }

    @Override
    protected Message execute(@Nonnull CommandSender sender, List<String> params) {
        String roomInput = params.get(0);
        Optional<MinigameRoom<?, ?>> room = AzaleaApi.getInstance().getMinigameRooms().stream()
                .filter(r -> r.getName().equals(roomInput))
                .findFirst();
        if (room.isEmpty()) {
            return notFound("room", roomInput);
        }

        String propertyInput = params.get(1);
        Optional<MinigameProperty<?>> property = room.get().getMinigame().getProperties().stream()
                .filter(p -> p.getConfigName().equals(propertyInput))
                .findFirst();
        if (property.isEmpty()) {
            return notFound("property", propertyInput);
        }

        String actionInput = params.get(2);
        switch (actionInput) {
            case SET -> property.get().set((Player) sender, params.subList(3, params.size()).toArray(new String[0]));
            case RESET -> property.get().reset();
            default -> {
                return invalid("action", actionInput);
            }
        }
        return success("Property '" + propertyInput + "' " + actionInput.toLowerCase() + " with '" + property.get().get() + "'.");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return AzaleaApi.getInstance().getMinigameRooms().stream().map(MinigameRoom::getName).toList();
        } else if (params.size() == 3) {
            return List.of(SET, RESET);
        } else {
            Optional<MinigameRoom<?, ?>> room = AzaleaApi.getInstance().getMinigameRooms().stream()
                    .filter(r -> r.getName().equals(params.get(0)))
                    .findFirst();

            if (room.isPresent()) {
                List<MinigameProperty<?>> properties = room.get().getMinigame().getProperties();

                if (params.size() == 2) {
                    return properties.stream().map(Property::getConfigName).toList();
                } else if (params.size() == 4 && params.get(2).equals(SET)) {
                    Optional<MinigameProperty<?>> property = properties.stream()
                            .filter(p -> p.getConfigName().equals(params.get(1)))
                            .findFirst();

                    if (property.isPresent()) {
                        return property.get().suggest((Player) sender);
                    }
                }
            }
        }
        return List.of();
    }
}
