package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.foundation.broadcast.message.Message;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

public interface Executor {
    /**
     * Called when command is issued by the <b>sender</b> with given <b>arguments</b>.
     *
     * @param sender Command issuer. (ei. player, via terminal).
     * @param arguments Command arguments provided when triggering command.
     * @return Message to output. Return <i>null</i> for no output message.
     */
    @Nullable Message execute(CommandSender sender, Arguments arguments);
}
