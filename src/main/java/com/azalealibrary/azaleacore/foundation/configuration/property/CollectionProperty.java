package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.google.common.collect.ImmutableList;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;

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
        configuration.set(getName(), get().stream().map(propertyType::toObject).toList());
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        List<?> objects = configuration.getList(getName());

        if (objects != null) {
            objects.forEach(object -> get().add(propertyType.toValue(object)));
        }
    }

    @Override
    public String toString() {
        return getName() + "=" + (!isSet() ? ChatColor.ITALIC + "<empty>" : get().stream().map(propertyType::toString).toList());
    }
}
