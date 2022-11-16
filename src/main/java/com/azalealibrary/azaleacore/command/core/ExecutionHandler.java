package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.Predicate;

public class ExecutionHandler implements Executor {

    private final Predicate<Arguments> predicate;
    private final BiFunction<CommandSender, Arguments, Message> executor;

    public ExecutionHandler(Predicate<Arguments> predicate, BiFunction<CommandSender, Arguments, Message> executor) {
        this.predicate = predicate;
        this.executor = executor;
    }

    @Override
    public @Nullable Message execute(CommandSender sender, Arguments arguments) {
        return executor.apply(sender, arguments);
    }

    @Override
    public boolean applyWhen(Arguments arguments) {
        return predicate.test(arguments);
    }
}
