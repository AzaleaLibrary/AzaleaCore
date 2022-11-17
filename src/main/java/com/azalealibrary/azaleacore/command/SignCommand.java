package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.room.MinigameRoom;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.command.Command;
import org.bukkit.plugin.java.annotation.command.Commands;

import java.util.List;

@Commands(@Command(name = SignCommand.NAME))
public class SignCommand extends AzaleaCommand {

    protected static final String NAME = "!sign";

    private static final String WORLD = "world";
    private static final String LOBBY = "lobby";

    public SignCommand(JavaPlugin plugin) {
        super(plugin, NAME);
        completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> List.of(WORLD, LOBBY));
        executeWhen((sender, arguments) -> arguments.size() == 2, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        MinigameRoom room = arguments.parse(0, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().get(input));
        String action = arguments.matching(1, WORLD, LOBBY);

        if (sender instanceof Player player) {
            Block target = player.getTargetBlock(null, 10);

            if (target.getState() instanceof Sign sign) {
                Location location = target.getLocation();
                List<Location> signs = action.equals(WORLD)
                        ? room.getSignTicker().getToWorldSigns()
                        : room.getSignTicker().getToLobbySigns();

                for (int i = 0; i < 4; i++) {
                    sign.setLine(i, "");
                }
                sign.update();

                if (!signs.contains(location)) {
                    signs.add(location);
                    return success("Added sign to " + room.getName() + " " + action + ".");
                } else {
                    if (signs.remove(location)) {
                        return success("Removed sign to " + room.getName() + " " + action + ".");
                    }
                    return warn("Could not remove sign...");
                }
            }
            return failure("Target block is not a sign.");
        }
        return failure("Command issuer not a player.");
    }
}
