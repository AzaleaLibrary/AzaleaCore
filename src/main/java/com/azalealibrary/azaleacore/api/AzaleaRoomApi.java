package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

public final class AzaleaRoomApi extends AzaleaApi<Room> implements Serializable {

    private static final AzaleaRoomApi AZALEA_API = new AzaleaRoomApi();

    public static AzaleaRoomApi getInstance() {
        return AZALEA_API;
    }

    @Override
    public String getConfigName() {
        return "rooms";
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        getEntries().forEach((key, room) -> {
            YamlConfiguration data = new YamlConfiguration();
            data.set("name", room.getName());
            data.set("minigame", room.getMinigame().getName());
            data.set("world", room.getWorld().getName());
            data.set("toWorldSigns", room.getSignTicker().getToWorldSigns());
            data.set("toLobbySigns", room.getSignTicker().getToLobbySigns());
            data.set("template", room.getTemplate().getName());
            room.getMinigame().serialize(data.createSection("configs"));
            configuration.set(key, data);
        });
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        configuration.getKeys(false).forEach(key -> {
            ConfigurationSection data = (ConfigurationSection) configuration.get(key);
            String name = (String) data.get("name");
            World world = Bukkit.getWorld((String) data.get("world"));
            Minigame minigame = AzaleaMinigameApi.getInstance().get((String) data.get("minigame")).get();
            minigame.deserialize((ConfigurationSection) data.get("configs"));
            File template = FileUtil.template((String) data.get("template"));
            Room room = new Room(name, minigame, world, template);
            List<Location> toWorldSigns = (List<Location>) data.get("toWorldSigns");
            toWorldSigns.forEach(sign -> room.getSignTicker().getToWorldSigns().add(sign));
            List<Location> toLobbySigns = (List<Location>) data.get("toLobbySigns");
            toLobbySigns.forEach(sign -> room.getSignTicker().getToLobbySigns().add(sign));
            add(name, room);
        });

        for (File file : FileUtil.rooms()) { // remove any stray room directories
            if (getKeys().stream().noneMatch(key -> key.equals(file.getName()))) {
                FileUtil.delete(file);
            }
        }
    }
}
