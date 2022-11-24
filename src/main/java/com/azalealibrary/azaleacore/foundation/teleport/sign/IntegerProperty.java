package com.azalealibrary.azaleacore.foundation.teleport.sign;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import org.bukkit.command.CommandSender;

public class IntegerProperty extends Property<Integer> {

    public IntegerProperty(String name, Integer defaultValue, boolean required) {
        super(name, defaultValue, required);
    }

    @Override
    public void set(CommandSender sender, Arguments arguments) {
        set(Integer.parseInt(arguments.get( arguments.size() - 1)));
    }
}
