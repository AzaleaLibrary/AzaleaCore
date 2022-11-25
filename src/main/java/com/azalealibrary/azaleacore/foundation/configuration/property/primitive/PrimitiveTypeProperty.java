package com.azalealibrary.azaleacore.foundation.configuration.property.primitive;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.configuration.property.AssignmentPolicy;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class PrimitiveTypeProperty<T> extends Property<T> {

    private final PrimitiveTypeParser<T> parser;
    private final PrimitiveTypeCompleter<T> completer;

    @SafeVarargs
    public PrimitiveTypeProperty(String name, T defaultValue, boolean required, PrimitiveTypeParser<T> parser, PrimitiveTypeCompleter<T> completer, AssignmentPolicy<T>... policies) {
        super(name, defaultValue, required, policies);
        this.parser = parser;
        this.completer = completer;
    }

    @Override
    public void set(CommandSender sender, Arguments arguments) {
        set(parser.parseValue(arguments.getLast()));
    }

    @Override
    public List<String> suggest(CommandSender sender, Arguments arguments) {
        return arguments.size() == 1 ? List.of(completer.suggest(get())) : List.of();
    }
}
