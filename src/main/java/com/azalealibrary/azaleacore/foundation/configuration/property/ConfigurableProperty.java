package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.Arguments;
import com.azalealibrary.azaleacore.foundation.Serializable;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Optional;

public abstract class ConfigurableProperty<T> implements Serializable {

    private final Class<?> type;
    private final String name;
    private final String description;
    private final T defaultValue;
    private final boolean required;

    private T value;

    protected ConfigurableProperty(Class<?> type, String name, String description, T defaultValue, boolean required) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.defaultValue = defaultValue;
        this.required = required;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return required;
    }

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

    @Override
    public boolean equals(Object object) {
        if (object instanceof ConfigurableProperty<?> property) {
            return property.name.equals(name) && property.type.equals(type);
        }
        return super.equals(object);
    }
}
