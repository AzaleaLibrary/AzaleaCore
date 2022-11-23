package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Objects;

public final class AzaleaRoomApi extends AzaleaApi<Room> implements Serializable {

    private static final AzaleaRoomApi AZALEA_API = new AzaleaRoomApi();

    public static AzaleaRoomApi getInstance() {
        return AZALEA_API;
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        getEntries().forEach((key, room) -> {
            ConfigurationSection section = configuration.createSection(key);
            section.set("name", room.getName());
            section.set("world", room.getWorld().getName());
            section.set("map", room.getMap().getName());
            section.set("minigame", room.getMinigame().getName());
            room.getMinigame().serialize(section.createSection("configs"));
            configuration.set(key, section);
        });
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        configuration.getKeys(false).forEach(key -> {
            ConfigurationSection data = Objects.requireNonNull(configuration.getConfigurationSection(key));
            String name = data.getString("name");
            World world = Bukkit.getWorld(Objects.requireNonNull(data.getString("world")));
            File map = FileUtil.map(data.getString("map"));
            Minigame minigame = AzaleaMinigameApi.getInstance().get(data.getString("minigame")).get();
            minigame.deserialize(Objects.requireNonNull(data.getConfigurationSection("configs")));
            add(name, new Room(name, minigame, world, map));
        });

        removeStrayRooms();
    }

    private void removeStrayRooms() {
        for (File file : FileUtil.rooms()) {
            if (getKeys().stream().noneMatch(key -> key.equals(file.getName()))) {
                FileUtil.delete(file);
            }
        }
    }
}
