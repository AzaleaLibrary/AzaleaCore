package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.api.broadcast.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;
import org.bukkit.plugin.java.annotation.permission.Permission;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@Commands(@Command(name = AzaleaCommand.COMMAND_PREFIX + "property"))
@Permission(name = "admin", defaultValue = PermissionDefault.OP)
public class PropertyCommand extends AzaleaCommand {

    public PropertyCommand(JavaPlugin plugin) {
        super("property", plugin);
    }

    @Override
    protected Message execute(@Nonnull CommandSender sender, String[] params) {
        return new ChatMessage(Arrays.toString(params));
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String option, String[] params) {
//        if (params.length == 0) {
//            return AzaleaApi.MINIGAMES.values().stream().map(controller -> controller.getMinigame().getName()).toList();
//        } else {
//            return List.of();
//        }
//        AzaleaApi.getInstance().getMinigames().values().stream()
//                .filter((c -> c.getCurrentRound().getPlayers().contains(player)))
//                .findFirst()
//                .ifPresent(controller -> controller.getCurrentRound().getBroadcaster().broadcast(message));
        return List.of();
    }
}
