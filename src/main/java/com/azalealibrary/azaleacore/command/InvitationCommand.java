package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.room.Room;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AzaCommand(name = "!invitation")
public class InvitationCommand extends AzaleaCommand {

    private static final String INVITE = "invite";
    private static final String WITHDRAW = "withdraw";
    private static final String ACCEPT = "accept";

    public InvitationCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(INVITE, WITHDRAW, ACCEPT));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && !arguments.is(0, ACCEPT), (sender, arguments) -> AzaleaConfiguration.getInstance().getServerLobby().getPlayers().stream().map(Player::getDisplayName).toList());
        configurator.completeWhen((sender, arguments) -> (arguments.size() == 3 && !arguments.is(0, ACCEPT)) || (arguments.size() == 2 && arguments.is(0, ACCEPT)), (sender, arguments) -> AzaleaRoomApi.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.size() == 3 && !arguments.is(0, ACCEPT), this::execute);
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, ACCEPT), this::accept);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        String action = arguments.matchesAny(0, "action", INVITE, WITHDRAW);
        Player player = arguments.find(1, "player", input -> sender.getServer().getPlayer(input));
        Room room = arguments.find(2, "room", AzaleaRoomApi.getInstance()::get);
        String playerName = ChatColor.YELLOW + player.getDisplayName() + ChatColor.RESET; // TODO - TextUtil

        if (!room.getConfiguration().joinWithInvitation()) {
            throw new AzaleaException("No invitations required for room " + room.getName() + "!");
        }

        if (action.equals(INVITE)) {
            if (room.getInvitations().contains(player)) {
                throw new AzaleaException(playerName + " has already been invited to room " + room.getName() + ".");
            }

            Broadcaster broadcaster = room.getBroadcaster();
            room.getInvitations().add(player);
            broadcaster.toRoom(ChatMessage.announcement("Invited " + playerName + " to room " + room.getName()));

            TextComponent message = new TextComponent("To accept the invitation, click ");
            TextComponent invitation = new TextComponent(ChatColor.YELLOW + "here" + ChatColor.RESET);
            invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Accept invitation?")));
            invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + getName() + " " + ACCEPT + " " + room.getName()));
            message.addExtra(invitation);
            message.addExtra(".");

            broadcaster.send(player, ChatMessage.announcement("You have been invited to room " + room.getName() + "."));
            broadcaster.send(player, new ChatMessage(message, ChatMessage.LogType.ANNOUNCEMENT));
        } else if (action.equals(WITHDRAW)) {
            if (!room.getInvitations().contains(player)) {
                throw new AzaleaException(playerName + " has no pending invitation for room " + room.getName() + ".");
            }

            room.getInvitations().remove(player);
            room.getBroadcaster().toRoom(ChatMessage.announcement("Withdrawn invitation for " + playerName + " to room " + room.getName() + "."));
        }
        return null;
    }

    private Message accept(CommandSender sender, Arguments arguments) {
        Room room = arguments.find(1, "room", AzaleaRoomApi.getInstance()::get);
        room.addPlayer((Player) sender);
        return null;
    }
}
