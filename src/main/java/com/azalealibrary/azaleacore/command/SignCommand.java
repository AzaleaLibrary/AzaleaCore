package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.command.core.AzaleaCommand;
import com.azalealibrary.azaleacore.command.core.CommandHandler;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AzaleaCommand(name = "!sign")
public class SignCommand {

    private static final String WORLD = "world";
    private static final String LOBBY = "lobby";

    public SignCommand(CommandHandler.Builder builder) {
        builder.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        builder.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> List.of(WORLD, LOBBY));
        builder.executeWhen((sender, arguments) -> arguments.size() == 2, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        Room room = arguments.parse(0, "Could not find room '%s'.", input -> AzaleaRoomApi.getInstance().get(input));
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
                    return ChatMessage.success("Added sign to " + room.getName() + " " + action + ".");
                } else {
                    if (signs.remove(location)) {
                        return ChatMessage.success("Removed sign to " + room.getName() + " " + action + ".");
                    }
                    return ChatMessage.warn("Could not remove sign...");
                }
            }
            return ChatMessage.failure("Target block is not a sign.");
        }
        return ChatMessage.failure("Command issuer not a player.");
    }
}
