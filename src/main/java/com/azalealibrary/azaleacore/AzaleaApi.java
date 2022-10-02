package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.api.Round;
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
import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class AzaleaApi implements Serializable {

    private static final AzaleaApi AZALEA_API = new AzaleaApi();

    public static AzaleaApi getInstance() {
        return AZALEA_API;
    }

    private final HashMap<String, MinigameProvider<?>> MINIGAMES = new HashMap<>();
    private final List<MinigameRoom<?, ?>> ROOMS = new ArrayList<>();

    public ImmutableMap<String, MinigameProvider<?>> getMinigames() {
        return ImmutableMap.copyOf(MINIGAMES);
    }

    public List<MinigameRoom<?, ?>> getRooms() {
        return ROOMS;
    }

    public @Nullable MinigameProvider<?> getMinigame(String minigame) {
        return getMinigames().get(minigame);
    }

    public @Nullable MinigameRoom<?, ?> getRoom(String room) {
        return getRooms().stream()
                .filter(r -> r.getName().equals(room))
                .findFirst().orElse(null);
    }

    public void registerMinigame(String name, MinigameProvider<?> minigame) {
        if (MINIGAMES.containsKey(name)) {
            throw new IllegalArgumentException("Minigame with name '" + name + "' already registered.");
        }
        MINIGAMES.put(name, minigame);
    }

    public <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameRoom<M, R> createRoom(MinigameProvider<?> provider, String name, World lobby, String template) {
        Thread thread = new Thread(() -> FileUtil.copyDirectory(FileUtil.template(template), new File(FileUtil.ROOMS, name)));
        thread.start(); // TODO - review
        try { thread.join(); } catch (InterruptedException ignored) { }
        World world = Bukkit.createWorld(new WorldCreator("rooms/" + name));
        return createRoom(provider, name, lobby, world);
    }

    @SuppressWarnings("unchecked")
    public <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameRoom<M, R> createRoom(MinigameProvider<?> provider, String name, World lobby, World world) {
        MinigameRoom<M, R> room = new MinigameRoom<>(name, world, lobby, (M) provider.create(world));
        ROOMS.add(room);
        return room;
    }

    @Override
    public String getConfigName() {
        return "AzaleaApiRooms";
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
            room.getMinigame().serialize(data.createSection("configs"));
            rooms.add(data);
        });
        configuration.set("rooms", rooms);
    }

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        if (configuration.contains("rooms")) {
            List<HashMap<String, Object>> rooms = (List<HashMap<String, Object>>) configuration.getList("rooms");
            rooms.forEach(data -> {
                String name = (String) data.get("name");
                String minigame = (String) data.get("minigame");
                String world = (String) data.get("world");
                String lobby = (String) data.get("lobby");
                MinigameProvider<?> provider = getMinigames().get(minigame);
                MinigameRoom<?, ?> room = createRoom(provider, name, Bukkit.getWorld(lobby), Bukkit.getWorld(world));
                YamlConfiguration configs = new YamlConfiguration();
                HashMap<String, Object> map = (HashMap<String, Object>) data.get("configs");
                map.forEach(configs::set);
                room.getMinigame().deserialize(configs);
            });
        }

        // remove any unused rooms
        for (File file : FileUtil.rooms()) {
            getRooms().stream()
                    .filter(r -> r.getName().equals(file.getName()))
                    .findAny()
                    .ifPresentOrElse(r -> {}, () -> FileUtil.delete(file));
        }
    }

    @FunctionalInterface
    public interface MinigameProvider<M extends Minigame<?>> {
        M create(World world);
    }
}
