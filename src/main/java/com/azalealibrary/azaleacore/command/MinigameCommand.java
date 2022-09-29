package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.generator.WorldInfo;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import javax.annotation.Nonnull;
import java.util.List;

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
            return invalid("action", actionInput);
        }

        String minigameInput = params.get(1);
        if (!AzaleaApi.REGISTERED_MINIGAME.containsKey(minigameInput)) {
            return notFound("minigame", minigameInput);
        }

        String worldInput = params.get(2);
        World world = Bukkit.getWorld(worldInput);
        if (world == null) {
            return notFound("world", worldInput);
        }

        Minigame<?> minigame = AzaleaApi.REGISTERED_MINIGAME.get(minigameInput).create(world);
        MinigameController<?, ?> controller = AzaleaApi.createController(minigame);

        Message message = params.size() > 3 ? new ChatMessage(String.join(" ", params.subList(3, params.size()))) : null;
        switch (actionInput) {
            case START -> controller.start(world.getPlayers(), message);
            case END -> controller.stop(message);
            case RESTART -> controller.restart(message);
        }
        return success(minigameInput + " " + actionInput.toLowerCase() + "ed in world '" + worldInput + "'.");
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return List.of(START, END, RESTART);
        } else  if (params.size() == 2) {
            return AzaleaApi.REGISTERED_MINIGAME.keySet().stream().toList();
        } else if (params.size() == 3) {
            return Bukkit.getWorlds().stream().map(WorldInfo::getName).toList();
        }
        return List.of();
    }
}
