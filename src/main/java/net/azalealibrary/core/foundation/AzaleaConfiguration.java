package net.azalealibrary.core.foundation;

import net.azalealibrary.core.foundation.configuration.Configurable;
import net.azalealibrary.core.foundation.configuration.property.ConfigurableProperty;
import net.azalealibrary.core.foundation.configuration.property.Property;
import net.azalealibrary.core.foundation.configuration.property.PropertyType;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.util.List;

public class AzaleaConfiguration implements Configurable {

    private static final AzaleaConfiguration CONFIGURATION = new AzaleaConfiguration();

    public static AzaleaConfiguration getInstance() {
        return CONFIGURATION;
    }

    private final Property<Integer> maxPlaygroundCount = new Property<>(PropertyType.INTEGER, "maxPlaygroundCount", "Maximum amount of playgrounds.", 3, true);
    private final Property<World> serverLobby = new Property<>(PropertyType.WORLD, "serverLobby", "The lobby of the server.", Bukkit.getWorld("world"), true);
    private final Property<Boolean> removeStrayPlayground = new Property<>(PropertyType.BOOLEAN, "removeStrayRooms", "Whether to remove unused world files.", false, true);

    public int getMaxPlaygroundCount() {
        return maxPlaygroundCount.get();
    }

    public World getServerLobby() {
        return serverLobby.get();
    }

    public boolean shouldRemoveStrayPlaygrounds() {
        return removeStrayPlayground.get();
    }

    @Override
    public List<ConfigurableProperty<?>> getProperties() {
        return List.of(maxPlaygroundCount, serverLobby, removeStrayPlayground);
    }
}
