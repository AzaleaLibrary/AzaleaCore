package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.room.broadcast.message.ActionMessage;
import com.azalealibrary.azaleacore.room.broadcast.message.ChatMessage;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.*;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SignTicker implements Listener {

    private final Room room;
    private final List<Location> toWorldSigns = new ArrayList<>();
    private final List<Location> toLobbySigns = new ArrayList<>();
    private final int eventId;

    public SignTicker(Room room) {
        this.room = room;
        Bukkit.getPluginManager().registerEvents(this, AzaleaCore.INSTANCE);
        eventId = ScheduleUtil.doEvery(20, this::updatedAll);
    }

    public List<Location> getToWorldSigns() {
        return toWorldSigns;
    }

    public List<Location> getToLobbySigns() {
        return toLobbySigns;
    }

    private void updatedAll() {
        updateSigns(room.getLobby(), toWorldSigns, this::updateToWorldSign);
        updateSigns(room.getWorld(), toLobbySigns, this::updateToLobbySign);
    }

    public void discardAll() {
        HandlerList.unregisterAll(this);
        Bukkit.getScheduler().cancelTask(eventId);
        updateSigns(room.getLobby(), toWorldSigns, this::removeSign);
    }

    private void updateSigns(World world, List<Location> signs, Consumer<Sign> decorator) {
        for (int i = 0; i < signs.size(); i++) {
            Location location = signs.get(i);
            BlockState state = world.getBlockState(location).getBlock().getState();

            if (state instanceof Sign sign) {
                decorator.accept(sign);
                sign.setEditable(false);
                sign.update();
            } else {
                signs.remove(location); // remove definitively if block is not a sign
            }
        }
    }

    private void updateToWorldSign(Sign sign) {
        sign.setLine(0, "- " + room.getName() + " -");
        sign.setLine(1, ChatColor.ITALIC + room.getMinigame().getName());
        sign.setLine(2, room.getWorld().getPlayers().size() + " / 100");
        String running = room.getRoundTicker().isRunning()
                ? ChatColor.RED + "Round ongoing"
                : ChatColor.GREEN + "Round idle";
        sign.setLine(3, "> " + running);
    }

    private void updateToLobbySign(Sign sign) {
        sign.setGlowingText(true);
        sign.setColor(DyeColor.GREEN);

        sign.setLine(0, ChatColor.YELLOW + "EXIT" + ChatColor.GREEN + " _o       ");
        sign.setLine(1, ChatColor.GREEN + "/ /\\_ ");
        sign.setLine(2, ChatColor.GREEN + "     _/\\  " + ChatColor.YELLOW + "LOBBY ");
        sign.setLine(3, ChatColor.GREEN + "/  ");
    }

    private void removeSign(Sign sign) {
        sign.setLine(0, "- " + room.getName() + " -");
        sign.setLine(1, "");
        sign.setLine(2, ChatColor.RED + "TERMINATED");
        sign.setLine(3, "");
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Location location = event.getClickedBlock().getLocation();
            Player player = event.getPlayer();

            if ((toLobbySigns.contains(location) || toWorldSigns.contains(location))) {

                if (room.getRoundTicker().isRunning()) {
                    String message = ChatColor.GOLD + "Sorry, a round is running.";
                    room.getBroadcaster().toPlayer(player, new ActionMessage(message));
                } else if (toLobbySigns.contains(location)) {
                    player.teleport(room.getLobby().getSpawnLocation());
                    notifyPlayers(player.getDisplayName() + " has left the room.");
                } else if (toWorldSigns.contains(location)) {
                    notifyPlayers(player.getDisplayName() + " has joined the room.");
                    player.teleport(room.getWorld().getSpawnLocation());
                }
            }
        }
    }

    private void notifyPlayers(String message) {
        room.getBroadcaster().toPlayground(new ChatMessage(ChatColor.YELLOW + message));
    }
}
