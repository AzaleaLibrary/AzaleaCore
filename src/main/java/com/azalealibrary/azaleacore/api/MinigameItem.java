package com.azalealibrary.azaleacore.api;

import com.azalealibrary.azaleacore.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MinigameItem implements Listener {

    private final ItemStack itemStack;
    private final Consumer<PlayerInteractEvent> onToggle;

    public MinigameItem(ItemStack itemStack, Consumer<PlayerInteractEvent> onToggle) {
        this.itemStack = itemStack;
        this.onToggle = onToggle;
        Bukkit.getPluginManager().registerEvents(this, Main.INSTANCE);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @EventHandler
    public final void onPlayerInteractEvent(PlayerInteractEvent event) {
        if (event.hasItem() && event.getItem().getItemMeta().getAsString().equals(itemStack.getItemMeta().getAsString())) {
            onToggle.accept(event);
        }
    }

    public static Builder create(Material material, int amount) {
        return new Builder(material, amount);
    }

    public static class Builder {

        private final ItemStack itemStack;
        private final ItemMeta meta;
        private final List<String> lore;
        private Consumer<PlayerInteractEvent> onToggle = event -> {};

        private Builder(Material material, int amount) {
            itemStack = new ItemStack(material, amount);
            meta = itemStack.getItemMeta();
            lore = new ArrayList<>();
        }

        public Builder called(String displayName) {
            meta.setDisplayName(displayName);
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

        public Builder addAttributeModifier(Attribute attribute, AttributeModifier attributeModifier) {
            meta.addAttributeModifier(attribute, attributeModifier);
            return this;
        }

        public Builder unbreakable() {
            meta.setUnbreakable(true);
            return this;
        }

        public Builder onToggle(Consumer<PlayerInteractEvent> onToggle) {
            this.onToggle = onToggle;
            return this;
        }

        public MinigameItem build(int damage) {
            ((Damageable) meta).setDamage(damage);
            return build();
        }

        public MinigameItem build() {
            meta.setLore(lore);
            itemStack.setItemMeta(meta);
            return new MinigameItem(itemStack, onToggle);
        }
    }
}
