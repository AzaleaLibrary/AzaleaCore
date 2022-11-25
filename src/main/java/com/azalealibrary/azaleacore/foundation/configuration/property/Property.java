package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class Property<T> implements Serializable, Supplier<T> {

    private final String name;
    private final T defaultValue;
    private final boolean required;
    private final AssignmentPolicy<T> validator;

    private T value;

    public Property(String name, T defaultValue, boolean required) {
        this(name, defaultValue, required, AssignmentPolicy.anything());
    }

    public Property(String name, T defaultValue, boolean required, AssignmentPolicy<T> validator) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.required = required;
        this.validator = validator;
    }

    public String getName() {
        return name;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isSet() {
        return get() != null;
    }

    public T get() {
        return Optional.ofNullable(value).orElse(defaultValue);
    }

    public void set(T value) {
        if (!validator.canAssign(value)) {
            throw new AzaleaException(validator.getMessage(value));
        }
        this.value = value;
    }

    public void reset() {
        value = defaultValue;
    }

    public abstract void set(CommandSender sender, Arguments arguments);

    public abstract List<String> suggest(CommandSender sender, Arguments arguments);

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        configuration.set(getName(), get());
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        set((T) configuration.get(getName()));
    }

    @Override
    public String toString() {
        return getName() + "=" + (get() == null ? ChatColor.ITALIC + "<empty>" : get());
    }
}
