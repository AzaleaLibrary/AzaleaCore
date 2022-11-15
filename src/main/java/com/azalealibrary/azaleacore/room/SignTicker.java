package com.azalealibrary.azaleacore.room;

import com.azalealibrary.azaleacore.Main;
import com.azalealibrary.azaleacore.util.ScheduleUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class SignTicker implements Listener {

    private final MinigameRoom room;
    private final List<Location> toWorldSigns = new ArrayList<>();
    private final List<Location> toLobbySigns = new ArrayList<>();

    public SignTicker(MinigameRoom room) {
        this.room = room;
        Bukkit.getPluginManager().registerEvents(this, Main.INSTANCE);
        ScheduleUtil.doFor(20, this::onTick);
    }

    public List<Location> getToWorldSigns() {
        return toWorldSigns;
    }

    public List<Location> getToLobbySigns() {
        return toLobbySigns;
    }

    private void onTick() {
        updateSigns(room.getLobby(), toWorldSigns, this::updateToWorldSign);
        updateSigns(room.getWorld(), toLobbySigns, this::updateToLobbySign);
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
        ChatColor color = room.getRoundTicker().isRunning() ? ChatColor.RED : ChatColor.GREEN;
        String running = room.getRoundTicker().isRunning() ? "Round ongoing" : "Round idle";
        sign.setLine(3, "> " + color + running);
    }

    private void updateToLobbySign(Sign sign) {
        sign.setLine(0, "===");
        sign.setLine(1, ChatColor.ITALIC + "Return to lobby");
        sign.setLine(2, "===");
    }

    @EventHandler
    public void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null) {
            Location location = event.getClickedBlock().getLocation();
            Player player = event.getPlayer();

            if (toLobbySigns.contains(location)) {
                player.teleport(room.getLobby().getSpawnLocation());
            } else if (toWorldSigns.contains(location)) {
                player.teleport(room.getWorld().getSpawnLocation());
            }
        }
    }
}
