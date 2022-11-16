package com.azalealibrary.azaleacore.foundation.serialization;

import com.azalealibrary.azaleacore.util.FileUtil;
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
            Bukkit.getLogger().warning("Could not load '" + serializable.getConfigName() + "' data: " + exception);
            exception.printStackTrace();
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
            Bukkit.getLogger().warning("Could not save '" + serializable.getConfigName() + "' data: " + exception);
            exception.printStackTrace();
        }
    }

    public static <S extends Serializable> File getOrCreate(JavaPlugin plugin, S serializable) {
        return FileUtil.insureExists(new File(plugin.getDataFolder(), serializable.getConfigName() + ".yml"));
    }
}
