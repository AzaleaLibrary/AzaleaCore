package com.azalealibrary.azaleacore.api.configuration;

import com.azalealibrary.azaleacore.serialization.Serializable;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.util.List;

public interface Configurable extends Serializable {

    List<CommandProperty<?>> getProperties();

    @Override
    default void serialize(@Nonnull ConfigurationSection configuration) {
        for (CommandProperty<?> property : getProperties()) {
            property.serialize(configuration);
        }
    }

    @Override
    default void deserialize(@Nonnull ConfigurationSection configuration) {
        for (CommandProperty<?> property : getProperties()) {
            property.deserialize(configuration);
        }
    }
}
