package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.api.AzaleaPlaygroundApi;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.api.AzaleaScoreboardApi;
import com.azalealibrary.azaleacore.command.*;
import com.azalealibrary.azaleacore.command.core.AzaleaCommand;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.AzaleaEvents;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.registry.AzaleaRegistry;
import com.azalealibrary.azaleacore.foundation.serialization.Serialization;
import com.azalealibrary.azaleacore.foundation.teleport.SignTicker;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.LogPrefix;
import org.bukkit.plugin.java.annotation.plugin.Plugin;

import java.io.File;

@SuppressWarnings("unused")
@Plugin(name = "AzaleaCore", version = Plugin.DEFAULT_VERSION)
@ApiVersion(ApiVersion.Target.v1_13) // compatible with all post-1.13 mc versions
@LogPrefix(AzaleaCore.PLUGIN_ID)
public final class AzaleaCore extends JavaPlugin implements Listener {

    public static final String PLUGIN_ID = "AZA";
    public static AzaleaCore INSTANCE;

    public AzaleaCore() { }

    public AzaleaCore(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
    }

    @Override
    public void onLoad() {
        INSTANCE = this;

        AzaleaCommand.register(this, BroadcastCommand.class);
        AzaleaCommand.register(this, ConfigureCommand.class);
        AzaleaCommand.register(this, GlobalConfigCommand.class);
        AzaleaCommand.register(this, InvitationCommand.class);
        AzaleaCommand.register(this, ItemCommand.class);
        AzaleaCommand.register(this, MinigameCommand.class);
        AzaleaCommand.register(this, PlaygroundCommand.class);
        AzaleaCommand.register(this, RoomCommand.class);
        AzaleaCommand.register(this, SignCommand.class);
        AzaleaCommand.register(this, TeleportCommand.class);
    }

    @Override
    public void onEnable() {
        AzaleaRegistry.MINIGAME.bake();
        AzaleaRegistry.ROUND.bake();
        AzaleaRegistry.ITEM.bake();
        AzaleaRegistry.TEAM.bake();
        AzaleaRegistry.WIN_CONDITION.bake();
        AzaleaRegistry.PROPERTY.bake();

        Bukkit.getPluginManager().registerEvents(new AzaleaEvents(), this);

        // sometimes, Bukkit#getWorld(String) returns null and this solves the issue?
        ScheduleUtil.doDelayed(0 , () -> { // TODO - review
            Serialization.load("configs", this, AzaleaConfiguration.getInstance());
            Serialization.load("scoreboard", this, AzaleaScoreboardApi.getInstance());
            Serialization.load("playgrounds", this, AzaleaPlaygroundApi.getInstance());
            Serialization.load("rooms", this, AzaleaRoomApi.getInstance());
            Serialization.load("signs", this, SignTicker.getInstance());
        });
    }

    @Override
    public void onDisable() {
        AzaleaRoomApi.getInstance().getObjects().forEach(room -> {
            if (room.getRoundTicker().isRunning()) {
                room.stop(ChatMessage.important("AzaleaCore restarted and caused the game to end."));
            }
        });

        Serialization.save("configs", this, AzaleaConfiguration.getInstance());
        Serialization.save("scoreboard", this, AzaleaScoreboardApi.getInstance());
        Serialization.save("playgrounds", this, AzaleaPlaygroundApi.getInstance());
        Serialization.save("rooms", this, AzaleaRoomApi.getInstance());
        Serialization.save("signs", this, SignTicker.getInstance());
    }
}
