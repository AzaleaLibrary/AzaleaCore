package com.azalealibrary.azaleacore.foundation.teleport;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.api.AzaleaRoomApi;
import com.azalealibrary.azaleacore.foundation.AzaleaException;
import com.azalealibrary.azaleacore.foundation.broadcast.Broadcaster;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ActionMessage;
import com.azalealibrary.azaleacore.foundation.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.foundation.serialization.Serializable;
import com.azalealibrary.azaleacore.room.Room;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import javax.annotation.Nonnull;
import java.util.*;

// TODO - what about portal/structure teleporters?
public class SignTicker implements Serializable, Listener {

    private static final SignTicker SIGN_TICKER = new SignTicker();

    public static SignTicker getInstance() {
        return SIGN_TICKER;
    }

    private final List<TeleportSign> signs = new ArrayList<>();

    public SignTicker() {
        Bukkit.getPluginManager().registerEvents(this, AzaleaCore.INSTANCE);
        ScheduleUtil.doEvery(20, () -> getRoomSigns().forEach(TeleportSign::update));
    }

    private List<RoomTeleportSign> getRoomSigns() {
        return signs.stream().filter(sign -> sign instanceof RoomTeleportSign).map(sign -> (RoomTeleportSign) sign).toList();
    }

    private List<RoomTeleportSign> getSignsToRoom(Room room) {
        return getRoomSigns().stream().filter(sign -> sign.getRoom() == room).toList();
    }

    public TeleportSign getSignAt(Location signLocation) {
        return signs.stream().filter(sign -> Objects.equals(sign.getSignLocation().toString(), signLocation.toString())).findFirst().orElse(null);
    }

    public boolean isTracked(Location signLocation) {
        return getSignAt(signLocation) != null;
    }

    public void addRoomSign(Location signLocation, Room room) {
        if (signLocation.getWorld() == room.getWorld()) {
            throw new AzaleaException("Creating room sign in same room.");
        }
        signs.add(new RoomTeleportSign(signLocation, room));
    }

    public void addLobbySign(Location signLocation) {
        if (signLocation.getWorld() == AzaleaCore.getLobby()) {
            throw new AzaleaException("Creating lobby sign in lobby.");
        }
        signs.add(new LobbyTeleportSign(signLocation));
    }

    public void removeSign(Location signLocation) {
        if (isTracked(signLocation)) {
            removeSign(getSignAt(signLocation), false);
        }
    }

    public void removeAll(Room room) {
        getSignsToRoom(room).forEach(sign -> removeSign(sign, true));
    }

    private void removeSign(TeleportSign teleportSign, boolean isTerminated) {
        signs.remove(teleportSign);

        if (isTerminated && teleportSign instanceof RoomTeleportSign roomSign) {
            // mark room signs as terminated rooms instead of blank
            roomSign.update(sign -> {
                sign.setGlowingText(false);
                sign.setColor(DyeColor.BLACK);
                sign.setLine(0, "- " + roomSign.getRoom().getName() + " -");
                sign.setLine(1, "");
                sign.setLine(2, ChatColor.RED + "TERMINATED");
                sign.setLine(3, "");
            });
        } else {
            // clear sign if is lobby sign
            teleportSign.update(sign -> { for (int i = 0; i < 4; i++) sign.setLine(i, ""); });
        }
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Location location = event.getClickedBlock().getLocation();
            Player player = event.getPlayer();

            if (isTracked(location)) {
                TeleportSign sign = getSignAt(location);

                if (sign instanceof RoomTeleportSign roomSign) {
                    Room room = roomSign.getRoom();
                    Broadcaster broadcaster = room.getBroadcaster();

                    if (room.getRoundTicker().isRunning()) {
                        broadcaster.toPlayer(player, new ActionMessage(ChatColor.GOLD + "Sorry, a round is running."));
                        // TODO - join as spectator
                    } else {
                        broadcaster.toPlayground(new ChatMessage(ChatColor.YELLOW + player.getDisplayName() + " has joined the room."));
                        roomSign.teleport(player);
                    }
                } else {
                    sign.teleport(player);
                    Room room = AzaleaRoomApi.getInstance().getObjects().stream()
                            .filter(r -> r.getWorld().getPlayers().contains(player))
                            .findFirst().orElse(null);

                    if (room != null) {
                        room.getBroadcaster().toPlayground(new ChatMessage(ChatColor.YELLOW + player.getDisplayName() + " has left the room."));
                    }
                }
            }
        }
    }

    @Override
    public void serialize(@Nonnull ConfigurationSection configuration) {
        List<Map<String, Object>> data = new ArrayList<>();

        for (TeleportSign sign : signs) {
            Map<String, Object> entry = new HashMap<>();
            entry.put("location", sign.getSignLocation());

            if (sign instanceof RoomTeleportSign roomSign) {
                entry.put("room", roomSign.getRoom().getName());
            }
            data.add(entry);
        }
        configuration.set("signs", data);
    }

    @Override
    public void deserialize(@Nonnull ConfigurationSection configuration) {
        List<Map<String, Object>> data = (List<Map<String, Object>>) configuration.getList("signs");

        for (Map<String, Object> entry : Objects.requireNonNull(data)) {
            Location location = (Location) entry.get("location");

            if (entry.containsKey("room")) {
                Room room = AzaleaRoomApi.getInstance().getObjects().stream()
                        .filter(r -> Objects.equals(r.getName(), entry.get("room")))
                        .findFirst()
                        .orElseThrow();
                signs.add(new RoomTeleportSign(location, room));
            } else {
                signs.add(new LobbyTeleportSign(location));
            }
        }
    }
}
