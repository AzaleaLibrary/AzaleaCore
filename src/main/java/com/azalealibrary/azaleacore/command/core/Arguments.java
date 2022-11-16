package com.azalealibrary.azaleacore.command.core;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Arguments extends AbstractList<String> {

    private static final String EMPTY = "";

    private final List<String> arguments;

    public Arguments(List<String> arguments) {
        this.arguments = arguments;
    }

    @Override
    public String get(int index) {
        return size() >= index ? arguments.get(index) : EMPTY;
    }

    @Override
    public boolean add(String argument) {
        return arguments.add(argument);
    }

    @Override
    public String remove(int index) {
        return arguments.remove(index);
    }

    @Override
    public int size() {
        return arguments.size();
    }

    public String missing(int index) {
        String argument = get(index);

        if (argument.equals(EMPTY)) {
            throw new AzaleaCommandException("Missing arguments.");
        }
        return argument;
    }

    public String matching(int index, String... actions) {
        String argument = missing(index);

        if (!Arrays.asList(actions).contains(argument)) {
            throw new AzaleaCommandException("Invalid argument provided.");
        }
        return argument;
    }

    public <T> T parse(int index, String message, Function<String, T> consumer) {
        String argument = missing(index);

        T obj = consumer.apply(argument);
        if (obj == null) {
            throw new AzaleaCommandException(String.format(message, argument));
        }
        return obj;
    }
}
