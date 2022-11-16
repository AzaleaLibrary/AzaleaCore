package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.azalealibrary.azaleacore.room.playground.Playground;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.util.Map;

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
            data.set("world", playground.getWorld());
            data.set("spawn", playground.getSpawn());
            configuration.set(key, data);
        });
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        configuration.getKeys(false).forEach(key -> {
            Map<String, Object> data = (Map<String, Object>) configuration.get(key);
            String name = (String) data.get("name");
            World world = (World) data.get("world");
            Location spawn = (Location) data.get("spawn");
            add(key, new Playground(name, world, spawn));
        });
    }
}
