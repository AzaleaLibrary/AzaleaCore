package com.azalealibrary.azaleacore;

import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.io.File;

@Plugin(name = "AzaleaCore", version = "1.0")
public final class Main extends JavaPlugin {

    public Main() {  }

    public Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onLoad() {  }

    @Override
    public void onEnable() {  }

    @Override
    public void onDisable() {  }
}
