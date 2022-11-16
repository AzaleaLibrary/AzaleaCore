package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.Minigame;
import org.bukkit.World;

public final class AzaleaMinigameApi extends AzaleaApi<AzaleaMinigameApi.MinigameProvider> {

    private static final AzaleaMinigameApi AZALEA_API = new AzaleaMinigameApi();

    public static AzaleaMinigameApi getInstance() {
        return AZALEA_API;
    }

    @FunctionalInterface
    public interface MinigameProvider {
        Minigame create(World world);
    }
}
