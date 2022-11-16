package com.azalealibrary.azaleacore.command.core;

import org.bukkit.command.CommandSender;

public interface Conditional {

    boolean applyWhen(CommandSender sender, Arguments arguments);
}