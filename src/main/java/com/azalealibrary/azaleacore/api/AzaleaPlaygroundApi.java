package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.azalealibrary.azaleacore.room.Playground;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;

public final class AzaleaPlaygroundApi extends AzaleaApi<Playground> implements Serializable {

    private static final AzaleaPlaygroundApi AZALEA_API = new AzaleaPlaygroundApi();

    public static AzaleaPlaygroundApi getInstance() {
        return AZALEA_API;
    }

    @Override
    public String getConfigName() {
        return "playgrounds";
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        getEntries().forEach((key, playground) -> {
            YamlConfiguration data = new YamlConfiguration();
            data.set("name", playground.getName());
            data.set("world", playground.getWorld().getName());
            data.set("spawn", playground.getSpawn());
            configuration.set(key, data);
        });
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        configuration.getKeys(false).forEach(key -> {
            ConfigurationSection data = (ConfigurationSection) configuration.get(key);
            String name = (String) data.get("name");
            World world = Bukkit.getWorld((String) data.get("world"));
            Location spawn = (Location) data.get("spawn");
            add(key, new Playground(name, world, spawn));
        });
    }
}
