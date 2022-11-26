package com.azalealibrary.azaleacore.foundation;

import com.azalealibrary.azaleacore.foundation.configuration.Configurable;
import com.azalealibrary.azaleacore.foundation.configuration.property.ConfigurableProperty;
import com.azalealibrary.azaleacore.foundation.configuration.property.Property;
import com.azalealibrary.azaleacore.foundation.configuration.property.PropertyType;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

public class AzaleaConfiguration implements Configurable {

    private static final AzaleaConfiguration CONFIGURATION = new AzaleaConfiguration();

    public static AzaleaConfiguration getInstance() {
        return CONFIGURATION;
    }

    private final Property<Integer> maxRoomCount = new Property<>(PropertyType.INTEGER, "maxRoomCount", 3, true);
    private final Property<World> serverLobby = new Property<>(PropertyType.WORLD, "serverLobby", Bukkit.getWorld("world"), true);
    private final Property<Boolean> removeStrayRooms = new Property<>(PropertyType.BOOLEAN, "removeStrayRooms", false, true);

    public int getMaxRoomCount() {
        return maxRoomCount.get();
    }

    public World getServerLobby() {
        return serverLobby.get();
    }

    public boolean shouldRemoveStrayRooms() {
        return removeStrayRooms.get();
    }

    @Override
    public List<ConfigurableProperty<?>> getProperties() {
        return List.of(maxRoomCount, serverLobby, removeStrayRooms);
    }
}
