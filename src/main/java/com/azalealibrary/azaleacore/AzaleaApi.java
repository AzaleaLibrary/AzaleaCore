package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.azalealibrary.azaleacore.api.minigame.round.Round;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import com.azalealibrary.azaleacore.serialization.Serializable;
import com.azalealibrary.azaleacore.serialization.Serialization;
import com.google.common.collect.ImmutableMap;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class AzaleaApi {

    private static final HashMap<String, MinigameProvider<?>> REGISTERED_MINIGAMES = new HashMap<>();
    private static final HashMap<World, MinigameController<?, ?>> MINIGAME_ROOMS = new HashMap<>();

    public static ImmutableMap<String, MinigameProvider<?>> getRegisteredMinigames() {
        return ImmutableMap.copyOf(REGISTERED_MINIGAMES);
    }

    public static ImmutableMap<World, MinigameController<?, ?>> getMinigameRooms() {
        return ImmutableMap.copyOf(MINIGAME_ROOMS);
    }

    public static void registerMinigame(String name, MinigameProvider<?> minigame) {
        if (REGISTERED_MINIGAMES.containsKey(name)) {
            throw new IllegalArgumentException("Minigame with name '" + name + "' already registered.");
        }
        REGISTERED_MINIGAMES.put(name, minigame);
    }

    @SuppressWarnings({"unchecked"})
    public static <M extends Minigame<? extends Round<M>>, R extends Round<M>> MinigameController<M, R> createMinigameRoom(World world, MinigameProvider<?> provider) {
        MinigameController<M, R> controller = new MinigameController<>((M) provider.create(world));
        MINIGAME_ROOMS.put(world, controller);
        return controller;
    }

    public static void load(Main instance) {
        Serialization.load(instance, new Serializable() {
            @Override
            public String getConfigName() {
                return "AzaleaApiData";
            }

            @Override
            public void serialize(@Nonnull ConfigurationSection configuration) { }

            @SuppressWarnings({"unchecked", "ConstantConditions", "SuspiciousMethodCalls"})
            @Override
            public void deserialize(@Nonnull ConfigurationSection configuration) {
                List<HashMap<String, Object>> minigames = (List<HashMap<String, Object>>) configuration.getList("minigames");

                minigames.forEach(data -> {
                    MinigameProvider<?> provider = AzaleaApi.getRegisteredMinigames().get(data.get("name"));
                    World world = Bukkit.getWorld((String) data.get("room"));
                    MinigameController<?, ?> controller = createMinigameRoom(world, provider);
                    YamlConfiguration configs = new YamlConfiguration();
                    HashMap<String, Object> map = (HashMap<String, Object>) data.get("configs");
                    map.forEach(configs::set);
                    controller.getMinigame().deserialize(configs);
                });
            }
        });
    }

    public static void unload(Main instance) {
        Serialization.save(instance, new Serializable() {
            @Override
            public String getConfigName() {
                return "AzaleaApiData";
            }

            @Override
            public void serialize(@Nonnull ConfigurationSection configuration) {
                List<ConfigurationSection> minigames = new ArrayList<>();

                getMinigameRooms().values().forEach(controller -> {
                    YamlConfiguration minigame = new YamlConfiguration();
                    minigame.set("name", controller.getMinigame().getConfigName());
                    minigame.set("room", controller.getMinigame().getWorld().getName());
                    ConfigurationSection configs = minigame.createSection("configs");
                    controller.getMinigame().serialize(configs);
                    minigames.add(minigame);
                });

                configuration.set("minigames", minigames);
            }

            @Override
            public void deserialize(@Nonnull ConfigurationSection configuration) { }
        });
    }

    @FunctionalInterface
    public interface MinigameProvider<M extends Minigame<?>> {
        M create(World world);
    }
}
