package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

import static com.azalealibrary.azaleacore.command.MinigameCommand.NAME;

@Commands(@Command(name = NAME))
public class MinigameCommand extends AzaleaCommand {

    protected static final String NAME = AzaleaCommand.COMMAND_PREFIX + "minigame";
    public static final String START = "START";
    public static final String END = "END";
    public static final String RESTART = "RESTART";

    public MinigameCommand(JavaPlugin plugin) {
        super(plugin, NAME);
    }

    @Override
    protected Message execute(@Nonnull CommandSender sender, List<String> params) {
        String actionInput = params.get(0);

        if (!List.of(START, END, RESTART).contains(actionInput)) {
            return new ChatMessage(ChatColor.RED + "Invalid action provided: '" + actionInput + "'.");
        }

        String minigameInput = params.get(1);
        Optional<MinigameController<?, ?>> controller = AzaleaApi.MINIGAMES.values().stream()
                .filter(c -> c.getMinigame().getName().equals(minigameInput))
                .findFirst();

        if (controller.isPresent()) {
            String worldInput = params.get(2);
            Optional<World> world = Optional.ofNullable(Bukkit.getWorld(worldInput));

            if (world.isPresent()) {

                Message message = params.size() > 3 ? new ChatMessage(String.join(" ", params.subList(3, params.size()))) : null;
                switch (actionInput) {
                    case START -> controller.get().start(world.get().getPlayers(), message);
                    case END -> controller.get().stop(message);
                    case RESTART -> controller.get().restart(message);
                }

                return new ChatMessage(ChatColor.GREEN + controller.get().getMinigame().getName() + " " + actionInput.toLowerCase() + "ed.");
            } else {
                return new ChatMessage(ChatColor.RED + "Could not find '" + worldInput + "' world.");
            }
        } else {
            return new ChatMessage(ChatColor.RED + "Could not find '" + minigameInput + "' minigame.");
        }
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return List.of(START, END, RESTART);
        } else  if (params.size() == 2) {
            return AzaleaApi.MINIGAMES.values().stream().map(controller -> controller.getMinigame().getName()).toList();
        } else if (params.size() == 3) {
            return Bukkit.getWorlds().stream().map(WorldInfo::getName).toList();
        }
        return List.of();
    }
}
