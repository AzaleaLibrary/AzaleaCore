package com.azalealibrary.azaleacore.serialization;

import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;

public interface Serializable {

    String getConfigName();

    void serialize(@Nonnull ConfigurationSection configuration);

    void deserialize(@Nonnull ConfigurationSection configuration);
}
