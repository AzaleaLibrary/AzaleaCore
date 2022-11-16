package com.azalealibrary.azaleacore.api;

import com.google.common.collect.ImmutableMap;
import org.bukkit.World;

import javax.annotation.Nullable;
import java.util.HashMap;

public final class AzaleaMinigameApi {

    private static final AzaleaMinigameApi AZALEA_API = new AzaleaMinigameApi();

    public static AzaleaMinigameApi getInstance() {
        return AZALEA_API;
    }

    private final HashMap<String, MinigameProvider> minigames = new HashMap<>();

    public ImmutableMap<String, MinigameProvider> getMinigames() {
        return ImmutableMap.copyOf(minigames);
    }

    public @Nullable MinigameProvider getMinigame(String minigame) {
        return getMinigames().get(minigame);
    }

    public void registerMinigame(String name, MinigameProvider minigame) {
        if (minigames.containsKey(name)) {
            throw new IllegalArgumentException("Minigame with name '" + name + "' already registered.");
        }
        minigames.put(name, minigame);
    }

    @FunctionalInterface
    public interface MinigameProvider {
        Minigame create(World world);
    }
}
