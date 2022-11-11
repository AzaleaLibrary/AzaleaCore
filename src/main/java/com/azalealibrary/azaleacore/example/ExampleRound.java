package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.api.Round;
import com.azalealibrary.azaleacore.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.minigame.round.RoundEvent;
import com.azalealibrary.azaleacore.minigame.round.RoundTeams;

public class ExampleRound extends Round<ExampleMinigame> {

    private final ExampleEventListener listener;

    public ExampleRound(RoundTeams teams) {
        super(teams);
        listener = new ExampleEventListener(this);
    }

    @Override
    public void onSetup(RoundEvent.Setup<ExampleMinigame> event) {
        super.onSetup(event);
        System.out.println("onSetup");

        getRoundTeams().getPlayers().forEach(player -> player.teleport(event.getRoom().getMinigame().getSpawn()));

        listener.enable();
    }

    @Override
    public void onStart(RoundEvent.Start<ExampleMinigame> event) {
        System.out.println("onStart");
    }

    @Override
    public void onTick(RoundEvent.Tick<ExampleMinigame> event) {
        System.out.println(getTick());
    }

    @Override
    public void onWin(RoundEvent.Win<ExampleMinigame> event) {
        super.onWin(event);
        System.out.println("onWin");
    }

    @Override
    public void onEnd(RoundEvent.End<ExampleMinigame> event) {
        super.onEnd(event);
        System.out.println("onEnd");

        listener.disable();
    }
}
