package com.azalealibrary.azaleacore.command.core;

import com.azalealibrary.azaleacore.room.broadcast.message.Message;
import org.bukkit.command.CommandSender;

import javax.annotation.Nullable;

public interface Executor extends Conditional {

    @Nullable Message execute(CommandSender sender, Arguments arguments);
}
