package com.azalealibrary.azaleacore.foundation.configuration;

import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;

public interface Configurable extends Serializable {

    List<ConfigurableProperty<?>> getProperties();

    default <T> T getValue(ConfigurableProperty<T> property) {
        ConfigurableProperty<?> found = getProperties().stream()
                .filter(p -> p.equals(property))
                .findFirst()
                .orElseThrow(() -> new AzaleaException("Could not find property '" + property.getName() + "'."));

        return (T) found.get();
    }

    default <T> void setValue(ConfigurableProperty<T> property, T value) {
        ConfigurableProperty<?> found = getProperties().stream()
                .filter(p -> p.equals(property))
                .findFirst()
                .orElseThrow(() -> new AzaleaException("Could not find property '" + property.getName() + "'."));

        ((ConfigurableProperty<T>) found).set(value);
    }

    @Override
    default void serialize(@Nonnull ConfigurationSection configuration) {
        for (ConfigurableProperty<?> property : getProperties()) {
            property.serialize(configuration);
        }
    }

    @Override
    default void deserialize(@Nonnull ConfigurationSection configuration) {
        for (ConfigurableProperty<?> property : getProperties()) {
            property.deserialize(configuration);
        }
    }
}
