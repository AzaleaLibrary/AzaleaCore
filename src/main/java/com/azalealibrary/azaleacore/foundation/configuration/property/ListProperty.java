package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import org.bukkit.command.CommandSender;

import java.util.List;

public class ListProperty<T> extends Property<List<T>> {

    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String REPLACE = "replace";

    public ListProperty(String name, List<T> defaultValue, boolean required) {
        super(name, defaultValue, required);
    }

    @Override
    public void set(CommandSender sender, Arguments arguments) {
        String action = arguments.matchesAny(0, "list operation", ADD, REMOVE, REPLACE);
        int index = arguments.find(1, "position", input -> Integer.parseInt(input.replace("@", "")));

        if ((action.equals(ADD) && index > get().size()) || (!action.equals(ADD) && index >= get().size())) {
            throw new AzaleaException("Specified list position '" + index +"' too large for list of size " + get().size() + ".");
        }

        if (action.equals(REMOVE)) {
            get().remove(index);
        } else {
            Arguments sub = new Arguments(arguments.getCommand(), arguments.subList(2, arguments.size()));
            T parsedValue = parseValue(sender, sub);

            if (action.equals(ADD)) {
                get().add(parsedValue);
            } else {
                get().set(index, parsedValue);
            }
        }
    }

    @Override
    public List<String> suggest(CommandSender sender, Arguments arguments) {
        if (arguments.size() == 1) {
            return List.of(ADD, REMOVE, REPLACE);
        } else if (arguments.size() == 2) {
            return List.of("@" + (arguments.get(0).equals(ADD) ? get().size() : get().size() - 1));
        }

        // avoid suggesting more than necessary
        List<String> suggestion = suggestValue(sender, arguments);
        return arguments.size() -2 <= suggestion.size() ? suggestion : List.of();
    }

    public T parseValue(CommandSender sender, Arguments arguments) {
        return (T) arguments.get(0);
    }

    public List<String> suggestValue(CommandSender sender, Arguments arguments) {
        return List.of();
    }
}
