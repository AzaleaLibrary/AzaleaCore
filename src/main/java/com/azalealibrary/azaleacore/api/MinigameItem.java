package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.AzaleaCore;
import com.azalealibrary.azaleacore.manager.PlaygroundManager;
import com.azalealibrary.azaleacore.playground.Playground;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class MinigameItem<E extends Event> implements Listener {

    private final ItemStack itemStack;

    public MinigameItem(ItemStack itemStack) {
        this.itemStack = itemStack;
        Bukkit.getPluginManager().registerEvents(this, AzaleaCore.INSTANCE);
    }

    public ItemStack getItemStack() {
        return itemStack.clone();
    }

    protected final void handleEvent(E event) {
        Player player = getPlayer(event);

        if (player != null) {
            ItemMeta itemMeta = player.getInventory().getItemInMainHand().getItemMeta();
            ItemMeta selfMeta = itemStack.getItemMeta();

            if (itemMeta != null && selfMeta != null && Objects.equals(itemMeta.toString(), selfMeta.toString())) {
                onUse(event, player, PlaygroundManager.getInstance().get(player));
            }
        }
    }

    /**
     * Systematically implement this method by calling {@link MinigameItem#handleEvent(Event)} in child class:
     *
     * <pre>{@code
     * @EventHandler
     * @Override
     * protected void onEvent(PlayerMoveEvent event) {
     *     super.handleEvent(event);
     * }
     * }</pre>
     *
     * @param event The event.
     */
    @SuppressWarnings("unused") // TODO - review, generic events does not work with spigot
    protected abstract void onEvent(E event);

    protected abstract @Nullable Player getPlayer(E event);

    protected abstract void onUse(E event, Player player, @Nullable Playground playground);

    public static class Builder {

        private final ItemStack itemStack;
        private final ItemMeta meta;
        private final List<String> lore;

        public Builder(Material material) {
            itemStack = new ItemStack(material);
            meta = itemStack.getItemMeta();
            lore = new ArrayList<>();
        }

        public Builder named(String name) {
            meta.setDisplayName(name);
            return this;
        }

        public Builder count(int count) {
            itemStack.setAmount(count);
            return this;
        }

        public Builder unbreakable() {
            meta.setUnbreakable(true);
            return this;
        }

        public Builder addLore(String line) {
            lore.add(line);
            return this;
        }

        public Builder addEnchantment(Enchantment enchantment, int level) {
            meta.addEnchant(enchantment, level, true);
            return this;
        }

        public Builder addFlags(ItemFlag... itemFlag) {
            meta.addItemFlags(itemFlag);
            return this;
        }

        public Builder addAttribute(Attribute attribute, AttributeModifier attributeModifier) {
            meta.addAttributeModifier(attribute, attributeModifier);
            return this;
        }

        public ItemStack build(int damage) {
            ((Damageable) meta).setDamage(damage);
            return build();
        }

        public ItemStack build() {
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            return itemStack;
        }
    }
}
