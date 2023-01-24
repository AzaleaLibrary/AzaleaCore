package com.azalealibrary.azaleacore.minigame;

import com.azalealibrary.azaleacore.api.MinigameTeam;
import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.command.CommandNode;
import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.foundation.registry.AzaleaRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class Minigame implements Configurable {

    private final MinigameIdentifier identifier;
    private final List<Supplier<Object>> listeners;
    private final List<WinCondition> winConditions;
    private final List<MinigameTeam> possibleTeams;
    private final List<CommandNode> commands;
    private final List<ConfigurableProperty<?>> properties;

    private Minigame(MinigameIdentifier identifier, List<Supplier<Object>> listeners, List<WinCondition> winConditions, List<MinigameTeam> possibleTeams, List<CommandNode> commands, List<ConfigurableProperty<?>> properties) {
        this.identifier = identifier;
        this.listeners = listeners;
        this.winConditions = winConditions;
        this.possibleTeams = possibleTeams;
        this.commands = commands;
        this.properties = properties;
    }

    public MinigameIdentifier getIdentifier() {
        return identifier;
    }

    public List<Supplier<Object>> getListeners() {
        return listeners;
    }

    public List<WinCondition> getWinConditions() {
        return winConditions;
    }

    public List<MinigameTeam> getPossibleTeams() {
        return possibleTeams;
    }

    public List<CommandNode> getCommands() {
        return commands;
    }

    @Override
    public List<ConfigurableProperty<?>> getProperties() {
        return properties;
    }

    public static Minigame create(MinigameIdentifier identifier) {
        List<Supplier<Object>> listeners = AzaleaRegistry.ROUND.getAll(identifier);
        List<WinCondition> winConditions = AzaleaRegistry.WIN_CONDITION.getAll(identifier);
        List<MinigameTeam> possibleTeams = AzaleaRegistry.TEAM.getAll(identifier);
        List<CommandNode> commands = AzaleaRegistry.COMMAND.getAll(identifier);

        List<ConfigurableProperty<?>> properties = new ArrayList<>();
        for (Supplier<ConfigurableProperty<?>> supplier : AzaleaRegistry.PROPERTY.getAll(identifier)) {
            properties.add(supplier.get());
        }
        return new Minigame(identifier, listeners, winConditions, possibleTeams, commands, properties);
    }
}
