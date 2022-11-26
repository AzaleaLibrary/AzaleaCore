package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.Objects;

public final class AzaleaRoomApi extends AzaleaApi<Room> {

    private static final AzaleaRoomApi AZALEA_API = new AzaleaRoomApi();

    public static AzaleaRoomApi getInstance() {
        return AZALEA_API;
    }

    public void createRoom(Player player, String name, File map, Minigame minigame) {
        if (getEntries().size() >= AzaleaConfiguration.getInstance().getMaxRoomCount()) {
            throw new AzaleaException("Max room count reached.");
        }

        FileUtil.copyDirectory(map, new File(FileUtil.ROOMS, name));
        WorldCreator creator = new WorldCreator("azalea/rooms/" + name);
        Room room = new Room(player, name, minigame, Bukkit.createWorld(creator), map);

        add(name, room);
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        super.deserialize(configuration);

        for (File file : FileUtil.rooms()) {
            if (getKeys().stream().noneMatch(key -> key.equals(file.getName()))) {
                AzaleaCore.BROADCASTER.warn("Found stray room '" + file.getName() + "'.");

                if (AzaleaConfiguration.getInstance().shouldRemoveStrayRooms()) {
                    AzaleaCore.BROADCASTER.warn("Removing room '" + file.getName() + "'.");
                    FileUtil.delete(file);
                }
            }
        }
    }

    @Override
    protected void serializeEntry(ConfigurationSection section, Room entry) {
        section.set("name", entry.getName());
        section.set("world", entry.getWorld().getName());
        section.set("map", entry.getMap().getName());
        section.set("minigame", entry.getMinigame().getName());
        ConfigurationSection configs = section.createSection("configs");
        entry.getConfiguration().serialize(configs.createSection("room"));
        entry.getMinigame().serialize(configs.createSection("minigame"));
    }

    @Override
    protected Room deserializeEntry(ConfigurationSection section) {
        String name = section.getString("name");
        World world = Bukkit.getWorld(Objects.requireNonNull(section.getString("world")));
        File map = FileUtil.map(section.getString("map"));
        Minigame minigame = AzaleaMinigameApi.getInstance().get(section.getString("minigame")).get();
        minigame.deserialize(Objects.requireNonNull(section.getConfigurationSection("configs")));
        Room room = new Room(/* TODO everything as config? */ null, name, minigame, world, map);
        ConfigurationSection configs = Objects.requireNonNull(section.getConfigurationSection("configs"));
        room.getConfiguration().deserialize(Objects.requireNonNull(configs.getConfigurationSection("room")));
        room.getMinigame().deserialize(Objects.requireNonNull(configs.getConfigurationSection("minigame")));
        return room;
    }
}
