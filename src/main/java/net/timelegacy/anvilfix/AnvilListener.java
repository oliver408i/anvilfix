package net.timelegacy.anvilfix;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;

import org.bukkit.plugin.java.JavaPlugin;

public class AnvilListener implements Listener {

    private AnvilFix plugin;

    public AnvilListener(AnvilFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        if (plugin.isLoggingEnabled()) {
            plugin.getLogger().info("Anvil prepare event triggered");
        }

        ItemStack firstItem = event.getInventory().getItem(0);
        ItemStack secondItem = event.getInventory().getItem(1);
        ItemStack result = event.getResult();

        if (firstItem == null || secondItem == null || result == null || result.getType() == Material.AIR) {
            if (plugin.isLoggingEnabled()) {
                plugin.getLogger().info("Missing input or result item in the anvil");
            }
            return;
        }

        if (plugin.isLoggingEnabled()) {
            plugin.getLogger().info("First item type: " + firstItem.getType().toString());
            plugin.getLogger().info("Second item type: " + secondItem.getType().toString());
        }

        boolean combiningBooks = (firstItem.getType() == Material.ENCHANTED_BOOK && secondItem.getType() == Material.ENCHANTED_BOOK);

        if (combiningBooks) {
            if (plugin.isLoggingEnabled()) {
                plugin.getLogger().info("Combining enchanted books, allowing vanilla behavior.");
            }
            return; // Allow vanilla behavior to combine books
        }

        boolean validEnchantment = false;

        if (secondItem.getType() == Material.ENCHANTED_BOOK && secondItem.hasItemMeta()) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) secondItem.getItemMeta();
            Map<Enchantment, Integer> enchantments = meta.getStoredEnchants();

            if (plugin.isLoggingEnabled()) {
                plugin.getLogger().info("Second item is an enchanted book with " + enchantments.size() + " enchantments");
            }

            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int level = entry.getValue();

                if (plugin.isLoggingEnabled()) {
                    plugin.getLogger().info("Checking enchantment: " + enchantment.getName() + " Level: " + level);
                }

                if (enchantment.canEnchantItem(firstItem)) {
                    if (plugin.isLoggingEnabled()) {
                        plugin.getLogger().info("First item can accept this enchantment");
                    }

                    result.addUnsafeEnchantment(enchantment, level);
                    validEnchantment = true;
                } else {
                    if (plugin.isLoggingEnabled()) {
                        plugin.getLogger().info("First item cannot accept this enchantment, skipping");
                    }
                }
            }

            if (!validEnchantment) {
                if (plugin.isLoggingEnabled()) {
                    plugin.getLogger().info("No valid enchantments found, setting result to null");
                }
                event.setResult(null);
            } else {
                event.setResult(result);
            }
        } else {
            if (plugin.isLoggingEnabled()) {
                plugin.getLogger().info("Second item is not an enchanted book or has no meta");
            }
        }
    }
}