package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.broadcast.AzaleaBroadcaster;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import com.azalealibrary.azaleacore.foundation.registry.MinigameIdentifier;
import com.azalealibrary.azaleacore.foundation.teleport.SignTicker;
import com.azalealibrary.azaleacore.minigame.Minigame;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.Objects;

public final class AzaleaRoomApi extends AzaleaApi<Room> {

    private static final AzaleaRoomApi AZALEA_API = new AzaleaRoomApi();

    public static AzaleaRoomApi getInstance() {
        return AZALEA_API;
    }

    public Room createRoom(Player player, String name, File map, Minigame minigame) {
        if (getEntries().size() >= AzaleaConfiguration.getInstance().getMaxRoomCount()) {
            throw new AzaleaException("Max room count reached.");
        }

        // prepare files
        File roomFile = new File(FileUtil.ROOMS, name);
        FileUtil.copyDirectory(map, roomFile);
        FileUtil.delete(new File(roomFile, "uid.dat")); // otherwise spigot complains

        // create new world
        WorldCreator creator = new WorldCreator("azalea/rooms/" + name);
        World world = Bukkit.createWorld(creator);

        // create new room
        Room room = new Room(player, name, minigame, world);
        add(name, room);

        return room;
    }

    public void terminateRoom(Room room, @Nullable Message message) {
        if (room.getRoundTicker().isRunning()) {
            room.stop(message);
        }

        room.teleportAllToLobby();
        SignTicker.getInstance().removeAll(room);
        Bukkit.unloadWorld(room.getWorld(), false);
        FileUtil.delete(Objects.requireNonNull(FileUtil.room(room.getName())));

        remove(room);
    }

    public @Nullable Room getRoom(Player player) {
        return getObjects().stream()
                .filter(r -> r.getWorld().getPlayers().contains(player))
                .findFirst().orElse(null);
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        super.deserialize(configuration);

        for (File file : FileUtil.rooms()) {
            if (getKeys().stream().noneMatch(key -> key.equals(file.getName()))) {
                AzaleaBroadcaster.getInstance().warn("Found stray room '" + file.getName() + "'.");

                if (AzaleaConfiguration.getInstance().shouldRemoveStrayRooms()) {
                    AzaleaBroadcaster.getInstance().warn("Removing room '" + file.getName() + "'.");
                    FileUtil.delete(file);
                }
            }
        }
    }

    @Override
    protected void serializeEntry(ConfigurationSection section, Room entry) {
        section.set("name", entry.getName());
        section.set("world", entry.getWorld().getName());
        section.set("minigame", entry.getMinigame().getIdentifier().toString());
        ConfigurationSection configs = section.createSection("configs");
        entry.getConfiguration().serialize(configs.createSection("room"));
        entry.getMinigame().serialize(configs.createSection("minigame"));
    }

    @Override
    protected Room deserializeEntry(ConfigurationSection section) {
        String name = section.getString("name");
        World world = Bukkit.getWorld(Objects.requireNonNull(section.getString("world")));
        Minigame minigame = Minigame.create(new MinigameIdentifier(Objects.requireNonNull(section.getString("minigame"))));
        Room room = new Room(null, name, minigame, world);
        ConfigurationSection configs = Objects.requireNonNull(section.getConfigurationSection("configs"));
        room.getConfiguration().deserialize(Objects.requireNonNull(configs.getConfigurationSection("room")));
        room.getMinigame().deserialize(Objects.requireNonNull(configs.getConfigurationSection("minigame")));
        return room;
    }
}
