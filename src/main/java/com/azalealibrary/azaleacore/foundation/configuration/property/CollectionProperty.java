package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.List;
import java.util.Optional;

public final class CollectionProperty<T> extends ConfigurableProperty<List<T>> implements ProtectedAssignment<T> {

    private static final String ADD = "add";
    private static final String REMOVE = "remove";
    private static final String REPLACE = "replace";

    private final PropertyType<T> propertyType;
    private final ImmutableList<AssignmentPolicy<T>> policies;

    @SafeVarargs
    public CollectionProperty(PropertyType<T> propertyType, String name, List<T> defaultValue, boolean required, AssignmentPolicy<T>... policies) {
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
        String action = arguments.matchesAny(0, "list operation", ADD, REMOVE, REPLACE);

        if (action.equals(ADD)) {
            get().add(verify(propertyType.parse(sender, arguments.subArguments(1), null)));
        } else {
            int index = arguments.find(1, "position", input -> Integer.parseInt(input.replace("@", "")));

            if (index >= get().size()) {
                throw new AzaleaException("Specified list position '" + index +"' too large for list of size " + get().size() + ".");
            }

            if (action.equals(REPLACE)) {
                get().set(index, verify(propertyType.parse(sender, arguments.subArguments(2), null)));
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
        } else if (arguments.is(0, ADD)) {
            // avoid suggesting more than necessary
            List<String> suggestion = propertyType.suggest(sender, arguments, null);
            return arguments.size() -1 <= suggestion.size() ? suggestion : List.of();
        }
        return List.of();
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        Optional.ofNullable(get()).ifPresent(value -> configuration.set(getName(), value.stream().map(propertyType::toObject).toList()));
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        Optional.ofNullable(configuration.getList(getName())).ifPresent(objects -> objects.forEach(object -> get().add(propertyType.toValue(object))));
    }

    @Override
    public String toString() {
        String name = ChatColor.LIGHT_PURPLE + getName() + ChatColor.RESET;
        String value = !isSet() ? ChatColor.DARK_GRAY + "<empty>" : colorize(get().stream().map(propertyType::toString).toList()).toString();
        return name + "=" + value;
    }

    private static List<String> colorize(List<String> elements) {
        return elements.stream().map(element -> net.md_5.bungee.api.ChatColor.of(new Color((int) (Math.random() * 0x1000000))) + element + ChatColor.RESET).toList();
    }
}
