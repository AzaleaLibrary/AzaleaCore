package com.azalealibrary.azaleacore.foundation.serialization;

import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Serialization {

    public static <S extends Serializable> void load(String name, final JavaPlugin plugin, final S serializable) {
        load(name, serializable, FileUtil.insureExists(new File(plugin.getDataFolder(), name + ".yml")));
    }

    public static <S extends Serializable> void load(String name, final S serializable, final File file) {
        Bukkit.getLogger().warning("Loading '" + name + "' data.");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        try {
            serializable.deserialize(config);
        } catch (Exception exception) {
            Bukkit.getLogger().warning("Could not load '" + name + "' data: " + exception);
            exception.printStackTrace();
        }
    }

    public static <S extends Serializable> void save(String name, final JavaPlugin plugin, final S serializable) {
        save(name, serializable, FileUtil.insureExists(new File(plugin.getDataFolder(), name + ".yml")));
    }

    public static <S extends Serializable> void save(String name, final S serializable, final File file) {
        Bukkit.getLogger().warning("Saving '" + name + "' data.");
        YamlConfiguration config = new YamlConfiguration();
        serializable.serialize(config);

        try {
            config.save(file);
        } catch (Exception exception) {
            Bukkit.getLogger().warning("Could not save '" + name + "' data: " + exception);
            exception.printStackTrace();
        }
    }
}
