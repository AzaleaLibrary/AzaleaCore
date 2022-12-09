package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;

public class CommandNode extends Command {

    private final List<CommandNode> children;

    public CommandNode(String name, CommandNode... children) {
        super(name);
        this.children = List.of(children);
    }

    public List<CommandNode> getChildren(CommandSender sender, Arguments arguments) {
        return children;
    }

    @Override
    public final boolean execute(@Nonnull CommandSender sender, @Nonnull String label, String[] args) {
        try {
            Arguments arguments = new Arguments(this, List.of(args));
            Map.Entry<CommandNode, Arguments> pair = getClosestMatch(sender, getChildren(sender, arguments), arguments, 0, this);
            Optional.ofNullable(pair.getKey()).ifPresent(n -> n.execute(sender, pair.getValue()));
        } catch (AzaleaException exception) {
            ChatMessage.error(exception.getMessage()).post(AzaleaCore.PLUGIN_ID, sender);

            for (String message : exception.getMessages()) {
                ChatMessage.error(message).post(AzaleaCore.PLUGIN_ID, sender);
            }
        } catch (Exception exception) {
            String error = exception.getMessage() != null ? exception.getMessage() : exception.toString();
            ChatMessage.error(error).post(AzaleaCore.PLUGIN_ID, sender);
            exception.printStackTrace();
        }
        return true;
    }

    @Override
    public final @Nonnull List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String label, String[] args) {
        Arguments arguments = new Arguments(this, List.of(args));

        if (arguments.size() > 1) {
            try {
                Map.Entry<CommandNode, Arguments> pair = getClosestMatch(sender, getChildren(sender, arguments), arguments, 0, this);
                return Optional.ofNullable(pair.getKey()).map(n -> n.complete(sender, pair.getValue())).orElse(new ArrayList<>());
            } catch (AzaleaException ignored) {
                // ignore AzaleaExceptions
            } catch (Exception exception) {
                String error = exception.getMessage() != null ? exception.getMessage() : exception.toString();
                ChatMessage.error(error).post(AzaleaCore.PLUGIN_ID, sender);
                exception.printStackTrace();
            }
        }
        return complete(sender, arguments);
    }

    protected Map.Entry<CommandNode, Arguments> getClosestMatch(CommandSender sender, List<CommandNode> children, Arguments arguments, int depth, CommandNode node) {
        if (arguments.size() == depth) {
            return new AbstractMap.SimpleEntry<>(node, arguments.subArguments(depth));
        }
        CommandNode child = children.stream().filter(n -> n.getName().equals(arguments.get(depth)) && n.testPermissionSilent(sender)).findFirst().orElse(null);

        if (child == null) {
            return new AbstractMap.SimpleEntry<>(node, arguments.subArguments(depth));
        }
        return getClosestMatch(sender, child.getChildren(sender, arguments.subArguments(depth)), arguments, depth + 1, child);
    }

    public void execute(CommandSender sender, Arguments arguments) {
        throw new AzaleaException("Invalid " + getName() + " command issued. Should be:", getUsage());
    }

    public List<String> complete(CommandSender sender, Arguments arguments) {
        return getChildren(sender, arguments).stream().filter(n -> n.testPermissionSilent(sender)).map(Command::getName).toList();
    }

    public static void register(JavaPlugin plugin, Class<? extends CommandNode> command) {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(plugin.getName(), command.getConstructor().newInstance());
        } catch (Exception exception) {
            throw new RuntimeException("An error occurred while registering command.", exception);
        }
    }
}
