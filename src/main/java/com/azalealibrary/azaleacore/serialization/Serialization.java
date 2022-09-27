package com.azalealibrary.azaleacore.serialization;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class Serialization {

    public static <S extends Serializable> void load(final JavaPlugin plugin, final S serializable) {
        load(serializable, getOrCreate(plugin, serializable));
    }

    public static <S extends Serializable> void load(final S serializable, final File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        try {
            serializable.deserialize(config);
        } catch (Exception exception) {
            Bukkit.getLogger().warning("Could not load '" + serializable.getName() + "' data: " + exception);
        }
    }

    public static <S extends Serializable> void save(final JavaPlugin plugin, final S serializable) {
        save(serializable, getOrCreate(plugin, serializable));
    }

    public static <S extends Serializable> void save(final S serializable, final File file) {
        YamlConfiguration config = new YamlConfiguration();
        serializable.serialize(config);

        try {
            config.save(file);
        } catch (Exception exception) {
            Bukkit.getLogger().warning("Could not save '" + serializable.getName() + "' data: " + exception);
        }
    }

    public static <S extends Serializable> File getOrCreate(JavaPlugin plugin, S serializable) {
        return getOrCreate(plugin, serializable.getName() + ".yml");
    }

    public static File getOrCreate(JavaPlugin plugin, String name) {
        File parent = plugin.getDataFolder();
        File file = new File(parent, name);

        if (!file.exists()) {
            try {
                if (parent.mkdirs() & file.createNewFile()) {
                    plugin.getLogger().warning("Created '" + file.getName() + "' data file.");
                }
            } catch (Exception exception) {
                plugin.getLogger().warning("Could not create '" + file.getName() + "' file: " + exception);
            }
        }
        return file;
    }
}
