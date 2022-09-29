package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaApi;
import com.azalealibrary.azaleacore.api.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
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
import java.util.Objects;

import static com.azalealibrary.azaleacore.command.MinigameCommand.NAME;

@Commands(@Command(name = NAME))
public class MinigameCommand extends AzaleaCommand {

    protected static final String NAME = AzaleaCommand.COMMAND_PREFIX + "minigame";
    public static final String CREATE = "CREATE";
    public static final String START = "START";
    public static final String END = "END";
    public static final String RESTART = "RESTART";

    public MinigameCommand(JavaPlugin plugin) {
        super(plugin, NAME);
    }

    @Override
    protected Message execute(@Nonnull CommandSender sender, List<String> params) {
        String actionInput = params.get(0);
        if (!List.of(CREATE, START, END, RESTART).contains(actionInput)) {
            return invalid("action", actionInput);
        }

        String worldInput = params.get(1).split(":")[0];
        World world = Bukkit.getWorld(worldInput);
        if (world == null) {
            return notFound("world", worldInput);
        }

        if (Objects.equals(actionInput, CREATE)) {
            String minigameInput = params.get(2);
            AzaleaApi.MinigameProvider<?> provider = AzaleaApi.getInstance().getRegisteredMinigames().get(minigameInput);
            if (provider == null) {
                return notFound("minigame", minigameInput);
            }

            AzaleaApi.getInstance().createMinigameRoom(world, provider);

            return success("New '" + minigameInput + "' room created in world '" + worldInput + "'.");
        } else {
            MinigameController<?, ?> controller = AzaleaApi.getInstance().getMinigameRooms().get(world);
            if (controller == null) {
                return notFound("minigame room", worldInput);
            }

            Message message = params.size() > 3 ? new ChatMessage(String.join(" ", params.subList(3, params.size()))) : null;
            switch (actionInput) {
                case START -> controller.start(world.getPlayers(), message);
                case END -> controller.stop(message);
                case RESTART -> controller.restart(message);
            }
            return success(controller.getMinigame().getConfigName() + " " + actionInput.toLowerCase() + "ed in minigame room '" + worldInput + "'.");
        }
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, List<String> params) {
        if (params.size() == 1) {
            return List.of(CREATE, START, END, RESTART);
        } else if (params.size() == 2) {
            return params.get(0).equals(CREATE)
                    ? Bukkit.getWorlds().stream().map(WorldInfo::getName).toList()
                    : AzaleaApi.getInstance().getMinigameRooms().values().stream().map(MinigameController::getControllerName).toList();
        } else if (params.get(0).equals(CREATE) && params.size() == 3) {
            return AzaleaApi.getInstance().getRegisteredMinigames().keySet().stream().toList();
        }
        return List.of();
    }
}
