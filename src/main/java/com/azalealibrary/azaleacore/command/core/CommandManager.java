package com.azalealibrary.azaleacore.command.core;

import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Consumer;

public final class CommandManager {

    private final JavaPlugin plugin;

    public CommandManager(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public CommandHandler getCommandHandler(String name) {
        PluginCommand command = plugin.getCommand(name);
        if (command == null) {
            throw new IllegalArgumentException("Command with name '" + name + "' not found. Is it registered?");
        }
        return new CommandHandler(command);
    }

    public CommandManager init(String name, Consumer<CommandHandler> consumer) {
        consumer.accept(getCommandHandler(name));
        return this;
    }
}
