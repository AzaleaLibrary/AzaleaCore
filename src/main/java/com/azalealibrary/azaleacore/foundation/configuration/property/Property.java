package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.google.common.collect.ImmutableList;
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
    private final ImmutableList<AssignmentPolicy<T>> policies;

    private T value;

    @SafeVarargs
    public Property(String name, T defaultValue, boolean required, AssignmentPolicy<T>... policies) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.required = required;
        this.policies = ImmutableList.copyOf(policies);
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
        AssignmentPolicy<T> failedCheck = policies.stream()
                .filter(validator -> !validator.canAssign(value))
                .findAny().orElse(null);

        if (failedCheck != null) {
            throw new AzaleaException(failedCheck.getMessage(value));
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
