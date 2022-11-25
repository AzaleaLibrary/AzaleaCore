package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class ListProperty<T> extends Property<List<T>> {

    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String REPLACE = "replace";

    public ListProperty(String name, List<T> defaultValue, boolean required) {
        super(name, defaultValue, required);
    }

    @Override
    public void set(CommandSender sender, Arguments arguments) {
        String action = arguments.matchesAny(0, "list operation", ADD, REMOVE, REPLACE);

        if (action.equals(ADD)) {
            get().add(parseValue(sender, arguments.subArguments(1)));
        } else {
            int index = arguments.find(1, "position", input -> Integer.parseInt(input.replace("@", "")));

            if (index >= get().size()) {
                throw new AzaleaException("Specified list position '" + index +"' too large for list of size " + get().size() + ".");
            }

            if (action.equals(REPLACE)) {
                get().set(index, parseValue(sender, arguments.subArguments(2)));
            } else {
                get().remove(index);
            }
        }
    }

    @Override
    public List<String> suggest(CommandSender sender, Arguments arguments) {
        if (arguments.size() == 1) {
            return List.of(ADD, REMOVE, REPLACE);
        } else if (arguments.size() == 2 && !get().isEmpty() && !arguments.is(0, ADD)) {
            return List.of("@" + (get().size() - 1));
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
