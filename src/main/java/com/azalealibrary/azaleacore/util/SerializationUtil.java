package com.azalealibrary.azaleacore.util;

import com.azalealibrary.azaleacore.foundation.Serializable;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class SerializationUtil {

    public static void load(String name, final JavaPlugin plugin, final Serializable serializable) {
        load(name, serializable, FileUtil.insureExists(new File(plugin.getDataFolder(), name + ".yml")));
    }

    public static void load(String name, final Serializable serializable, final File file) {
        System.out.println("Loading '" + name + "' data.");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        try {
            serializable.deserialize(config);
        } catch (Exception exception) {
            System.out.println("Could not load '" + name + "' data: " + exception);
            exception.printStackTrace();
        }
    }

    public static void save(String name, final JavaPlugin plugin, final Serializable serializable) {
        save(name, serializable, FileUtil.insureExists(new File(plugin.getDataFolder(), name + ".yml")));
    }

    public static void save(String name, final Serializable serializable, final File file) {
        System.out.println("Saving '" + name + "' data.");
        YamlConfiguration config = new YamlConfiguration();
        serializable.serialize(config);

        try {
            config.save(file);
        } catch (Exception exception) {
            System.out.println("Could not save '" + name + "' data: " + exception);
            exception.printStackTrace();
        }
    }
}
