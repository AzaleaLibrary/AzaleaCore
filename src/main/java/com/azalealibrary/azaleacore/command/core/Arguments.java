package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.foundation.AzaleaException;
import org.bukkit.command.Command;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class Arguments extends AbstractList<String> {

    private static final String EMPTY = "";

    private final Command command;
    private final List<String> arguments;

    public Arguments(Command command, List<String> arguments) {
        this.command = command;
        this.arguments = arguments;
    }

    public Command getCommand() {
        return command;
    }

    @Override
    public String get(int index) {
        return index < size() ? arguments.get(index) : EMPTY;
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
            throw new AzaleaException("Missing arguments.", command.getUsage());
        }
        return argument;
    }

    public String matching(int index, String... actions) {
        String argument = missing(index);

        if (!Arrays.asList(actions).contains(argument)) {
            throw new AzaleaException("Invalid argument provided.", command.getUsage());
        }
        return argument;
    }

    public <T> T parse(int index, String message, Function<String, T> consumer) {
        String argument = missing(index);

        T obj;
        try {
            obj = consumer.apply(argument);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            exception.printStackTrace();
            throw new AzaleaException(String.format(message, argument));
        }

        if (obj == null) {
            throw new AzaleaException(String.format(message, argument));
        }
        return obj;
    }
}
