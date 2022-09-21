package com.azalealibrary.azaleacore.event;

import com.azalealibrary.azaleacore.api.Minigame;
import com.azalealibrary.azaleacore.api.WinCondition;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;

public class MinigameEvent<M extends Minigame> {

    private final M minigame;
    private final List<Player> participants;
    private final BossBar timer;
    private WinCondition winCondition;
    private int currentTick = 0;

    public MinigameEvent(M game, List<Player> participants, BossBar timer) {
        this.minigame = game;
        this.participants = participants;
        this.timer = timer;
    }

    public M getMinigame() {
        return minigame;
    }

    public List<Player> getParticipants() {
        return participants;
    }

    public BossBar getTimer() {
        return timer;
    }

    public int getTick() {
        return currentTick;
    }

    public void setTick(int tick) {
        currentTick = tick;
    }

    public WinCondition getWinCondition() {
        return winCondition;
    }

    public void setWinCondition(WinCondition win) {
        winCondition = win;
    }
}
