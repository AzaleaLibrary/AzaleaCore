package com.azalealibrary.azaleacore.foundation.teleport.sign;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.function.Consumer;

public abstract class TeleportSign {

    private final Location signLocation;
    private final Location teleportLocation;

    public TeleportSign(Location signLocation, Location teleportLocation) {
        this.signLocation = signLocation;
        this.teleportLocation = teleportLocation;
    }

    public Location getTeleportLocation() {
        return teleportLocation;
    }

    public Location getSignLocation() {
        return signLocation;
    }

    public void teleport(Player player) {
        player.teleport(teleportLocation);
    }

    public void update() {
        update(this::paint);
    }

    public void update(Consumer<Sign> decorator) {
        World world = Objects.requireNonNull(signLocation.getWorld());
        BlockState state = world.getBlockState(signLocation).getBlock().getState();

        if (state instanceof Sign sign) {
            decorator.accept(sign);
            sign.setEditable(false);
            sign.update();
        }
    }

    protected abstract void paint(Sign sign);
}
