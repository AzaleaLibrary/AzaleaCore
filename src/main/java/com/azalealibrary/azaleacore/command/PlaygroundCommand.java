package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaPlaygroundApi;
import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import java.util.List;

@Commands(@Command(name = PlaygroundCommand.NAME))
public class PlaygroundCommand extends AzaleaCommand {

    protected static final String NAME = "!playground";

    private static final String CREATE = "create";
    private static final String DELETE = "delete";

    public PlaygroundCommand(JavaPlugin plugin) {
        super(plugin, NAME);
        completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, DELETE));
        completeWhen((sender, arguments) -> arguments.get(0).equals(DELETE), (sender, arguments) -> AzaleaPlaygroundApi.getInstance().getKeys());
        executeWhen((sender, arguments) -> arguments.get(0).equals(CREATE), this::create);
        executeWhen((sender, arguments) -> arguments.get(0).equals(DELETE), this::delete);
    }

    private Message create(CommandSender sender, Arguments arguments) {
        String name = arguments.missing(1);

        if (AzaleaPlaygroundApi.getInstance().get(name) != null) {
            return failure("Playground '" + name + "' already exists.");
        }

        if (sender instanceof Player player) {
            List<String> tags = arguments.subList(2, arguments.size());
            Playground playground = new Playground(name, player.getWorld(), player.getLocation(), tags);
            AzaleaPlaygroundApi.getInstance().add(name, playground);

            StringBuilder builder = new StringBuilder("Playground '" + name + "' created");
            if (!tags.isEmpty()) builder.append(" with tags: ").append(String.join(" ", tags));
            builder.append(".");

            return success(builder.toString());
        }
        return failure("Command issuer not a player.");
    }

    private Message delete(CommandSender sender, Arguments arguments) {
        Playground playground = arguments.parse(1, "Could not find playground '%s'.", input -> AzaleaPlaygroundApi.getInstance().get(input));
        AzaleaPlaygroundApi.getInstance().remove(playground);

        return success("Deleted '" + playground.getName() + "' playground.");
    }
}
