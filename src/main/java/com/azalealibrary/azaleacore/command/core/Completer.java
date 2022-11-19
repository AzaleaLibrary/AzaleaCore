package com.azalealibrary.azaleacore.command.core;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Completer {
    /**
     * Suggest a list of possible arguments given the <b>sender</b> and the current <b>arguments</b>.
     *
     * @param sender Command issuer. (ei. player, via terminal).
     * @param arguments Command arguments provided when triggering command.
     * @return List of possible arguments.
     */
    List<String> suggest(CommandSender sender, Arguments arguments);
}
