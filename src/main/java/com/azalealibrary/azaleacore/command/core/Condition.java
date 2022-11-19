package com.azalealibrary.azaleacore.command.core;

import org.bukkit.command.CommandSender;

public interface Condition {
    /**
     * Condition that needs to be met given the <b>sender</b> and <b>arguments</b>.
     *
     * @param sender Command issuer. (ei. player, via terminal).
     * @param arguments Command arguments provided when triggering command.
     * @return Whether implementer is eligible to be triggered.
     */
    boolean applyWhen(CommandSender sender, Arguments arguments);
}
