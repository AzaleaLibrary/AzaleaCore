package com.azalealibrary.azaleacore.foundation.configuration;

import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;

public interface Configurable extends Serializable {

    List<Property<?>> getProperties();

    @Override
    default void serialize(@Nonnull ConfigurationSection configuration) {
        for (Property<?> property : getProperties()) {
            property.serialize(configuration);
        }
    }

    @Override
    default void deserialize(@Nonnull ConfigurationSection configuration) {
        for (Property<?> property : getProperties()) {
            property.deserialize(configuration);
        }
    }
}
