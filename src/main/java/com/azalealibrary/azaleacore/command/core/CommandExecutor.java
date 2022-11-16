package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.command.CommandSender;

import java.util.function.BiFunction;
import java.util.function.Predicate;

public class CommandExecutor implements CommandConditional {

    private final Predicate<Arguments> shouldHit;
    private final BiFunction<CommandSender, Arguments, Message> executor;

    public CommandExecutor(Predicate<Arguments> shouldHit, BiFunction<CommandSender, Arguments, Message> executor) {
        this.shouldHit = shouldHit;
        this.executor = executor;
    }

    public Message execute(CommandSender sender, Arguments arguments) {
        return executor.apply(sender, arguments);
    }

    @Override
    public boolean shouldHit(Arguments arguments) {
        return shouldHit.test(arguments);
    }
}
