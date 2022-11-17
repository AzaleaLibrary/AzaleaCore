package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class ExecutionHandler implements Executor {

    private final BiPredicate<CommandSender, Arguments> predicate;
    private final BiFunction<CommandSender, Arguments, Message> executor;

    public ExecutionHandler(BiPredicate<CommandSender, Arguments> predicate, BiFunction<CommandSender, Arguments, Message> executor) {
        this.predicate = predicate;
        this.executor = executor;
    }

    @Override
    public @Nullable Message execute(CommandSender sender, Arguments arguments) {
        return executor.apply(sender, arguments);
    }

    @Override
    public boolean applyWhen(CommandSender sender, Arguments arguments) {
        return predicate.test(sender, arguments);
    }
}
