package com.azalealibrary.azaleacore.room.playground;

import org.bukkit.Location;
import org.bukkit.World;

public class Playground {

    private final String name;
    private final World world;
    private final Location spawn;
    // TODO - world borders

    public Playground(String name, World world) {
        this(name, world, world.getSpawnLocation());
    }

    public Playground(String name, World world, Location spawn) {
        this.name = name;
        this.world = world;
        this.spawn = spawn;
    }

    public String getName() {
        return name;
    }

    public World getWorld() {
        return world;
    }

    public Location getSpawn() {
        return spawn;
    }
}
