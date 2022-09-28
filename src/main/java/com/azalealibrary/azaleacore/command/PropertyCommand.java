package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.configuration.MinigameProperty;
import com.azalealibrary.azaleacore.api.configuration.Property;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import org.bukkit.ChatColor;
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

    public PropertyCommand(JavaPlugin plugin) {
        super(plugin, NAME);
    }

    @Override
    protected Message execute(@Nonnull CommandSender sender, List<String> params) {
        return new ChatMessage(params.toString());
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return AzaleaApi.MINIGAMES.values().stream().map(controller -> controller.getMinigame().getName()).toList();
        } else {
            String minigameInput = params.get(0);
            Optional<MinigameController<?, ?>> controller = AzaleaApi.MINIGAMES.values().stream()
                    .filter(c -> c.getMinigame().getName().equals(minigameInput))
                    .findFirst();

            if (controller.isPresent()) {
                List<MinigameProperty<?>> properties = controller.get().getMinigame().getProperties();

                if (params.size() == 2) {
                    return properties.stream().map(Property::getName).toList();
                } else {
                    String propertyInput = params.get(1);
                    Optional<MinigameProperty<?>> property = properties.stream()
                            .filter(p -> p.getName().equals(propertyInput))
                            .findFirst();

                    if (property.isPresent()) {
                        return property.get().suggest((Player) sender);
                    } else {
                        return List.of(ChatColor.RED + "Could not find '" + propertyInput + "' property.");
                    }
                }
            } else {
                return List.of(ChatColor.RED + "Could not find '" + minigameInput + "' minigame.");
            }
        }
    }
}
