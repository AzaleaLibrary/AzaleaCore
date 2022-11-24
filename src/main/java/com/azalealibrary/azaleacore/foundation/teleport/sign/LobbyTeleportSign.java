package com.azalealibrary.azaleacore.foundation.teleport.sign;

import com.azalealibrary.azaleacore.AzaleaCore;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.block.Sign;

public class LobbyTeleportSign extends TeleportSign {

    public LobbyTeleportSign(Location signLocation) {
        super(signLocation, AzaleaCore.getLobby().getSpawnLocation()); // TODO - review
        update(); // update lobby sign only on creation
    }

    @Override
    protected void paint(Sign sign) {
        sign.setGlowingText(true);
        sign.setColor(DyeColor.GREEN);
        sign.setLine(0, ChatColor.YELLOW + "EXIT" + ChatColor.GREEN + " _o       ");
        sign.setLine(1, ChatColor.GREEN + "/ /\\_ ");
        sign.setLine(2, ChatColor.GREEN + "     _/\\  " + ChatColor.YELLOW + "LOBBY ");
        sign.setLine(3, ChatColor.GREEN + "/  ");
    }
}
