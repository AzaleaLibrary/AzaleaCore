package net.azalealibrary.azaleacore.foundation.configuration;

import net.azalealibrary.azaleacore.foundation.AzaleaException;
import net.azalealibrary.azaleacore.foundation.Serializable;
import net.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;

public interface Configurable extends Serializable {

    List<ConfigurableProperty<?>> getProperties();

    default <T> T getValue(ConfigurableProperty<T> property) {
        ConfigurableProperty<T> found = (ConfigurableProperty<T>) getProperties().stream()
                .filter(p -> p.equals(property))
                .findFirst()
                .orElseThrow(() -> new AzaleaException("Could not find property '" + property.getName() + "'."));

        return found.get();
    }

    default <T> void setValue(ConfigurableProperty<T> property, T value) {
        ConfigurableProperty<T> found = (ConfigurableProperty<T>) getProperties().stream()
                .filter(p -> p.equals(property))
                .findFirst()
                .orElseThrow(() -> new AzaleaException("Could not find property '" + property.getName() + "'."));

        found.set(value);
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
