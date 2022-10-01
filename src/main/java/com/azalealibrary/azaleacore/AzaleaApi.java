package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.minigame.MinigameRoom;
import com.azalealibrary.azaleacore.serialization.Serializable;
import com.azalealibrary.azaleacore.util.FileUtil;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class AzaleaApi implements Serializable {

    private static final AzaleaApi AZALEA_API = new AzaleaApi();

    public static AzaleaApi getInstance() {
        return AZALEA_API;
    }

    private final HashMap<String, MinigameProvider<?>> REGISTERED_MINIGAMES = new HashMap<>();
    private final List<MinigameRoom<?, ?>> MINIGAME_ROOMS = new ArrayList<>();

    public ImmutableMap<String, MinigameProvider<?>> getRegisteredMinigames() {
        return ImmutableMap.copyOf(REGISTERED_MINIGAMES);
    }

    public List<MinigameRoom<?, ?>> getMinigameRooms() {
        return MINIGAME_ROOMS;
    }

    public void registerMinigame(String name, MinigameProvider<?> minigame) {
        if (REGISTERED_MINIGAMES.containsKey(name)) {
            throw new IllegalArgumentException("Minigame with name '" + name + "' already registered.");
        }
        REGISTERED_MINIGAMES.put(name, minigame);
    }

    public <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameRoom<M, R> createRoom(MinigameProvider<?> provider, String name, String template) {
        File original = new File(Bukkit.getWorldContainer(), "templates/" + template);
        File destination = new File(Bukkit.getWorldContainer(), "rooms/" + name);
        FileUtil.copyDirectory(original, destination);
        World world = Bukkit.createWorld(new WorldCreator("rooms/" + name));
        return createRoom(provider, name, world);
    }

    @SuppressWarnings("unchecked")
    public <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameRoom<M, R> createRoom(MinigameProvider<?> provider, String name, World world) {
        MinigameRoom<M, R> room = new MinigameRoom<>(name, world, (M) provider.create(world));
        MINIGAME_ROOMS.add(room);
        return room;
    }

    @Override
    public String getConfigName() {
        return "AzaleaApiRooms";
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        List<ConfigurationSection> rooms = new ArrayList<>();
        getMinigameRooms().forEach(room -> {
            YamlConfiguration data = new YamlConfiguration();
            data.set("name", room.getName());
            data.set("minigame", room.getMinigame().getConfigName());
            data.set("world", room.getWorld().getName());
            room.getMinigame().serialize(data.createSection("configs"));
            rooms.add(data);
        });
        configuration.set("rooms", rooms);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        List<HashMap<String, Object>> rooms = (List<HashMap<String, Object>>) configuration.getList("rooms");
        rooms.forEach(data -> {
            String name = (String) data.get("name");
            String minigame = (String) data.get("minigame");
            String world = (String) data.get("world");

            MinigameProvider<?> provider = getRegisteredMinigames().get(minigame);
            MinigameRoom<?, ?> room = createRoom(provider, name, Bukkit.getWorld(world));

            YamlConfiguration configs = new YamlConfiguration();
            HashMap<String, Object> map = (HashMap<String, Object>) data.get("configs");
            map.forEach(configs::set);

            room.getMinigame().deserialize(configs);
        });
    }

    @FunctionalInterface
    public interface MinigameProvider<M extends Minigame<?>> {
        M create(World world);
    }
}
