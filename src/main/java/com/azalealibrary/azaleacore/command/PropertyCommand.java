package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.configuration.MinigameProperty;
import com.azalealibrary.azaleacore.api.configuration.Property;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.generator.WorldInfo;
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
        String worldInput = params.get(0);
        World world = Bukkit.getWorld(worldInput);
        if (world == null) {
            return notFound("world", worldInput);
        }

        MinigameController<?, ?> controller = AzaleaApi.getMinigameRooms().get(world);

        String propertyInput = params.get(1);
        Optional<MinigameProperty<?>> property = controller.getMinigame().getProperties().stream()
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
        return success("Property '" + propertyInput + "' reset with '" + property.get().getDefault() + "'.");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return AzaleaApi.getMinigameRooms().keySet().stream().map(WorldInfo::getName).toList();
        } else {
            World world = Bukkit.getWorld(params.get(0));

            if (world != null) {
                MinigameController<?, ?> controller = AzaleaApi.getMinigameRooms().get(world);

                if (controller != null) {
                    List<MinigameProperty<?>> properties = controller.getMinigame().getProperties();

                    if (params.size() == 2) {
                        return properties.stream().map(Property::getConfigName).toList();
                    } else if (params.size() == 3) {
                        return List.of(SET, RESET);
                    } else if (params.size() == 4 && !params.get(2).equals(RESET)) {
                        Optional<MinigameProperty<?>> property = properties.stream()
                                .filter(p -> p.getConfigName().equals(params.get(1)))
                                .findFirst();

                        if (property.isPresent()) {
                            return property.get().suggest((Player) sender);
                        }
                    }
                }
            }
        }
        return List.of();
    }
}
