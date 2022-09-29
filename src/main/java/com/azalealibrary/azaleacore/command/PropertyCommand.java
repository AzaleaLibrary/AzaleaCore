package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.configuration.MinigameProperty;
import com.azalealibrary.azaleacore.api.configuration.Property;
import com.azalealibrary.azaleacore.minigame.MinigameController;
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
        String minigameInput = params.get(0);
        Optional<MinigameController<?, ?>> controller = AzaleaApi.RUNNING_MINIGAMES.values().stream()
                .filter(c -> c.getMinigame().getConfigName().equals(minigameInput))
                .findFirst();
        if (controller.isEmpty()) {
            return notFound("minigame", minigameInput);
        }

        String propertyInput = params.get(1);
        Optional<MinigameProperty<?>> property = controller.get().getMinigame().getProperties().stream()
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
            return AzaleaApi.RUNNING_MINIGAMES.values().stream().map(c -> c.getMinigame().getConfigName()).toList();
        } else {
            Optional<MinigameController<?, ?>> controller = AzaleaApi.RUNNING_MINIGAMES.values().stream()
                    .filter(c -> c.getMinigame().getConfigName().equals(params.get(0)))
                    .findFirst();

            if (controller.isPresent()) {
                List<MinigameProperty<?>> properties = controller.get().getMinigame().getProperties();

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
        return List.of();
    }
}
