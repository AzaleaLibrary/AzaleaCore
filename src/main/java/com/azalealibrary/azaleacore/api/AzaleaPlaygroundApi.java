package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.azalealibrary.azaleacore.room.Playground;
import com.azalealibrary.azaleacore.util.FileUtil;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.annotation.Nonnull;
import java.io.File;
import java.util.List;

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
            data.set("tags", playground.getTags());
            playground.getConfiguration().serialize(data.createSection("configs"));
            configuration.set(key, data);
        });
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        configuration.getKeys(false).forEach(key -> {
            ConfigurationSection data = (ConfigurationSection) configuration.get(key);
            String name = (String) data.get("name");
            File template = FileUtil.template((String) data.get("template"));
            List<String> tags = (List<String>) data.getList("tags");
            Playground playground = new Playground(name, template, tags);
            playground.getConfiguration().deserialize((ConfigurationSection) data.get("configs"));
            add(key, playground);
        });
    }
}
