package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.api.broadcast.MinigameBroadcaster;
import com.azalealibrary.azaleacore.api.broadcast.message.TitleMessage;
import com.azalealibrary.azaleacore.api.minigame.Minigame;
import com.google.common.collect.ImmutableList;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

public class ExampleMinigame extends Minigame<ExampleRound> {

    public ExampleMinigame(World world) {
        super(world);
    }

    @Override
    public String getName() {
        return "ExampleMinigame";
    }

    @Override
    public ImmutableList<WinCondition<ExampleRound>> getWinConditions() {
        return ImmutableList.of(new WinCondition<>(new TitleMessage("No players :("), 123, round -> round.getCurrentExampleState() == 0));
    }

    @Override
    public ExampleRound newRound(List<Player> players) {
        return new ExampleRound(players, new MinigameBroadcaster(getName(), players));
    }

    public int getExampleProperty() {
        return 0;
    }
}
