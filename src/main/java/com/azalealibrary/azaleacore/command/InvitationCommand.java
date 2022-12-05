package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.AzaCommand;
import com.azalealibrary.azaleacore.command.core.AzaleaCommand;
import com.azalealibrary.azaleacore.command.core.CommandConfigurator;
import com.azalealibrary.azaleacore.command.core.CommandDescriptor;

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
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(INVITE, WITHDRAW, ACCEPT));
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && !arguments.is(0, ACCEPT), (sender, arguments) -> AzaleaConfiguration.getInstance().getServerLobby().getPlayers().stream().map(Player::getDisplayName).toList());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, ACCEPT), (sender, arguments) -> PlaygroundManager.getInstance().getEntries().entrySet().stream().filter(entry -> entry.getValue().getInvitations().contains(Bukkit.getPlayer(arguments.get(1)))).map(Map.Entry::getKey).toList());
//        configurator.completeWhen((sender, arguments) -> arguments.size() == 3 && !arguments.is(0, ACCEPT), (sender, arguments) -> PlaygroundManager.getInstance().getKeys());
//        configurator.executeWhen((sender, arguments) -> arguments.size() == 3 && !arguments.is(0, ACCEPT), this::execute);
//        configurator.executeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, ACCEPT), this::accept);
    }

//    private Message execute(CommandSender sender, Arguments arguments) {
//        String action = arguments.matchesAny(0, "action", INVITE, WITHDRAW);
//        Player player = arguments.find(1, "player", input -> sender.getServer().getPlayer(input));
//        Room getPlayground = arguments.find(2, "getPlayground", AzaleaRoomApi.getInstance()::get);
//
//        String playerName = TextUtil.getName(player);
//        String roomName = TextUtil.getName(getPlayground);
//
//        if (!getPlayground.getConfiguration().joinWithInvitation()) {
//            throw new AzaleaException("No invitations required for getPlayground " + roomName + "!");
//        }
//
//        if (action.equals(INVITE)) {
//            if (getPlayground.getInvitations().contains(player)) {
//                throw new AzaleaException(playerName + " has already been invited to getPlayground " + roomName + ".");
//            }
//
//            Broadcaster broadcaster = getPlayground.getBroadcaster();
//            getPlayground.getInvitations().add(player);
//            broadcaster.toRoom(ChatMessage.announcement("Invited " + playerName + " to getPlayground " + roomName));
//
//            TextComponent message = new TextComponent("To accept the invitation, click ");
//            TextComponent invitation = new TextComponent(ChatColor.YELLOW + "here" + ChatColor.RESET);
//            invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Accept invitation?")));
//            invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + getName() + " " + ACCEPT + " " + roomName));
//            message.addExtra(invitation);
//            message.addExtra(".");
//
//            broadcaster.send(player, ChatMessage.announcement("You have been invited to getPlayground " + roomName + "."));
//            broadcaster.send(player, new ChatMessage(message, ChatMessage.LogType.ANNOUNCEMENT));
//        } else if (action.equals(WITHDRAW)) {
//            if (!getPlayground.getInvitations().contains(player)) {
//                throw new AzaleaException(playerName + " has no pending invitation for getPlayground " + roomName + ".");
//            }
//
//            getPlayground.getInvitations().remove(player);
//            getPlayground.getBroadcaster().toRoom(ChatMessage.announcement("Withdrawn invitation for " + playerName + " to getPlayground " + roomName + "."));
//        }
//        return null;
//    }
//
//    private Message accept(CommandSender sender, Arguments arguments) {
//        Room getPlayground = arguments.find(1, "getPlayground", AzaleaRoomApi.getInstance()::get);
//        getPlayground.addPlayer((Player) sender);
//        return null;
//    }
}
