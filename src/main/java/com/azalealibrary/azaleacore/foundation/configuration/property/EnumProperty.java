package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.command.core.Arguments;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class EnumProperty<E extends Enum<E>> extends Property<E> {

    private final Class<E> clazz;

    public EnumProperty(String name, Class<E> clazz, E defaultValue, boolean required) {
        super(name, defaultValue, required);
        this.clazz = clazz;
    }

    @Override
    public void set(CommandSender sender, Arguments arguments) {
        set(safeValueOf(clazz, arguments.getLast().toUpperCase()));
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
        set(safeValueOf(clazz, configuration.getString(getName())));
    }

    private static @Nullable <E extends Enum<E>> E safeValueOf(Class<E> clazz, String string) {
        try {
            return E.valueOf(clazz, string.toUpperCase());
        } catch (Exception exception) {
            AzaleaCore.BROADCASTER.warn("Could not parse enum property value '" + clazz.getName() + "'.");
            AzaleaCore.BROADCASTER.warn(exception.getMessage());
            exception.printStackTrace();
            return null;
        }
    }
}
