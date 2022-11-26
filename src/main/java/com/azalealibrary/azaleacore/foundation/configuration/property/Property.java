package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;

public final class Property<T> extends ConfigurableProperty<T> implements ProtectedAssignment<T> {

    private final PropertyType<T> propertyType;
    private final ImmutableList<AssignmentPolicy<T>> policies;

    @SafeVarargs
    public Property(PropertyType<T> propertyType, String name, T defaultValue, boolean required, AssignmentPolicy<T>... policies) {
        super(name, defaultValue, required);
        this.propertyType = propertyType;
        this.policies = ImmutableList.copyOf(policies);
    }

    @Override
    public List<AssignmentPolicy<T>> getAssignmentPolicies() {
        return policies;
    }

    @Override
    public void fromCommand(CommandSender sender, Arguments arguments) {
        set(verify(propertyType.parse(sender, arguments, get())));
    }

    @Override
    public List<String> suggest(CommandSender sender, Arguments arguments) {
        return propertyType.suggest(sender, arguments, get());
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        Optional.ofNullable(get()).ifPresent(value -> configuration.set(getName(), propertyType.toObject(value)));
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        Optional.ofNullable(configuration.get(getName())).ifPresent(object -> set(propertyType.toValue(object)));
    }

    @Override
    public String toString() {
        return getName() + "=" + (!isSet() ? ChatColor.ITALIC + "<empty>" : propertyType.toString(get()));
    }
}
