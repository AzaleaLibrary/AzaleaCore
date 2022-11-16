package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.WinCondition;
import com.azalealibrary.azaleacore.serialization.Serializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

public final class AzaleaScoreboardApi implements Serializable {

    private static final AzaleaScoreboardApi AZALEA_API = new AzaleaScoreboardApi();

    public static AzaleaScoreboardApi getInstance() {
        return AZALEA_API;
    }

    private final Map<UUID, Integer> scores = new HashMap<>();

    public void award(Player player, WinCondition<?> winCondition) {
        award(player, winCondition.getWinAward());
    }

    public void award(Player player, int amount) {
        UUID uuid = player.getUniqueId();
        scores.put(uuid, scores.getOrDefault(uuid, 0) + amount);
    }

    @Override
    public String getConfigName() {
        return "scoreboard";
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        List<Map<String, Integer>> data = new ArrayList<>();
        scores.forEach((uuid, score) -> {
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
            scores.put(UUID.fromString(uuid), score);
        });
    }
}
