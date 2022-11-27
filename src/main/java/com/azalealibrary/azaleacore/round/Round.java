package com.azalealibrary.azaleacore.round;

import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;

import java.util.List;

public final class Round implements Configurable {

    private final RoundTeams teams;
    private final RoundLifeCycle listener;
    private final List<ConfigurableProperty<?>> properties;

    public Round(RoundTeams teams, RoundLifeCycle listener, List<ConfigurableProperty<?>> properties) {
        this.teams = teams;
        this.listener = listener;
        this.properties = properties;
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
