package com.azalealibrary.azaleacore.foundation.configuration;

import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Objects;

public interface Configurable extends Serializable {

    List<ConfigurableProperty<?>> getProperties();

    default <T> void updateProperty(ConfigurableProperty<T> property, T value) {
        for (ConfigurableProperty<?> configurableProperty : getProperties()) {
            if (Objects.equals(configurableProperty.getName(), property.getName())) {
                if (configurableProperty.get().getClass() == property.get().getClass()) {
                    // FORGE this.clazz.equals(property.clazz) && this.name.equals(property.name);
                    ((ConfigurableProperty<T>) configurableProperty).set(value); // TODO - review
                }
            }
        }
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
