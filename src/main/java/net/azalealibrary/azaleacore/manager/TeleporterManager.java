package net.azalealibrary.azaleacore.manager;

import net.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import net.azalealibrary.azaleacore.foundation.AzaleaException;
import net.azalealibrary.azaleacore.teleport.SignTeleporter;
import net.azalealibrary.azaleacore.teleport.Teleporter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;

public class TeleporterManager extends Manager<Teleporter> {

    private static final TeleporterManager MANAGER = new TeleporterManager();

    public static TeleporterManager getInstance() {
        return MANAGER;
    }

    private TeleporterManager() {
        super("teleporter");
    }

    public boolean isTeleporter(Location location) {
        return getTeleporter(location) != null;
    }

    public Teleporter getTeleporter(Location location) {
        return getAll().stream().filter(t -> t.getPosition().equals(location)).findFirst().orElse(null);
    }

    public Teleporter create(String name, Location position, Location to) {
        SignTeleporter teleporter = new SignTeleporter(name, position, to);
        add(name, teleporter);
        return teleporter;
    }

    @Override
    protected void serializeEntry(ConfigurationSection section, Teleporter teleporter) {
        section.set("name", teleporter.getName());
        section.set("position", teleporter.getPosition());
        section.set("to", teleporter.getTo());
        section.set("type", teleporter.getType().name());
    }

    @Override
    protected SignTeleporter deserializeEntry(ConfigurationSection section) {
        String name = Objects.requireNonNull(section.getString("name"));
        Location position = Objects.requireNonNull(section.getLocation("position"));
        Location to = Objects.requireNonNull(section.getLocation("to"));
        SignTeleporter teleporter = new SignTeleporter(name, position, to);

        if (!position.getWorld().getWorldFolder().getPath().equals(AzaleaConfiguration.getInstance().getServerLobby().getWorldFolder().getPath())) {
            if (PlaygroundManager.getInstance().get(position.getWorld()) == null) {
                throw new AzaleaException("Teleporter " + teleporter.getName() + " to playground does not exist.");
            }
        }
        return teleporter; // TODO - type
    }
}
