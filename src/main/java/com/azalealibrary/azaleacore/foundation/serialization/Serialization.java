package com.azalealibrary.azaleacore.foundation.serialization;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Serialization {

    public static <S extends Serializable> void load(String name, final JavaPlugin plugin, final S serializable) {
        load(name, serializable, FileUtil.insureExists(new File(plugin.getDataFolder(), name + ".yml")));
    }

    public static <S extends Serializable> void load(String name, final S serializable, final File file) {
        AzaleaCore.BROADCASTER.debug("Loading '" + name + "' data.");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        try {
            serializable.deserialize(config);
        } catch (Exception exception) {
            AzaleaCore.BROADCASTER.error("Could not load '" + name + "' data: " + exception);
            exception.printStackTrace();
        }
    }

    public static <S extends Serializable> void save(String name, final JavaPlugin plugin, final S serializable) {
        save(name, serializable, FileUtil.insureExists(new File(plugin.getDataFolder(), name + ".yml")));
    }

    public static <S extends Serializable> void save(String name, final S serializable, final File file) {
        AzaleaCore.BROADCASTER.debug("Saving '" + name + "' data.");
        YamlConfiguration config = new YamlConfiguration();
        serializable.serialize(config);

        try {
            config.save(file);
        } catch (Exception exception) {
            AzaleaCore.BROADCASTER.error("Could not save '" + name + "' data: " + exception);
            exception.printStackTrace();
        }
    }
}
