package com.azalealibrary.azaleacore.scoreboard;

import com.azalealibrary.azaleacore.api.WinCondition;
import com.azalealibrary.azaleacore.serialization.Serializable;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.*;

public final class AzaleaScoreboard implements Serializable {

    private static final AzaleaScoreboard AZALEA_API = new AzaleaScoreboard();

    public static AzaleaScoreboard getInstance() {
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
        return "AzaleaApiScoreboard";
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
