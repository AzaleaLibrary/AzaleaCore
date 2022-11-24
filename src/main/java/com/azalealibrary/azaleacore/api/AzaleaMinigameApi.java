package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.Minigame;
import org.bukkit.configuration.ConfigurationSection;

import java.util.function.Supplier;

public final class AzaleaMinigameApi extends AzaleaApi<Supplier<Minigame>> {

    private static final AzaleaMinigameApi AZALEA_API = new AzaleaMinigameApi();

    public static AzaleaMinigameApi getInstance() {
        return AZALEA_API;
    }

    @Override
    protected void serializeEntry(ConfigurationSection section, Supplier<Minigame> entry) { }

    @Override
    protected Supplier<Minigame> deserializeEntry(ConfigurationSection section) {
        return () -> null;
    }
}
