package com.azalealibrary.azaleacore.manager;

import com.azalealibrary.azaleacore.teleport.SignTeleporter;
import com.azalealibrary.azaleacore.teleport.Teleporter;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

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
        String name = section.getString("name");
        Location position = section.getLocation("position");
        Location to = section.getLocation("to");
        return new SignTeleporter(name, position, to); // TODO - type
    }
}
