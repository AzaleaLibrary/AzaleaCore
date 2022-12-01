package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.foundation.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.minigame.Minigame;
import com.azalealibrary.azaleacore.room.RoomConfiguration;
import org.bukkit.World;

public final class Round {

    private final World world;
    private final Minigame minigame;
    private final Broadcaster broadcaster;
    private final RoundTeams teams;
    private final RoomConfiguration configuration;

    public Round(World world, Minigame minigame, Broadcaster broadcaster, RoundTeams teams, RoomConfiguration configuration) {
        this.world = world;
        this.minigame = minigame;
        this.broadcaster = broadcaster;
        this.teams = teams;
        this.configuration = configuration;
    }

    public World getWorld() {
        return world;
    }

    public Minigame getMinigame() {
        return minigame;
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public RoundTeams getTeams() {
        return teams;
    }

    public RoomConfiguration getConfiguration() {
        return configuration;
    }
}
