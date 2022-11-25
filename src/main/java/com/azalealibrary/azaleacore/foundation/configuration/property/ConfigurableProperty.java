package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class ConfigurableProperty<T> implements Supplier<T>, Serializable {

    private final String name;
    private final T defaultValue;
    private final boolean required;

    private T value;

    protected ConfigurableProperty(String name, T defaultValue, boolean required) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    @Override
    public T get() {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    public void set(T value) {
        this.value = value;
    }

    public void reset() {
        value = defaultValue;
    }

    public boolean isSet() {
        return get() != null;
    }

    public abstract void fromCommand(CommandSender sender, Arguments arguments);

    public abstract List<String> suggest(CommandSender sender, Arguments arguments);
}
