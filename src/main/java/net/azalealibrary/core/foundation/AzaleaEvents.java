package net.azalealibrary.core.foundation;

import net.azalealibrary.core.foundation.message.ChatMessage;
import net.azalealibrary.core.manager.PlaygroundManager;
import net.azalealibrary.core.manager.TeleporterManager;
import net.azalealibrary.core.party.Party;
import net.azalealibrary.core.party.PartyConfiguration;
import net.azalealibrary.core.playground.Playground;
import net.azalealibrary.core.util.ScheduleUtil;
import net.azalealibrary.core.util.TextUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldInitEvent;

import java.time.LocalTime;

public class AzaleaEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldInitEvent(WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
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
                String message = String.format("%s will be removed from the round in %s.", name, time);
                party.broadcast(ChatMessage.announcement(message));

                ScheduleUtil.doDelayed(timeout * 20, () -> {
                    if (!player.isOnline() && playground.hasOngoingRound()) {
                        party.removePlayer(player);
                        playground.removePlayer(player);
                    }
                });
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Location location = event.getClickedBlock().getLocation();
            Player player = event.getPlayer();

            if (TeleporterManager.getInstance().isTeleporter(location)) {
                TeleporterManager.getInstance().getTeleporter(location).teleport(player);
                event.setCancelled(true);
            }
        }
    }
}
