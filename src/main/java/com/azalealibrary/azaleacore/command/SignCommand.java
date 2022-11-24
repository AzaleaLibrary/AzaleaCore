package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.foundation.teleport.SignTicker;
import com.azalealibrary.azaleacore.room.Room;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AzaCommand(name = "!sign")
public class SignCommand extends AzaleaCommand {

    private static final String ADD = "add";
    private static final String REMOVE = "remove";

    private static final String ROOM = "room";
    private static final String LOBBY = "lobby";

    public SignCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(ADD, REMOVE));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, ADD), (sender, arguments) -> List.of(ROOM, LOBBY));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && arguments.is(1, ROOM), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.size() == 3 && arguments.is(1, ROOM), this::addRoomSign);
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(1, LOBBY), this::addLobbySign);
        configurator.executeWhen((sender, arguments) -> arguments.size() == 1 && arguments.is(0, REMOVE), this::removeSign);
    }

    private Message addRoomSign(CommandSender sender, Arguments arguments) {
        Room room = arguments.find(2, "room", AzaleaRoomApi.getInstance()::get);
        SignTicker ticker = SignTicker.getInstance();
        Location location = getTargetSign(sender);

        if (!ticker.isTracked(location)) {
            ticker.addRoomSign(location, room);
            return ChatMessage.success("Added sign to room '" + room.getName() + "'.");
        }
        return ChatMessage.warn("Sign already in use.");
    }

    private Message addLobbySign(CommandSender sender, Arguments arguments) {
        SignTicker ticker = SignTicker.getInstance();
        Location location = getTargetSign(sender);

        if (!ticker.isTracked(location)) {
            ticker.addLobbySign(location);
            return ChatMessage.success("Added sign to lobby.");
        }
        return ChatMessage.warn("Sign already in use.");
    }

    private Message removeSign(CommandSender sender, Arguments arguments) {
        Location location = getTargetSign(sender);
        SignTicker ticker = SignTicker.getInstance();

        if (ticker.isTracked(location)) {
            ticker.removeSign(location);
            return ChatMessage.success("Removed sign.");
        }
        return ChatMessage.warn("Sign not in use.");
    }

    private static Location getTargetSign(CommandSender sender) {
        if (sender instanceof Player player) {
            Block target = player.getTargetBlock(null, 10);

            if (target.getState() instanceof Sign) {
                return target.getLocation();
            }
            throw new AzaleaException("Target block is not a sign.");
        }
        throw new AzaleaException("Command issuer not a player.");
    }
}
