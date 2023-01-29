package com.azalealibrary.azaleacore.util;

import com.azalealibrary.azaleacore.foundation.Serializable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SerializationUtil {

    public static void load(String name, final JavaPlugin plugin, final Serializable serializable) {
        load(serializable, FileUtil.insureExists(new File(plugin.getDataFolder(), name + ".yml")));
    }

    public static void load(final Serializable serializable, final File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        try {
            serializable.deserialize(config);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static void save(String name, final JavaPlugin plugin, final Serializable serializable) {
        save(serializable, FileUtil.insureExists(new File(plugin.getDataFolder(), name + ".yml")));
    }

    public static void save(final Serializable serializable, final File file) {
        YamlConfiguration config = new YamlConfiguration();
        serializable.serialize(config);

        try {
            config.save(file);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
