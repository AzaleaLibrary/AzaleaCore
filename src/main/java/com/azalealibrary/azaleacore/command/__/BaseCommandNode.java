package com.azalealibrary.azaleacore.command.__;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;

public abstract class BaseCommandNode extends CommandNode {

    protected BaseCommandNode(String name, CommandNode... children) {
        super(name, children);
    }

    @Override
    public final boolean execute(@Nonnull CommandSender sender, @Nonnull String label, String[] args) {
        try {
            onClosestMatch(sender, getChildren(), new Arguments(this, List.of(args)), 0, null, (node, params) -> node.execute(sender, params));
        } catch (AzaleaException exception) {
            ChatMessage.error(exception.getMessage()).post(AzaleaCore.PLUGIN_ID, sender);

            for (String message : exception.getMessages()) {
                ChatMessage.error(message).post(AzaleaCore.PLUGIN_ID, sender);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            String error = exception.getMessage() != null ? exception.getMessage() : exception.toString();
            ChatMessage.error(error).post(AzaleaCore.PLUGIN_ID, sender);
        }
        return true;
    }

    @Override
    public final @Nonnull List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String label, String[] args) {
        Arguments arguments = new Arguments(this, List.of(args));

        if (args.length > 1) {
            AtomicReference<List<String>> output = new AtomicReference<>(new ArrayList<>());
            onClosestMatch(sender, getChildren(), arguments, 0, null, (node, params) -> output.set(node.complete(sender, params)));
            return output.get();
        }
        return super.complete(sender, arguments);
    }

    private void onClosestMatch(CommandSender sender, List<CommandNode> parents, Arguments arguments, int depth, @Nullable CommandNode node, BiConsumer<CommandNode, Arguments> consumer) {
        if (arguments.size() == depth && node != null) consumer.accept(node, arguments.subArguments(depth));
        CommandNode child = parents.stream().filter(n -> n.getName().equals(arguments.get(depth)) && (n.getPermission() == null || sender.hasPermission(n.getPermission()))).findFirst().orElse(null);
        if (child != null) onClosestMatch(sender, child.getChildren(), arguments, depth + 1, child, consumer);
        else if (node != null) consumer.accept(node, arguments.subArguments(depth));
    }

    public static void register(JavaPlugin plugin, BaseCommandNode command) {
        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            map.register(plugin.getName(), command);
        } catch (Exception exception) {
            throw new RuntimeException("An error occurred while registering command.", exception);
        }
    }
}
