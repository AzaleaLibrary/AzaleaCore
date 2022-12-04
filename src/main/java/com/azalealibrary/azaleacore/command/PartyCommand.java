package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.manager.PartyManager;
import com.azalealibrary.azaleacore.party.Party;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

@AzaCommand(name = "!party")
public class PartyCommand extends AzaleaCommand {

    private static final String CREATE = "create";
    private static final String DISBAND = "disband";
    // TODO - invitations
    // TODO - configuration

    public PartyCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> List.of(CREATE, DISBAND));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, CREATE), (sender, arguments) -> List.of("<name>"));
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2 && arguments.is(0, DISBAND), (sender, arguments) -> PartyManager.getInstance().getKeys());
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2, this::execute);
    }

    private void execute(CommandSender sender, Arguments arguments) {
        String action = arguments.matchesAny(0, "action", CREATE, DISBAND);

        if (Objects.equals(action, CREATE)) {
            String name = arguments.notMissing(1, "name");
            Party party = PartyManager.getInstance().create(name, (Player) sender);
            ChatMessage.info("Party " + TextUtil.getName(party) + " created.").post("AZA", sender);
        } else {
            Party party = arguments.find(1, "party", PartyManager.getInstance()::get);
            PartyManager.getInstance().delete(party);
        }
    }
}
