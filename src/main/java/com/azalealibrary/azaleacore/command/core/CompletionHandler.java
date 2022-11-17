package com.azalealibrary.azaleacore.command.core;

import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

public class CompletionHandler implements Completer {

    private final BiPredicate<CommandSender, Arguments> predicate;
    private final BiFunction<CommandSender, Arguments, List<String>> suggest;

    public CompletionHandler(BiPredicate<CommandSender, Arguments> predicate, BiFunction<CommandSender, Arguments, List<String>> suggest) {
        this.predicate = predicate;
        this.suggest = suggest;
    }

    @Override
    public List<String> suggest(CommandSender sender, Arguments arguments) {
        return suggest.apply(sender, arguments);
    }

    @Override
    public boolean applyWhen(CommandSender sender, Arguments arguments) {
        return predicate.test(sender, arguments);
    }
}
