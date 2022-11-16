package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.room.MinigameRoom;
import com.azalealibrary.azaleacore.serialization.Serializable;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class AzaleaRoomApi implements Serializable {

    private static final AzaleaRoomApi AZALEA_API = new AzaleaRoomApi();

    public static AzaleaRoomApi getInstance() {
        return AZALEA_API;
    }

    private final List<MinigameRoom> rooms = new ArrayList<>();

    public List<MinigameRoom> getRooms() {
        return rooms;
    }

    public @Nullable MinigameRoom getRoom(String room) {
        return getRooms().stream()
                .filter(r -> r.getName().equals(room))
                .findFirst().orElse(null);
    }

    public MinigameRoom createRoom(AzaleaMinigameApi.MinigameProvider provider, String name, World lobby, File template) {
        FileUtil.copyDirectory(template, new File(FileUtil.ROOMS, name));
        World world = Bukkit.createWorld(new WorldCreator("rooms/" + name));
        return createRoom(provider, name, lobby, world);
    }

    public MinigameRoom createRoom(AzaleaMinigameApi.MinigameProvider provider, String name, World lobby, World world) {
        MinigameRoom room = new MinigameRoom(name, world, lobby, provider.create(world));
        rooms.add(room);
        return room;
    }

    @Override
    public String getConfigName() {
        return "rooms";
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        List<ConfigurationSection> rooms = new ArrayList<>();
        getRooms().forEach(room -> {
            YamlConfiguration data = new YamlConfiguration();
            data.set("name", room.getName());
            data.set("minigame", room.getMinigame().getName());
            data.set("world", room.getWorld().getName());
            data.set("lobby", room.getLobby().getName());
            data.set("toWorldSigns", room.getSignTicker().getToWorldSigns());
            data.set("toLobbySigns", room.getSignTicker().getToLobbySigns());
            room.getMinigame().serialize(data.createSection("configs"));
            rooms.add(data);
        });
        configuration.set("rooms", rooms);
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        if (configuration.contains("rooms")) {
            List<HashMap<String, Object>> rooms = (List<HashMap<String, Object>>) configuration.getList("rooms");
            rooms.forEach(data -> {
                String name = (String) data.get("name");
                String minigame = (String) data.get("minigame");
                String world = (String) data.get("world");
                String lobby = (String) data.get("lobby");
                AzaleaMinigameApi.MinigameProvider provider = AzaleaMinigameApi.getInstance().getMinigames().get(minigame);
                MinigameRoom room = createRoom(provider, name, Bukkit.getWorld(lobby), Bukkit.getWorld(world));
                YamlConfiguration configs = new YamlConfiguration();
                List<Location> toWorldSigns = (List<Location>) data.get("toWorldSigns");
                toWorldSigns.forEach(sign -> room.getSignTicker().getToWorldSigns().add(sign));
                List<Location> toLobbySigns = (List<Location>) data.get("toLobbySigns");
                toLobbySigns.forEach(sign -> room.getSignTicker().getToLobbySigns().add(sign));
                HashMap<String, Object> map = (HashMap<String, Object>) data.get("configs");
                map.forEach(configs::set);
                room.getMinigame().deserialize(configs);
            });
        }

        for (File file : FileUtil.rooms()) {
            if (getRooms().stream().noneMatch(room -> room.getName().equals(file.getName()))) {
                FileUtil.delete(file); // remove any unused rooms
            }
        }
    }
}
