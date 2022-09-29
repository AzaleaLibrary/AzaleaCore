package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.command.MinigameCommand;
import com.azalealibrary.azaleacore.command.PropertyCommand;
import com.azalealibrary.azaleacore.example.ExampleMinigame;
import com.azalealibrary.azaleacore.serialization.Serialization;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.io.File;

@Plugin(name = "AzaleaCore", version = Plugin.DEFAULT_VERSION)
@ApiVersion() // compatible with all post-1.13 mc versions
@SuppressWarnings("unused")
public final class Main extends JavaPlugin {

    public static Main INSTANCE; // TODO - remove

    public Main() {  }

    public Main(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onEnable() {
        INSTANCE = this; // TODO - remove

        new PropertyCommand(this);
        new MinigameCommand(this);

        AzaleaApi.registerMinigame("ExampleMinigame", ExampleMinigame::new);

        AzaleaApi.RUNNING_MINIGAMES.forEach(((world, controller) -> Serialization.load(controller.getMinigame().getConfiguration().getPlugin(), controller.getMinigame())));
    }

    @Override
    public void onDisable() {
        AzaleaApi.RUNNING_MINIGAMES.forEach(((world, controller) -> Serialization.save(controller.getMinigame().getConfiguration().getPlugin(), controller.getMinigame())));
    }
}
