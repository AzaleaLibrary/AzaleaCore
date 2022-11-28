package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.core.MinigameTeam;
import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.foundation.registry.AzaleaRegistry;
import com.azalealibrary.azaleacore.foundation.registry.MinigameIdentifier;
import com.azalealibrary.azaleacore.round.RoundLifeCycle;

import java.util.List;

public final class Minigame implements Configurable {

    private final MinigameIdentifier identifier;
    private final List<RoundLifeCycle> listeners;
    private final List<WinCondition> winConditions;
    private final List<MinigameTeam> possibleTeams;
    private final List<ConfigurableProperty<?>> properties;

    private Minigame(MinigameIdentifier identifier, List<RoundLifeCycle> listeners, List<WinCondition> winConditions, List<MinigameTeam> possibleTeams, List<ConfigurableProperty<?>> properties) {
        this.identifier = identifier;
        this.listeners = listeners;
        this.winConditions = winConditions;
        this.possibleTeams = possibleTeams;
        this.properties = properties;
    }

    public MinigameIdentifier getIdentifier() {
        return identifier;
    }

    public List<RoundLifeCycle> getListeners() {
        return listeners;
    }

    public List<WinCondition> getWinConditions() {
        return winConditions;
    }

    public List<MinigameTeam> getPossibleTeams() {
        return possibleTeams;
    }

    @Override
    public List<ConfigurableProperty<?>> getProperties() {
        return properties;
    }

    public static Minigame create(MinigameIdentifier identifier) {
        List<RoundLifeCycle> listeners = AzaleaRegistry.ROUND.getAll(identifier);
        List<WinCondition> winConditions = AzaleaRegistry.WIN_CONDITION.getAll(identifier);
        List<MinigameTeam> possibleTeams = AzaleaRegistry.TEAM.getAll(identifier);
        List<ConfigurableProperty<?>> properties = AzaleaRegistry.PROPERTY.getAll(identifier);
        return new Minigame(identifier, listeners, winConditions, possibleTeams, properties);
    }
}
