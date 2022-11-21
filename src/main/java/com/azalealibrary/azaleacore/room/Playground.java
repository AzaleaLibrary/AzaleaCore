package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.api.core.Minigame;

import java.io.File;

public class Playground {

    private final String name;
    private final File template;
    private final Minigame minigame;

    public Playground(String name, File template, Minigame minigame) {
        this.name = name;
        this.template = template;
        this.minigame = minigame;
    }

    public String getName() {
        return name;
    }

    public File getTemplate() {
        return template;
    }

    public Minigame getMinigame() {
        return minigame;
    }
}
