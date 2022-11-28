package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.api.core.MinigameItem;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.foundation.registry.AzaleaRegistry;
import com.azalealibrary.azaleacore.foundation.registry.MinigameIdentifier;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@AzaCommand(name = "!item")
public class ItemCommand extends AzaleaCommand {

    public ItemCommand(CommandDescriptor descriptor) {
        super(descriptor);
    }

    @Override
    protected void configure(CommandConfigurator configurator) {
        configurator.completeWhen((sender, arguments) -> arguments.size() == 1, (sender, arguments) -> AzaleaRegistry.ITEM.getKeys().stream().map(MinigameIdentifier.Tag::toString).toList());
        configurator.completeWhen((sender, arguments) -> arguments.size() == 2, (sender, arguments) -> ((Player) sender).getWorld().getPlayers().stream().map(Player::getDisplayName).toList());
        configurator.executeWhen((sender, arguments) -> arguments.size() == 2, this::execute);
    }

    private Message execute(CommandSender sender, Arguments arguments) {
        MinigameItem item = arguments.find(0, "minigame item", input -> AzaleaRegistry.ITEM.get(new MinigameIdentifier.Tag(input)));
        Player player = arguments.find(1, "player", input -> sender.getServer().getPlayer(input));

        player.getInventory().addItem(item.getItemStack());
        String name = ChatColor.YELLOW + player.getDisplayName() + ChatColor.RESET;

        return ChatMessage.info("Gave " + name + " item " + item.getItemStack().getItemMeta().getDisplayName() + ".");
    }
}
