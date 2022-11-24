package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class Property<T> implements Serializable, Supplier<T> {

    private final String name;
    private final T defaultValue;
    private final boolean required;

    private T value;

    public Property(String name, T defaultValue, boolean required) {
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

    public boolean isSet() {
        return get() != null;
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

    public void set(CommandSender sender, Arguments arguments) { // TODO - abstract ?
        set((T) arguments.get(arguments.size() - 1));
    }

    public List<String> suggest(CommandSender sender, Arguments arguments) { // TODO - abstract ?
        return List.of();
    }

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
