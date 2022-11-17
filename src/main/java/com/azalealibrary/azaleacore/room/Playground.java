package com.azalealibrary.azaleacore.room;

import org.bukkit.Location;

import java.io.File;
import java.util.List;

public class Playground {

    private final String name;
    private final File template;
    private final Location spawn;
    private final List<String> tags;
    // TODO - world borders

    public Playground(String name, File template, Location spawn, List<String> tags) {
        this.name = name;
        this.template = template;
        this.spawn = spawn;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public File getTemplate() {
        return template;
    }

    public Location getSpawn() {
        return spawn;
    }

    public List<String> getTags() {
        return tags;
    }
}
