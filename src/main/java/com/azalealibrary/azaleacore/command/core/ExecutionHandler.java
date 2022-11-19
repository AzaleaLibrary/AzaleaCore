package com.azalealibrary.azaleacore.command.core;

import org.bukkit.command.CommandSender;

/**
 * Convenient interface to implement both {@link Condition} and {@link Executor}.
 * <p>
 * When {@link Condition#applyWhen(CommandSender, Arguments)} returns <b>true</b>,
 * run {@link Executor#execute(CommandSender, Arguments)}.
 */
public interface ExecutionHandler extends Condition, Executor { }
