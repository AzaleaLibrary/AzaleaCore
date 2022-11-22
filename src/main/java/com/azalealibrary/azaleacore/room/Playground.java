package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.api.core.Minigame;

import java.io.File;

public class Playground {

    private final String name;
    private final File map;
    private final Minigame minigame;

    public Playground(String name, File map, Minigame minigame) {
        this.name = name;
        this.map = map;
        this.minigame = minigame;
    }

    public String getName() {
        return name;
    }

    public File getMap() {
        return map;
    }

    public Minigame getMinigame() {
        return minigame;
    }
}
