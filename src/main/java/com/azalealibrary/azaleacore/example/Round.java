package com.azalealibrary.azaleacore.example;

import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.round.RoundEvent;
import com.google.common.eventbus.Subscribe;
import org.bukkit.util.Vector;

public final class Round {
    @Subscribe
    public void onStart(RoundEvent.Start event) {
        Vector spawn = event.getRound().getMinigame().getValue(Registry.SPAWN.get());
        event.getRound().getParty().broadcast(ChatMessage.info(spawn.toString()));
    }

    @Subscribe
    public void onTick(RoundEvent.Tick event) {
        if (event.getTick().isFullSecond()) {
            event.getRound().getParty().broadcast(ChatMessage.info(String.valueOf(event.getTick().getSeconds())));
        }
    }

    @Subscribe
    public void onWin(RoundEvent.Win event) {
        event.getRound().getParty().broadcast(ChatMessage.info("win"));
    }

    @Subscribe
    public void onEnd(RoundEvent.End event) {
        event.getRound().getParty().broadcast(ChatMessage.info("end"));
    }
}
