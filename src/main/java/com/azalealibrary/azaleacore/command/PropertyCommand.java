package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.MinigameProperty;
import com.azalealibrary.azaleacore.configuration.Property;
import com.azalealibrary.azaleacore.room.MinigameRoom;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Commands(@Command(name = PropertyCommand.NAME))
public class PropertyCommand extends AzaleaCommand {

    protected static final String NAME = "!property";

    private static final String SET = "SET";
    private static final String RESET = "RESET";

    public PropertyCommand(JavaPlugin plugin) {
        super(plugin, NAME);
    }

    @Override
    protected @Nullable Message execute(@Nonnull CommandSender sender, List<String> params) {
        String roomInput = params.get(0);
        String propertyInput = params.get(1);
        String actionInput = params.get(2);

        MinigameRoom room = AzaleaApi.getInstance().getRoom(roomInput);
        if (room == null) {
            return notFound("room", roomInput);
        }

        Optional<MinigameProperty<?>> property = room.getMinigame().getProperties().stream()
                .filter(p -> p.getConfigName().equals(propertyInput))
                .findFirst();
        if (property.isEmpty()) {
            return notFound("property", propertyInput);
        }

        if (!Objects.equals(actionInput, SET) && !Objects.equals(actionInput, RESET)) {
            return invalid("action", actionInput);
        }

        switch (actionInput) {
            case SET -> property.get().set((Player) sender, params.subList(3, params.size()).toArray(new String[0]));
            case RESET -> property.get().reset();
        }
        return success("Property '" + propertyInput + "' " + actionInput.toLowerCase() + ".");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return AzaleaApi.getInstance().getRooms().stream().map(MinigameRoom::getName).toList();
        } else if (params.size() == 3) {
            return List.of(SET, RESET);
        } else {
            MinigameRoom room = AzaleaApi.getInstance().getRoom(params.get(0));

            if (room != null) {
                List<MinigameProperty<?>> properties = room.getMinigame().getProperties();

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
