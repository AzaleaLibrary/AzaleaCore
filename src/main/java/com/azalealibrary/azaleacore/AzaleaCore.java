package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.AzaleaMinigameApi;
import com.azalealibrary.azaleacore.api.AzaleaPlaygroundApi;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.api.AzaleaScoreboardApi;
import com.azalealibrary.azaleacore.command.*;
import com.azalealibrary.azaleacore.command.core.AzaleaCommand;
import com.azalealibrary.azaleacore.example.ExampleMinigame;
import com.azalealibrary.azaleacore.foundation.serialization.Serialization;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
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
public final class AzaleaCore extends JavaPlugin implements Listener {

    public static final String PLUGIN_ID = "AZA";

    public static AzaleaCore INSTANCE;

    public AzaleaCore() { }

    public AzaleaCore(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    public static World getLobby() {
        return Bukkit.getWorld("world"); // TODO - add config
    }

    @Override
    public void onLoad() {
        INSTANCE = this;

        AzaleaCommand.register(this, BroadcastCommand.class);
        AzaleaCommand.register(this, MinigameCommand.class);
        AzaleaCommand.register(this, PlaygroundCommand.class);
        AzaleaCommand.register(this, PropertyCommand.class);
        AzaleaCommand.register(this, RoomCommand.class);
        AzaleaCommand.register(this, SignCommand.class);
        AzaleaCommand.register(this, TeleportCommand.class);
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, AzaleaCore.INSTANCE); // TODO - separate event class

        AzaleaMinigameApi.getInstance().add("ExampleMinigame", ExampleMinigame::new); // TODO - remove

        Serialization.load("scoreboard", this, AzaleaScoreboardApi.getInstance());
        Serialization.load("playgrounds", this, AzaleaPlaygroundApi.getInstance());
        Serialization.load("rooms", this, AzaleaRoomApi.getInstance());
    }

    @Override
    public void onDisable() {
        Serialization.save("scoreboard", this, AzaleaScoreboardApi.getInstance());
        Serialization.save("playgrounds", this, AzaleaPlaygroundApi.getInstance());
        Serialization.save("rooms", this, AzaleaRoomApi.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldInitEvent(WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false); // considerably reduces lag on room creation
    }
}
