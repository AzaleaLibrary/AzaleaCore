package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.Minigame;

public final class AzaleaMinigameApi extends AzaleaApi<Minigame> {

    private static final AzaleaMinigameApi AZALEA_API = new AzaleaMinigameApi();

    public static AzaleaMinigameApi getInstance() {
        return AZALEA_API;
    }
}
