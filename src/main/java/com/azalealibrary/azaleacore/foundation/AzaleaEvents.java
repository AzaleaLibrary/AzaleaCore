package com.azalealibrary.azaleacore.foundation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldInitEvent;

public class AzaleaEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldInitEvent(WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false); // considerably reduces lag on getPlayground creation
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
//        Player player = event.getPlayer();
//        Party getPlayground = RoomManager.getInstance().get(player);
//
//        if (getPlayground != null) {
//            event.setQuitMessage(null);
//
//            PartyConfiguration configs = getPlayground.getConfiguration();
//
//            String name = ChatColor.YELLOW + player.getDisplayName() + ChatColor.RESET;
//            int timeout = configs.getPlayerTimeout();
//            String m1 = String.format("%s has left and will be removed from the game in %ss.", name, timeout);
//            ChatMessage a1 = ChatMessage.announcement(m1);
//            getPlayground.broadcast(a1);
//
//            ScheduleUtil.doDelayed(timeout + 20 / configs.getRoundTickRate(), () -> {
//                if (!player.isOnline()) {
//                    getPlayground.removePlayer(player);
//
//                    if (getPlayground.getRoundTicker().isRunning()) {
//                        String m2 = String.format("%s hasn't come back and has been removed from the game.", name);
//                        ChatMessage a2 = ChatMessage.announcement(m2);
//                        getPlayground.broadcast(a2);
//                    }
//                }
//            });
//        }
    }
}
