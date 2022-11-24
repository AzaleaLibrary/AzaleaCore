package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class EnumProperty<E extends Enum<E>> extends Property<E> {

    private final Class<E> clazz;

    public EnumProperty(String name, Class<E> clazz, E defaultValue, boolean required) {
        super(name, defaultValue, required);
        this.clazz = clazz;
    }

    @Override
    public List<String> suggest(CommandSender sender, Arguments arguments) {
        return Arrays.stream(clazz.getEnumConstants()).map(v -> v.name().toLowerCase()).toList();
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        configuration.set(getName(), get().name());
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        try {
            set(E.valueOf(clazz, Objects.requireNonNull(configuration.getString(getName()))));
        } catch (Exception exception) {
            System.err.println(exception.getMessage());
            exception.printStackTrace();
        }
    }
}
