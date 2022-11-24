package com.azalealibrary.azaleacore.foundation.broadcast;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;

public final class AzaleaBroadcaster extends Broadcaster {

    public AzaleaBroadcaster() {
        super(AzaleaCore.PLUGIN_ID, null, null);
    }

    @Override
    public void broadcast(Message message, Chanel chanel) {
        if (chanel != Chanel.LOBBY) {
            throw new UnsupportedOperationException("Azalea broadcaster can only broadcast to the lobby or a player.");
        }
        super.broadcast(message, chanel);
    }
}
