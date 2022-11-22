package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class AzaleaScoreboardApi extends AzaleaApi<Integer> implements Serializable {

    private static final AzaleaScoreboardApi AZALEA_API = new AzaleaScoreboardApi();

    public static AzaleaScoreboardApi getInstance() {
        return AZALEA_API;
    }

    public void award(Player player, WinCondition<?> winCondition) {
        award(player, winCondition.getWinAward());
    }

    public void award(Player player, int amount) {
        update(player.getUniqueId().toString(), amount);
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        List<Map<String, Integer>> data = new ArrayList<>();
        getEntries().forEach((uuid, score) -> {
            Map<String, Integer> entry = new HashMap<>();
            entry.put(String.valueOf(uuid), score);
            data.add(entry);
        });
        configuration.set("scores", data);
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        List<Map<?, ?>> data = configuration.getMapList("scores");
        data.forEach(entry -> {
            String uuid = (String) new ArrayList<>(entry.keySet()).get(0);
            int score = (int) new ArrayList<>(entry.values()).get(0);
            add(uuid, score);
        });
    }
}
