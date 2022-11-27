package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.foundation.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import org.bukkit.World;

import java.util.List;

public final class Round implements Configurable {

    private final World world;
    private final Broadcaster broadcaster;
    private final RoundTeams teams;
    private final RoundLifeCycle listener;
    private final List<ConfigurableProperty<?>> properties;

    public Round(World world, Broadcaster broadcaster, RoundTeams teams, RoundLifeCycle listener, List<ConfigurableProperty<?>> properties) {
        this.world = world;
        this.broadcaster = broadcaster;
        this.teams = teams;
        this.listener = listener;
        this.properties = properties;
    }

    public World getWorld() {
        return world;
    }

    public Broadcaster getBroadcaster() {
        return broadcaster;
    }

    public RoundTeams getTeams() {
        return teams;
    }

    public RoundLifeCycle getListener() {
        return listener;
    }

    @Override
    public List<ConfigurableProperty<?>> getProperties() {
        return properties;
    }
}
