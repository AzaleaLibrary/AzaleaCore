package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CommandConfigurator {

    private final List<ExecutionHandler> executors = new ArrayList<>();
    private final List<CompletionHandler> completers = new ArrayList<>();

    public List<ExecutionHandler> getExecutors() {
        return executors;
    }

    public List<CompletionHandler> getCompleters() {
        return completers;
    }

    public CommandConfigurator executeWhen(Condition conditional, Executor executor) {
        return addExecutor(new ExecutionHandler() {
            @Override
            public boolean applyWhen(CommandSender sender, Arguments arguments) {
                return conditional.applyWhen(sender, arguments);
            }

            @Override
            public @Nullable Message execute(CommandSender sender, Arguments arguments) {
                return executor.execute(sender, arguments);
            }
        });
    }

    public CommandConfigurator completeWhen(Condition conditional, Completer completer) {
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

    public CommandConfigurator addExecutor(ExecutionHandler executor) {
        this.executors.add(executor);
        return this;
    }

    public CommandConfigurator addCompleter(CompletionHandler completer) {
        this.completers.add(completer);
        return this;
    }
}
