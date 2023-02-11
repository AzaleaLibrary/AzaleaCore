package net.azalealibrary.azaleacore.teleport;

import org.bukkit.Location;

public class SignTeleporter implements Teleporter {

    private final String name;
    private final Location position;
    private final Location to;

    public SignTeleporter(String name, Location position, Location to) {
        this.name = name;
        this.position = position;
        this.to = to;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getType() {
        return Type.SIGN;
    }

    @Override
    public Location getPosition() {
        return position;
    }

    @Override
    public org.bukkit.Location getTo() {
        return to;
    }
}
