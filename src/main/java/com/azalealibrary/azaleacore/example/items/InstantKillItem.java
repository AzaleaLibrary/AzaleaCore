package com.azalealibrary.azaleacore.example.items;

import com.azalealibrary.azaleacore.api.MinigameItem;
import com.azalealibrary.azaleacore.playground.Playground;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;

public class InstantKillItem extends MinigameItem<EntityDamageByEntityEvent> {

    public InstantKillItem(ItemStack itemStack) {
        super(itemStack);
    }

    @EventHandler
    @Override
    protected void onEvent(EntityDamageByEntityEvent event) {
        super.handleEvent(event);
    }

    @Override
    protected @Nullable Player getPlayer(EntityDamageByEntityEvent event) {
        return event.getDamager() instanceof Player player ? player : null;
    }

    @Override
    protected void onUse(EntityDamageByEntityEvent event, Player player, @Nullable Playground playground) {
        if (event.getEntity() instanceof Player victim) {
            victim.setHealth(0);
        } else {
            event.getEntity().remove();
        }
    }
}
