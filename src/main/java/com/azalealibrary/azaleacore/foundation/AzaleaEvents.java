package com.azalealibrary.azaleacore.foundation;

import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.foundation.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.room.RoomConfiguration;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.world.WorldInitEvent;

public class AzaleaEvents implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWorldInitEvent(WorldInitEvent event) {
        event.getWorld().setKeepSpawnInMemory(false); // considerably reduces lag on room creation
    }

    @EventHandler
    public void onPlayerQuitEvent(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Room room = AzaleaRoomApi.getInstance().getRoom(player);

        if (room != null) {
            event.setQuitMessage(null);

            RoomConfiguration configs = room.getConfiguration();
            Broadcaster broadcaster = room.getBroadcaster();

            String name = ChatColor.YELLOW + player.getDisplayName() + ChatColor.RESET;
            int timeout = configs.getPlayerTimeout();
            String m1 = String.format("%s has left and will be removed from the game in %ss.", name, timeout);
            ChatMessage a1 = ChatMessage.announcement(m1);
            broadcaster.broadcast(a1, Broadcaster.Chanel.ROOM);

            ScheduleUtil.doDelayed(timeout + 20 / configs.getRoundTickRate(), () -> {
                if (!player.isOnline()) {
                    room.removePlayer(player);

                    if (room.getRoundTicker().isRunning()) {
                        String m2 = String.format("%s hasn't come back and has been removed from the game.", name);
                        ChatMessage a2 = ChatMessage.announcement(m2);
                        broadcaster.broadcast(a2, Broadcaster.Chanel.ROOM);
                    }
                }
            });
        }
    }
}
