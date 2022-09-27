package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.serialization.Serialization;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.io.File;

@Plugin(name = "AzaleaCore", version = Plugin.DEFAULT_VERSION)
@ApiVersion(ApiVersion.Target.v1_19)
public final class Main extends JavaPlugin {

    public Main() {  }

    public Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onLoad() {  }

    @Override
    public void onEnable() {
        AzaleaApi.MINIGAMES.forEach(((world, controller) -> Serialization.load(controller.getConfiguration().getPlugin(), controller.getMinigame())));
    }

    @Override
    public void onDisable() {
        AzaleaApi.MINIGAMES.forEach(((world, controller) -> Serialization.save(controller.getConfiguration().getPlugin(), controller.getMinigame())));
    }
}
