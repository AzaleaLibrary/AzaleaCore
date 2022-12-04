package com.azalealibrary.azaleacore.command.core;

import org.bukkit.command.CommandSender;

public interface Executor {
    /**
     * Called when command is issued by the <b>sender</b> with given <b>arguments</b>.
     *
     * @param sender Command issuer. (ei. player, via terminal).
     * @param arguments Command arguments provided when triggering command.
     */
    void execute(CommandSender sender, Arguments arguments);
}
