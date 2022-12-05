package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.message.Message;
import com.azalealibrary.azaleacore.manager.PartyManager;
import com.azalealibrary.azaleacore.manager.PlaygroundManager;
import com.azalealibrary.azaleacore.party.Party;
import com.azalealibrary.azaleacore.playground.Playground;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@AzaCommand(name = "!teleport")
public class TeleportCommand extends AzaleaCommand {

    private static final String LOBBY = "lobby";
    private static final String PLAYGROUND = "playground";

    public TeleportCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(LOBBY, PLAYGROUND));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, PLAYGROUND), (sender, arguments) -> PlaygroundManager.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.size() == 1 && arguments.is(0, LOBBY), this::toLobby);
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, PLAYGROUND), this::toPlayground);
    }

    private Message toLobby(CommandSender sender, Arguments arguments) {
        if (sender instanceof Player player) {
            player.teleport(AzaleaConfiguration.getInstance().getServerLobby().getSpawnLocation());
            Party party = PartyManager.getInstance().get(player);

            if (party != null) {
                party.broadcast(ChatMessage.info(TextUtil.getName(player) + " left the playground."));
            }
        }
        return null;
    }

    private Message toPlayground(CommandSender sender, Arguments arguments) {
        if (sender instanceof Player player) {
            Playground playground = arguments.find(1, "playground", PlaygroundManager.getInstance()::get);
            player.teleport(playground.getWorld().getSpawnLocation());

            if (playground.hasParty()) {
                playground.getParty().broadcast(ChatMessage.info(TextUtil.getName(player) + " left the playground."));
            }
        }
        return null;
    }
}
