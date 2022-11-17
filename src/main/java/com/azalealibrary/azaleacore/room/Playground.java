package com.azalealibrary.azaleacore.room;

import java.io.File;
import java.util.List;

public class Playground {

    private final String name;
    private final File template;
    private final List<String> tags;
    // TODO - world borders

    public Playground(String name, File template, List<String> tags) {
        this.name = name;
        this.template = template;
        this.tags = tags;
    }

    public String getName() {
        return name;
    }

    public File getTemplate() {
        return template;
    }

    public List<String> getTags() {
        return tags;
    }
}
