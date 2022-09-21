package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.api.broadcast.message.TitleMessage;
import com.azalealibrary.azaleacore.minigame.MinigameConfiguration;
import com.azalealibrary.azaleacore.minigame.MinigameController;
import org.bukkit.World;

import java.util.HashMap;

public final class AzaleaCoreApi {

    private static final TitleMessage SERVER_STOPPING = new TitleMessage(1, 2, 3, "Server stopping", "The server is stopping"); // TODO - own registry

    private static final HashMap<World, MinigameController<?>> MINIGAMES = new HashMap<>();

    public static <M extends Minigame> MinigameController<M> createController(M minigame, MinigameConfiguration configuration) {
        MinigameController<M> instance = new MinigameController<>(minigame, configuration.getWorld().getPlayers(), configuration);
        MINIGAMES.put(configuration.getWorld(), instance);
        return instance;
    }
}
