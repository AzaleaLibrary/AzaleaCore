package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.api.core.WinCondition;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public final class AzaleaScoreboardApi extends AzaleaApi<Integer> {

    private static final AzaleaScoreboardApi AZALEA_API = new AzaleaScoreboardApi();

    public static AzaleaScoreboardApi getInstance() {
        return AZALEA_API;
    }

    public void award(Player player, WinCondition<?> winCondition) {
        award(player, winCondition.getWinAward());
    }

    public void award(Player player, int amount) {
        update(player.getUniqueId().toString(), get(player.getUniqueId().toString()) + amount);
    }

    @Override
    protected void serializeEntry(ConfigurationSection section, Integer entry) {
        section.set("score", entry);
    }

    @Override
    protected Integer deserializeEntry(ConfigurationSection section) {
        return section.getInt("score");
    }
}
