package com.azalealibrary.azaleacore.command.core;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class CommandCompleter implements CommandConditional {

    private final Predicate<Arguments> shouldHit;
    private final Function<Arguments, List<String>> suggest;

    public CommandCompleter(Predicate<Arguments> shouldHit, Function<Arguments, List<String>> suggest) {
        this.shouldHit = shouldHit;
        this.suggest = suggest;
    }

    public List<String> suggest(Arguments arguments) {
        return suggest.apply(arguments);
    }

    @Override
    public boolean shouldHit(Arguments arguments) {
        return shouldHit.test(arguments);
    }
}
