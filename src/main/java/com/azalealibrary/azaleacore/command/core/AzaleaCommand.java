package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.broadcast.AzaleaBroadcaster;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.google.common.collect.ImmutableList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.*;

public abstract class AzaleaCommand extends Command {

    private final ImmutableList<ExecutionHandler> executors;
    private final ImmutableList<CompletionHandler> completers;

    protected AzaleaCommand(CommandDescriptor descriptor) {
        super(descriptor.getName(), descriptor.getDescription(), descriptor.getUsage(), Arrays.asList(descriptor.getAliases()));
        setPermission(descriptor.getPermission());
        setPermissionMessage(descriptor.getPermissionMessage());

        CommandConfigurator configurator = new CommandConfigurator();
        configure(configurator);
        this.executors = ImmutableList.copyOf(configurator.getExecutors());
        this.completers = ImmutableList.copyOf(configurator.getCompleters());
    }

    protected abstract void configure(CommandConfigurator configurator);

    @Override
    public final boolean execute(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
        try {
            Arguments arguments = new Arguments(this, Arrays.asList(args));
            Executor executor = executors.stream()
                    .filter(e -> e.applyWhen(sender, arguments))
                    .findFirst()
                    .orElseThrow(() -> new AzaleaException("Invalid Azalea command issued.", getUsage()));

            Message message = executor.execute(sender, arguments);
            if (message != null) {
                AzaleaBroadcaster.getInstance().send(sender, message);
            }
        } catch (AzaleaException exception) {
            AzaleaBroadcaster.getInstance().send(sender, ChatMessage.error(exception.getMessage()));

            for (String message : exception.getMessages()) {
                AzaleaBroadcaster.getInstance().send(sender, ChatMessage.error(message));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            String error = exception.getMessage() != null ? exception.getMessage() : exception.toString();
            AzaleaBroadcaster.getInstance().send(sender, ChatMessage.error(error));
        }
        return true;
    }

    @Override
    public final @Nonnull List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String label, @Nonnull String[] args) {
        Arguments arguments = new Arguments(this, Arrays.asList(args));
        Optional<CompletionHandler> optional = completers.stream()
                .filter(completer -> completer.applyWhen(sender, arguments))
                .findFirst();

        try {
            List<String> output = optional.map(completer -> new ArrayList<>(completer.suggest(sender, arguments))).orElseGet(ArrayList::new);
            String last = arguments.size() > 0 ? arguments.getLast() : "";
            output.sort(Comparator.<String, Boolean>comparing(s -> s.contains(last)).reversed());
            return output;
        } catch (AzaleaException ignored) {
            // ignore AzaleaExceptions
        } catch (Exception exception) {
            exception.printStackTrace();
            String error = exception.getMessage() != null ? exception.getMessage() : exception.toString();
            AzaleaBroadcaster.getInstance().send(sender, ChatMessage.error(error));
        }
        return new ArrayList<>();
    }

    public static void register(JavaPlugin plugin, Class<?> clazz) {
        if (!clazz.isAnnotationPresent(AzaCommand.class)) {
            throw new IllegalArgumentException("Registering non-command class as Azalea command.");
        }

        AzaCommand annotation = clazz.getAnnotation(AzaCommand.class);
        CommandDescriptor descriptor = new CommandDescriptor(
                annotation.name(),
                annotation.description(),
                annotation.usage(),
                annotation.aliases(),
                annotation.permission(),
                annotation.permissionMessage()
        );

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);

            CommandMap map = (CommandMap) field.get(Bukkit.getServer());
            AzaleaCommand command = (AzaleaCommand) clazz.getConstructor(CommandDescriptor.class).newInstance(descriptor);
            map.register(plugin.getName(), command);
        } catch (Exception exception) {
            throw new RuntimeException("An error occurred while register Azalea command.", exception);
        }
    }
}
