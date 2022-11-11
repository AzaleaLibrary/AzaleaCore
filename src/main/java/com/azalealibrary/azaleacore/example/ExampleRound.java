package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.api.Round;
import com.azalealibrary.azaleacore.minigame.round.RoundEvent;
import com.azalealibrary.azaleacore.minigame.round.RoundTeams;

public class ExampleRound extends Round {

    private final ExampleEventListener listener;

    public ExampleRound(RoundTeams teams) {
        super(teams);
        listener = new ExampleEventListener(this);
    }

    @Override
    public void onSetup(RoundEvent.Setup event) {
        super.onSetup(event);
        System.out.println("onSetup");

        getRoundTeams().getPlayers().forEach(player -> player.teleport(event.getRoom().<ExampleMinigame>getMinigame().getSpawn()));

        listener.enable();
    }

    @Override
    public void onStart(RoundEvent.Start event) {
        System.out.println("onStart");
    }

    @Override
    public void onTick(RoundEvent.Tick event) {
        System.out.println(getTick());
    }

    @Override
    public void onWin(RoundEvent.Win event) {
        super.onWin(event);
        System.out.println("onWin");
    }

    @Override
    public void onEnd(RoundEvent.End event) {
        super.onEnd(event);
        System.out.println("onEnd");

        listener.disable();
    }
}
