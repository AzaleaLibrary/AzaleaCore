package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class CommandHandler implements TabExecutor {

    private final List<ExecutionHandler> executors = new ArrayList<>();
    private final List<CompletionHandler> completers = new ArrayList<>();

    public CommandHandler(PluginCommand command) {
        command.setExecutor(this);
        command.setTabCompleter(this);
    }

    public CommandHandler executeWhen(Condition conditional, Executor executor) {
        return addExecutor(new ExecutionHandler() {
            @Override
            public boolean applyWhen(CommandSender sender, Arguments arguments) {
                return conditional.applyWhen(sender, arguments);
            }

            @Nullable
            @Override
            public Message execute(CommandSender sender, Arguments arguments) {
                return executor.execute(sender, arguments);
            }
        });
    }

    public CommandHandler completeWhen(Condition conditional, Completer completer) {
        return addCompleter(new CompletionHandler() {
            @Override
            public boolean applyWhen(CommandSender sender, Arguments arguments) {
                return conditional.applyWhen(sender, arguments);
            }

            @Override
            public List<String> suggest(CommandSender sender, Arguments arguments) {
                return completer.suggest(sender, arguments);
            }
        });
    }

    public CommandHandler addExecutor(ExecutionHandler executor) {
        this.executors.add(executor);
        return this;
    }

    public CommandHandler addCompleter(CompletionHandler completer) {
        this.completers.add(completer);
        return this;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        try {
            Arguments arguments = new Arguments(command, Arrays.asList(args));
            Executor executor = executors.stream()
                    .filter(e -> e.applyWhen(sender, arguments))
                    .findFirst()
                    .orElseThrow(() -> new AzaleaException("Invalid Azalea command issued.", command.getUsage()));

            Message message = executor.execute(sender, arguments);
            if (message != null) {
                message.post(AzaleaCore.PLUGIN_ID, sender);
            }
        } catch (AzaleaException exception) {
            ChatMessage.failure(exception.getMessage()).post(AzaleaCore.PLUGIN_ID, sender);

            for (String message : exception.getMessages()) {
                ChatMessage.failure(message).post(AzaleaCore.PLUGIN_ID, sender);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            String error = exception.getMessage() != null ? exception.getMessage() : exception.toString();
            ChatMessage.failure(error).post(AzaleaCore.PLUGIN_ID, sender);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        Arguments arguments = new Arguments(command, Arrays.asList(args));
        Optional<CompletionHandler> optional = completers.stream()
                .filter(c -> c.applyWhen(sender, arguments))
                .findFirst();

        List<String> output = optional.map(completer -> new ArrayList<>(completer.suggest(sender, arguments)))
                .orElseGet(ArrayList::new);

        String last = arguments.size() > 0 ? arguments.get(arguments.size() - 1) : "";
        output.sort(Comparator.<String, Boolean>comparing(s -> s.contains(last)).reversed());
        return output;
    }
}
