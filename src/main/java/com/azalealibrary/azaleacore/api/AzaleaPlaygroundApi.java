package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.configuration.ConfigurationSection;

import java.io.File;
import java.util.Objects;

public final class AzaleaPlaygroundApi extends AzaleaApi<Playground> {

    private static final AzaleaPlaygroundApi AZALEA_API = new AzaleaPlaygroundApi();

    public static AzaleaPlaygroundApi getInstance() {
        return AZALEA_API;
    }

    @Override
    protected void serializeEntry(ConfigurationSection section, Playground entry) {
        section.set("name", entry.getName());
        section.set("map", entry.getMap().getName());
        section.set("minigame", entry.getMinigame().getName());
        entry.getMinigame().serialize(section.createSection("configs"));
    }

    @Override
    protected Playground deserializeEntry(ConfigurationSection section) {
        String name = section.getString("name");
        File map = FileUtil.map(section.getString("map"));
        Minigame minigame = AzaleaMinigameApi.getInstance().get(section.getString("minigame")).get();
        minigame.deserialize(Objects.requireNonNull(section.getConfigurationSection("configs")));
        return new Playground(name, map, minigame);
    }
}
