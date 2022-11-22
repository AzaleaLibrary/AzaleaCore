package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.Minigame;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;

public final class AzaleaPlaygroundApi extends AzaleaApi<Playground> implements Serializable {

    private static final AzaleaPlaygroundApi AZALEA_API = new AzaleaPlaygroundApi();

    public static AzaleaPlaygroundApi getInstance() {
        return AZALEA_API;
    }

    @Override
    public String getConfigName() {
        return "playgrounds";
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        getEntries().forEach((key, playground) -> {
            YamlConfiguration data = new YamlConfiguration();
            data.set("name", playground.getName());
            data.set("template", playground.getTemplate().getName());
            data.set("minigame", playground.getMinigame().getName());
            playground.getMinigame().serialize(data.createSection("configs"));
            configuration.set(key, data);
        });
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        configuration.getKeys(false).forEach(key -> {
            ConfigurationSection data = (ConfigurationSection) configuration.get(key);
            String name = (String) data.get("name");
            File template = FileUtil.template((String) data.get("template"));
            Minigame minigame = AzaleaMinigameApi.getInstance().get((String) data.get("minigame")).get();
            minigame.deserialize((ConfigurationSection) data.get("configs"));
            Playground playground = new Playground(name, template, minigame);
            add(key, playground);
        });

//        for (File file : FileUtil.playgrounds()) { // remove any stray playground directories
//            if (getKeys().stream().noneMatch(key -> key.equals(file.getName()))) {
//                FileUtil.delete(file);
//            }
//        }
    }
}
