package com.azalealibrary.azaleacore;

import com.azalealibrary.azaleacore.command.*;
import com.azalealibrary.azaleacore.foundation.AzaleaConfiguration;
import com.azalealibrary.azaleacore.foundation.AzaleaEvents;
import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.registry.AzaleaRegistry;
import com.azalealibrary.azaleacore.manager.PartyManager;
import com.azalealibrary.azaleacore.manager.PlaygroundManager;
import com.azalealibrary.azaleacore.manager.TeleporterManager;
import com.azalealibrary.azaleacore.util.SerializationUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

        CommandNode.register(this, AzaleaCommand.class);
        CommandNode.register(this, GotoCommand.class);
        CommandNode.register(this, PartyCommand.class);
        CommandNode.register(this, PlaygroundCommand.class);
        CommandNode.register(this, TeleporterCommand.class);
    }

    @Override
    public void onEnable() {
        AzaleaRegistry.EVENT_BUS.register(new ExampleMinigame()); // TODO - remove

        AzaleaRegistry.MINIGAME.bake();
        AzaleaRegistry.ROUND.bake();
        AzaleaRegistry.ITEM.bake();
        AzaleaRegistry.TEAM.bake();
        AzaleaRegistry.WIN_CONDITION.bake();
        AzaleaRegistry.PROPERTY.bake();

        Bukkit.getPluginManager().registerEvents(new AzaleaEvents(), this);

        SerializationUtil.load("configs", this, AzaleaConfiguration.getInstance());
        SerializationUtil.load("party", this, PartyManager.getInstance());
        SerializationUtil.load("playground", this, PlaygroundManager.getInstance());
        SerializationUtil.load("teleporters", this, TeleporterManager.getInstance());
    }

    @Override
    public void onDisable() {
        PlaygroundManager.getInstance().getAll().forEach(playground -> {
            if (playground.hasOngoingRound() && playground.hasParty()) {
                playground.stop(ChatMessage.important(ChatColor.RED + "AzaleaCore reloaded!"));
            }
        });

        SerializationUtil.save("configs", this, AzaleaConfiguration.getInstance());
        SerializationUtil.save("party", this, PartyManager.getInstance());
        SerializationUtil.save("playground", this, PlaygroundManager.getInstance());
        SerializationUtil.save("teleporters", this, TeleporterManager.getInstance());
    }
}
