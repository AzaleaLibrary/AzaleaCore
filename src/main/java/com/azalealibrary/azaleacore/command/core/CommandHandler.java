package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class CommandHandler extends Command {

    private final List<ExecutionHandler> executors;
    private final List<CompletionHandler> completers;

    private CommandHandler(String name, String description, String usage, String[] aliases, String permission, String permissionMessage, List<ExecutionHandler> executors, List<CompletionHandler> completers) {
        super(name, description, usage, Arrays.asList(aliases));
        this.executors = executors;
        this.completers = completers;
        setPermission(permission);
        setPermissionMessage(permissionMessage);
    }

    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
        try {
            Arguments arguments = new Arguments(this, Arrays.asList(args));
            Executor executor = executors.stream()
                    .filter(e -> e.applyWhen(sender, arguments))
                    .findFirst()
                    .orElseThrow(() -> new AzaleaException("Invalid Azalea command issued.", getUsage()));

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
    public @Nonnull List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
        Arguments arguments = new Arguments(this, Arrays.asList(args));
        Optional<CompletionHandler> optional = completers.stream()
                .filter(c -> c.applyWhen(sender, arguments))
                .findFirst();

        List<String> output = optional.map(completer -> new ArrayList<>(completer.suggest(sender, arguments)))
                .orElseGet(ArrayList::new);

        String last = arguments.size() > 0 ? arguments.get(arguments.size() - 1) : "";
        output.sort(Comparator.<String, Boolean>comparing(s -> s.contains(last)).reversed());
        return output;
    }

    public static class Builder {

        private final String name;
        private final List<ExecutionHandler> executors = new ArrayList<>();
        private final List<CompletionHandler> completers = new ArrayList<>();

        private String description;
        private String usage;
        private String[] aliases;
        private String permission;
        private String permissionMessage;

        public Builder(String name) {
            this.name = name;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setUsage(String usage) {
            this.usage = usage;
            return this;
        }

        public Builder setAliases(String... aliases) {
            this.aliases = aliases;
            return this;
        }

        public Builder setPermission(String permission) {
            this.permission = permission;
            return this;
        }

        public Builder setPermissionMessage(String permissionMessage) {
            this.permissionMessage = permissionMessage;
            return this;
        }

        public Builder executeWhen(Condition conditional, Executor executor) {
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

        public Builder completeWhen(Condition conditional, Completer completer) {
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

        public Builder addExecutor(ExecutionHandler executor) {
            this.executors.add(executor);
            return this;
        }

        public Builder addCompleter(CompletionHandler completer) {
            this.completers.add(completer);
            return this;
        }

        public CommandHandler build() {
            return new CommandHandler(name, description, usage, aliases, permission, permissionMessage, executors, completers);
        }
    }
}
