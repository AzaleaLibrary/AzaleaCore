package com.azalealibrary.azaleacore.command.core;

import org.bukkit.command.CommandSender;

/**
 * Convenient interface to implement both {@link Condition} and {@link Completer}.
 * <p>
 * When {@link Condition#applyWhen(CommandSender, Arguments)} returns <b>true</b>,
 * run {@link Completer#suggest(CommandSender, Arguments)}.
 */
public interface CompletionHandler extends Condition, Completer { }
