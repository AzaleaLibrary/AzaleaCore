package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import org.bukkit.command.CommandSender;
import org.bukkit.util.Vector;

import java.util.List;

public class VectorListProperty extends ListProperty<Vector> {

    public VectorListProperty(String name, List<Vector> defaultValue, boolean required) {
        super(name, defaultValue, required);
    }

    @Override
    public Vector parseValue(CommandSender sender, Arguments arguments) {
        return VectorProperty.parseVector(arguments);
    }

    @Override
    public List<String> suggestValue(CommandSender sender, Arguments arguments) {
        return VectorProperty.suggestVector(sender);
    }
}
