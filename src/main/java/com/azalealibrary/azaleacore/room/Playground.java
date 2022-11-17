package com.azalealibrary.azaleacore.room;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Arrays;
import java.util.List;

public class Playground {

    private final String name;
    private final World world;
    private final Location spawn;
    private final List<String> tags;
    // TODO - world borders

    public Playground(String name, World world, Location spawn, String... tags) {
        this(name, world, spawn, Arrays.asList(tags));
    }

    public Playground(String name, World world, Location spawn, List<String> tags) {
        this.name = name;
        this.world = world;
        this.spawn = spawn;
        this.tags = tags;
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

    public List<String> getTags() {
        return tags;
    }
}
