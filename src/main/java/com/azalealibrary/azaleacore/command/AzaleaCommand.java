package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.Main;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

// TODO - currently composition-based, what about inheritance?
public abstract class AzaleaCommand implements CommandExecutor, TabCompleter {

    protected AzaleaCommand(JavaPlugin plugin, String name) {
        PluginCommand command = plugin.getCommand(name);

        if (command == null) {
            throw new IllegalArgumentException("Azalea command with name '" + name + "' does not exist.");
        }

        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String ignored, @Nonnull String[] args) {
        try {
            Optional.ofNullable(execute(sender, Arrays.asList(args))).ifPresent(message -> message.post(Main.PLUGIN_ID, sender));
        } catch (Exception exception) {
            exception.printStackTrace();
            String error = exception.getMessage() != null ? exception.getMessage() : exception.toString();
            failure(error).post(Main.PLUGIN_ID, sender);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        List<String> params = Arrays.asList(args);
        List<String> output = new ArrayList<>(onTabComplete(sender, params));
        String last = params.size() > 0 ? params.get(params.size() - 1) : "";
        output.sort(Comparator.<String, Boolean>comparing(s -> s.contains(last)).reversed());
        return output;
    }

    protected abstract @Nullable Message execute(@Nonnull CommandSender sender, List<String> params);

    protected abstract List<String> onTabComplete(CommandSender sender, List<String> params);

    protected static Message success(String message) {
        return new ChatMessage(ChatColor.GREEN + message);
    }

    protected static Message failure(String message) {
        return new ChatMessage(ChatColor.RED + message);
    }

    protected static Message notFound(String thing, String input) {
        return failure("Could not find '" + input + "' " + thing + ".");
    }

    protected static Message invalid(String thing, String input) {
        return failure("Invalid " + thing + " provided: '" + input + "'.");
    }
}
