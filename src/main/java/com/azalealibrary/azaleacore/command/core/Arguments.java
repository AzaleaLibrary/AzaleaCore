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

    public Arguments subArguments(int from) {
        return new Arguments(command, subList(from, size()));
    }

    public String notMissing(int index, String thing) {
        String argument = get(index);

        if (argument.equals(EMPTY)) {
            throw new AzaleaException(String.format("Missing %s argument.", thing), command.getUsage());
        }
        return argument;
    }

    public String matchesAny(int index, String thing, String... values) {
        String argument = notMissing(index, thing);

        if (!Arrays.asList(values).contains(argument)) {
            throw new AzaleaException(String.format("Invalid %s argument provided: '%s'", thing, argument), command.getUsage());
        }
        return argument;
    }

    public <T> T find(int index, String thing, Function<String, T> consumer) {
        String argument = notMissing(index, thing);

        T obj = null;
        try {
            obj = consumer.apply(argument);
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }

        if (obj == null) {
            throw new AzaleaException(String.format("Could not find %s '%s'.", thing, argument));
        }
        return obj;
    }
}
