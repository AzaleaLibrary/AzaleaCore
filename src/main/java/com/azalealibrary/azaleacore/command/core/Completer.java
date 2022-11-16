package com.azalealibrary.azaleacore.command.core;

import org.bukkit.command.CommandSender;

import java.util.List;

public interface Completer extends Conditional {

    List<String> suggest(CommandSender sender, Arguments arguments);
}
