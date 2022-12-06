package com.azalealibrary.azaleacore.foundation;

import com.azalealibrary.azaleacore.foundation.message.ChatMessage;
import com.azalealibrary.azaleacore.manager.PlaygroundManager;
import com.azalealibrary.azaleacore.party.Party;
import com.azalealibrary.azaleacore.party.PartyConfiguration;
import com.azalealibrary.azaleacore.playground.Playground;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import com.azalealibrary.azaleacore.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldInitEvent;

import java.time.LocalTime;

public class AzaleaEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldInitEvent(WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false); // considerably reduces lag on getPlayground creation
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Playground playground = PlaygroundManager.getInstance().get(player);

        if (playground != null) {
            event.setQuitMessage(null);
            playground.removePlayer(player);
            Party party = playground.getParty();

            if (playground.hasOngoingRound() && party != null && party.isMember(player)) {
                PartyConfiguration configs = party.getConfiguration();
                int timeout = configs.getPlayerTimeout();

                String name = TextUtil.getName(player);
                String time = LocalTime.MIN.plusSeconds(timeout).toString();
                String firstMessage = String.format("%s will be removed from the round in %s.", name, time);
                party.broadcast(ChatMessage.announcement(firstMessage));

                ScheduleUtil.doDelayed(timeout * 20, () -> {
                    if (!player.isOnline() && playground.hasOngoingRound()) {
                        playground.removeFromRound(player);
                    }
                });
            }
        }
    }
}
