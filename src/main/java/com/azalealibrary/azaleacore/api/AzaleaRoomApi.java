package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.room.Room;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
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
            data.set("playground", room.getPlayground().getName());
            data.set("lobby", room.getLobby().getName());
            data.set("toWorldSigns", room.getSignTicker().getToWorldSigns());
            data.set("toLobbySigns", room.getSignTicker().getToLobbySigns());
            room.getMinigame().serialize(data.createSection("configs"));
            configuration.set(key, data);
        });
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        configuration.getKeys(false).forEach(key -> {
            ConfigurationSection data = (ConfigurationSection) configuration.get(key);
            String name = (String) data.get("name");
            World lobby = Bukkit.getWorld((String) data.get("lobby"));
            AzaleaMinigameApi.MinigameProvider provider = AzaleaMinigameApi.getInstance().get((String) data.get("minigame"));
            Playground playground = AzaleaPlaygroundApi.getInstance().get((String) data.get("playground"));
            Room room = new Room(name, playground, lobby, provider.create(playground.getWorld()));
            List<Location> toWorldSigns = (List<Location>) data.get("toWorldSigns");
            toWorldSigns.forEach(sign -> room.getSignTicker().getToWorldSigns().add(sign));
            List<Location> toLobbySigns = (List<Location>) data.get("toLobbySigns");
            toLobbySigns.forEach(sign -> room.getSignTicker().getToLobbySigns().add(sign));
            ConfigurationSection configs = (ConfigurationSection) data.get("configs");
            room.getMinigame().deserialize(configs);
            add(name, room);
        });
    }
}
