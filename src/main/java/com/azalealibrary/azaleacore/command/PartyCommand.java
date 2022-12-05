package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.manager.PartyManager;
import com.azalealibrary.azaleacore.party.Party;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PartyCommand extends CommandNode {

    public PartyCommand() {
        super("@party", new Create(), new Delete(), new Invitation(), new Broadcast(), new Configure());
    }

    private static final class Create extends CommandNode {

        public Create() {
            super("create");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1 ? List.of("<name>") : List.of();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            String name = arguments.notMissing(0, "name");
            Party party = PartyManager.getInstance().create(name, (Player) sender);
            ChatMessage.info("Party " + TextUtil.getName(party) + " created.").post("AZA", sender);
        }
    }

    private static final class Delete extends CommandNode {

        public Delete() {
            super("delete");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1 ? PartyManager.getInstance().getKeys() : List.of();
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            Party party = arguments.find(0, "party", PartyManager.getInstance()::get);
            PartyManager.getInstance().delete(party);
        }
    }

    private static final class Invitation extends CommandNode {

        public Invitation() {
            super("invitation", new Create(), new Delete(), new Accept(), new PartyCommand.Create());
        }

        private static final class Create extends CommandNode {

            public Create() {
                super("create");
            }

            @Override
            public List<String> complete(CommandSender sender, Arguments arguments) {
                return arguments.size() == 1
                        ? PartyManager.getInstance().getKeys() : arguments.size() == 2
                        ? AzaleaConfiguration.getInstance().getServerLobby().getPlayers().stream().map(Player::getDisplayName).toList() : List.of();
            }

            @Override
            public void execute(CommandSender sender, Arguments arguments) {
                Party party = arguments.find(0, "party", PartyManager.getInstance()::get);
                Player player = arguments.find(1, "player", Bukkit.getServer()::getPlayer);
                party.invitePlayer(player);
            }
        }

        private static final class Delete extends CommandNode {

            public Delete() {
                super("delete");
            }

            @Override
            public List<String> complete(CommandSender sender, Arguments arguments) {
                if (arguments.size() == 1) {
                    return PartyManager.getInstance().getKeys();
                } else if (arguments.size() == 2) {
                    Party party = arguments.find(0, "party", PartyManager.getInstance()::get);
                    return party.getInvitations().stream().map(Player::getDisplayName).toList();
                }
                return List.of();
            }

            @Override
            public void execute(CommandSender sender, Arguments arguments) {
                Party party = arguments.find(0, "party", PartyManager.getInstance()::get);
                Player player = arguments.find(1, "player", Bukkit.getServer()::getPlayer);
                party.withdrawInvitation(player);
            }
        }

        private static final class Accept extends CommandNode {

            public Accept() {
                super("accept");
            }

//            @Override
//            public void execute(CommandSender sender, Arguments arguments) {
//                TextComponent message = new TextComponent("To accept the invitation, click ");
//                TextComponent invitation = new TextComponent(ChatColor.YELLOW + "here" + ChatColor.RESET);
//                invitation.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Accept invitation?")));
//                invitation.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/@party invitation accept " + ));
//                message.addExtra(invitation);
//                message.addExtra(".");
//
//                broadcaster.send(player, ChatMessage.announcement("You have been invited to getPlayground " + roomName + "."));
//                broadcaster.send(player, new ChatMessage(message, ChatMessage.LogType.ANNOUNCEMENT));
//            }
        }
    }

    private static final class Broadcast extends CommandNode {

        public Broadcast() {
            super("broadcast");
        }

        @Override
        public List<String> complete(CommandSender sender, Arguments arguments) {
            return arguments.size() == 1
                    ? PartyManager.getInstance().getKeys() : arguments.size() == 2
                    ? Arrays.stream(ChatMessage.LogType.values()).map(v -> v.name().toLowerCase()).toList() : List.of("<message>");
        }

        @Override
        public void execute(CommandSender sender, Arguments arguments) {
            Party party = arguments.find(0, "party", PartyManager.getInstance()::get);
            ChatMessage.LogType logType = arguments.find(1, "log type", input -> ChatMessage.LogType.valueOf(input.toUpperCase()));
            String input = String.join(" ", arguments.subList(2, arguments.size()));
            party.broadcast(new ChatMessage(input, logType));
        }
    }

    private static final class Configure extends ConfigureCommand {
        @Override
        protected List<String> completeConfigurable(CommandSender sender, Arguments arguments) {
            return PartyManager.getInstance().getKeys();
        }

        @Override
        protected @Nullable Configurable getConfigurable(String input) {
            return Optional.ofNullable(PartyManager.getInstance().get(input)).map(Party::getConfiguration).orElse(null);
        }
    }
}
