package com.azalealibrary.azaleacore.command;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.command.core.*;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Predicate;

// TODO - currently composition-based, what about inheritance?
public abstract class AzaleaCommand implements CommandExecutor, TabCompleter {

    private final List<Executor> executors = new ArrayList<>();
    private final List<Completer> completers = new ArrayList<>();

    protected AzaleaCommand(JavaPlugin plugin, String name) {
        PluginCommand command = Objects.requireNonNull(plugin.getCommand(name));
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    protected void executeWhen(Predicate<Arguments> predicate, BiFunction<CommandSender, Arguments, Message> executor) {
        addExecutor(new ExecutionHandler(predicate, executor));
    }

    protected void completeWhen(Predicate<Arguments> predicate, BiFunction<CommandSender, Arguments, List<String>> completer) {
        addCompleter(new CompletionHandler(predicate, completer));
    }

    protected void addExecutor(Executor executor) {
        executors.add(executor);
    }

    protected void addCompleter(Completer completer) {
        completers.add(completer);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String ignored, @Nonnull String[] args) {
        try {
            Arguments arguments = new Arguments(command, Arrays.asList(args));

            Executor executor = executors.stream().filter(exe -> exe.applyWhen(arguments))
                    .findFirst()
                    .orElse(new ExecutionHandler(a -> false, (s, a) -> failure(command.getUsage())));

            Message message = executor.execute(sender, arguments);
            if (message != null) {
                message.post(AzaleaCore.PLUGIN_ID, sender);
            }
        } catch (AzaleaException exception) {
            failure(exception.getMessage()).post(AzaleaCore.PLUGIN_ID, sender);

            for (String message : exception.getMessages()) {
                failure(message).post(AzaleaCore.PLUGIN_ID, sender);
            }
        } catch (Exception exception) {
            exception.printStackTrace();

            String error = exception.getMessage() != null
                    ? exception.getMessage()
                    : exception.toString();
            failure(error).post(AzaleaCore.PLUGIN_ID, sender);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        List<String> params = Arrays.asList(args);
        Arguments arguments = new Arguments(command, params);

        Optional<Completer> optional = completers.stream()
                .filter(completer -> completer.applyWhen(arguments))
                .findFirst();

        List<String> output = optional.map(completer -> new ArrayList<>(completer.suggest(sender, arguments)))
                .orElseGet(ArrayList::new);

        String last = params.size() > 0 ? params.get(params.size() - 1) : "";
        output.sort(Comparator.<String, Boolean>comparing(s -> s.contains(last)).reversed());
        return output;
    }

    protected static Message success(String message) {
        return new ChatMessage(ChatColor.GREEN + message);
    }

    protected static Message warn(String message) {
        return new ChatMessage(ChatColor.GOLD + message);
    }

    protected static Message failure(String message) {
        return new ChatMessage(ChatColor.RED + message);
    }
}
