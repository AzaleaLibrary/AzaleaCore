package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.broadcast.message.TitleMessage;

public class WinCondition {

    private final TitleMessage titleMessage;
    private final int winAward;

    public WinCondition(TitleMessage titleMessage, int winAward) {
        this.titleMessage = titleMessage;
        this.winAward = winAward;
    }

    public TitleMessage getTitleMessage() {
        return titleMessage;
    }

    public int getWinAward() {
        return winAward;
    }
}
