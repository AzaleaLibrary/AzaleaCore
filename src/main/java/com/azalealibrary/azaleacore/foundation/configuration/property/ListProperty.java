package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class ListProperty<T> extends Property<List<T>> {

    private static final String ADD = "add";
    private static final String REMOVE = "remove";

    public ListProperty(String name, List<T> defaultValue, boolean required) {
        super(name, defaultValue, required);
    }

    @Override
    public void set(CommandSender sender, Arguments arguments) {
        String action = arguments.matchesAny(0, ADD, REMOVE);
        int index = arguments.find(1, "index", input -> Integer.parseInt(input.replace("@", "")));

        if (action.equals(ADD)) {
            Arguments sub = new Arguments(arguments.getCommand(), arguments.subList(2, arguments.size()));
            T parsed = parseValue(sender, sub);
            get().add(index, parsed);
        } else if (action.equals(REMOVE)) {
            get().remove(index);
        }
    }

    @Override
    public List<String> suggest(CommandSender sender, Arguments arguments) {
        if (arguments.size() == 0) {
            return List.of(ADD, REMOVE);
        } else if (arguments.size() == 1) {
            return List.of("@" + get().size());
        }
        return suggestValue(sender, arguments);
    }

    public abstract T parseValue(CommandSender sender, Arguments arguments);

    public abstract List<String> suggestValue(CommandSender sender, Arguments arguments);
}
