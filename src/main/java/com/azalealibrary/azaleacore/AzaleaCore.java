package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.AzaleaMinigameApi;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.api.AzaleaScoreboardApi;
import com.azalealibrary.azaleacore.command.*;
import com.azalealibrary.azaleacore.example.ExampleMinigame;
import com.azalealibrary.azaleacore.foundation.serialization.Serialization;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.io.File;

@Plugin(name = "AzaleaCore", version = Plugin.DEFAULT_VERSION)
@ApiVersion(ApiVersion.Target.v1_13) // compatible with all post-1.13 mc versions
@LogPrefix(AzaleaCore.PLUGIN_ID)
@SuppressWarnings("unused")
public final class AzaleaCore extends JavaPlugin {

    public static final String PLUGIN_ID = "AZA";

    public static AzaleaCore INSTANCE;

    public AzaleaCore() { }

    public AzaleaCore(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onLoad() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        new PropertyCommand(this);
        new MinigameCommand(this);
        new RoomCommand(this);
        new SignCommand(this);
        new BroadcastCommand(this);

        AzaleaMinigameApi.getInstance().registerMinigame("ExampleMinigame", ExampleMinigame::new); // TODO - remove

        Serialization.load(this, AzaleaRoomApi.getInstance());
//        Serialization.load(this, AzaleaMinigameApi.getInstance());
        Serialization.load(this, AzaleaScoreboardApi.getInstance());
    }

    @Override
    public void onDisable() {
        Serialization.save(this, AzaleaRoomApi.getInstance());
//        Serialization.save(this, AzaleaMinigameApi.getInstance());
        Serialization.save(this, AzaleaScoreboardApi.getInstance());
    }
}
