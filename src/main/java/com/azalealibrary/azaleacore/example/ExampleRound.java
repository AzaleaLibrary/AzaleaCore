package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.api.Round;
import com.azalealibrary.azaleacore.minigame.round.RoundEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class ExampleRound extends Round<ExampleMinigame> {

    public ExampleRound(List<Player> players) {
        super(players);
    }

    public boolean isAllPlayersDead() {
        return getPlayers().size() == 0;
    }

    @Override
    public void onSetup(RoundEvent.Setup<ExampleMinigame> event) {
        Location location = event.getMinigame().getSpawn();
        getPlayers().forEach(player -> player.teleport(location));

//        getBroadcaster().broadcast(new ChatMessage("onSetup"));
    }

    @Override
    public void onStart(RoundEvent.Start<ExampleMinigame> event) {
//        getBroadcaster().broadcast(new ChatMessage("onStart"));
    }

    @Override
    public void onTick(RoundEvent.Tick<ExampleMinigame> event) {
//        getBroadcaster().broadcast(new ChatMessage(String.valueOf(getTick())));
    }

    @Override
    public void onWin(RoundEvent.Win<ExampleMinigame> event) {
//        event.restart();

//        getBroadcaster().broadcast(new ChatMessage("onWin " + event.getCondition().getTitleMessage().getMessage()));
    }

    @Override
    public void onEnd(RoundEvent.End<ExampleMinigame> event) {
//        getBroadcaster().broadcast(new ChatMessage("onEnd"));
    }
}
