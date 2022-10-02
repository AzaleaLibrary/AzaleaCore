package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.api.Round;
import com.azalealibrary.azaleacore.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.minigame.round.RoundEvent;
import org.bukkit.entity.Player;

import java.util.List;

public class ExampleRound extends Round<ExampleMinigame> {

    private final ExampleEventListener listener;

    public ExampleRound(List<Player> players, Broadcaster broadcaster) {
        super(players, broadcaster);
        listener = new ExampleEventListener(this);
    }

    public boolean isAllPlayersDead() {
        return getPlayers().size() == 0;
    }

    @Override
    public void onSetup(RoundEvent.Setup<ExampleMinigame> event) {
        getBroadcaster().broadcast(new ChatMessage("onSetup"));

        listener.enable();
    }

    @Override
    public void onStart(RoundEvent.Start<ExampleMinigame> event) {
        getBroadcaster().broadcast(new ChatMessage("onStart"));
    }

    @Override
    public void onTick(RoundEvent.Tick<ExampleMinigame> event) {
        getBroadcaster().broadcast(new ChatMessage(String.valueOf(getTick())));
    }

    @Override
    public void onWin(RoundEvent.Win<ExampleMinigame> event) {
        getBroadcaster().broadcast(new ChatMessage("onWin"));
    }

    @Override
    public void onEnd(RoundEvent.End<ExampleMinigame> event) {
        getBroadcaster().broadcast(new ChatMessage("onEnd"));

        listener.disable();
    }
}
