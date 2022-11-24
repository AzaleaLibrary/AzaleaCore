package com.azalealibrary.azaleacore.foundation.configuration.property;

import com.azalealibrary.azaleacore.command.core.Arguments;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.List;

public class VectorProperty extends Property<Vector> {

    public VectorProperty(String name, Vector defaultValue, boolean required) {
        super(name, defaultValue, required);
    }

    @Override
    public void set(CommandSender sender, Arguments arguments) {
        set(parseVector(arguments));
    }

    @Override
    public List<String> suggest(CommandSender sender, Arguments arguments) {
        return suggestVector(sender);
    }

    public static Vector parseVector(Arguments arguments) {
        double x = arguments.find(0, "x", Double::parseDouble);
        double y = arguments.find(1, "y", Double::parseDouble);
        double z = arguments.find(2, "z", Double::parseDouble);
        return new Vector(x, y, z);
    }

    public static List<String> suggestVector(CommandSender sender) {
        if (sender instanceof Player player) {
            Location location = player.getLocation();
            double x = location.getBlockX() + .5;
            double y = location.getBlockY() + .5;
            double z = location.getBlockZ() + .5;
            return List.of(x + " " + y + " " + z);
        }
        throw new AzaleaException("Command issuer not a player.");
    }
}
