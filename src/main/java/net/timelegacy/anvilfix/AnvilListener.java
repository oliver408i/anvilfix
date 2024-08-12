package net.timelegacy.anvilfix;

import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.InventoryView;

import org.bukkit.plugin.java.JavaPlugin;

public class AnvilListener implements Listener {

    final private JavaPlugin plugin;

    public AnvilListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        plugin.getLogger().info("Anvil prepare event triggered");

        ItemStack firstItem = event.getInventory().getItem(0);
        ItemStack secondItem = event.getInventory().getItem(1);
        ItemStack result = event.getResult();

        if (firstItem == null || secondItem == null || result == null || result.getType() == Material.AIR) {
            plugin.getLogger().info("Missing input or result item in the anvil");
            return;
        }

        plugin.getLogger().info("First item type: " + firstItem.getType().toString());
        plugin.getLogger().info("Second item type: " + secondItem.getType().toString());

        // Check if we're combining enchanted books
        boolean combiningBooks = (firstItem.getType() == Material.ENCHANTED_BOOK && secondItem.getType() == Material.ENCHANTED_BOOK);

        if (combiningBooks) {
            plugin.getLogger().info("Combining enchanted books, allowing vanilla behavior.");
            return; // Allow vanilla behavior to combine books
        }

        boolean validEnchantment = false;

        // Check if the second item is an enchanted book
        if (secondItem.getType() == Material.ENCHANTED_BOOK && secondItem.hasItemMeta()) {
            EnchantmentStorageMeta meta = (EnchantmentStorageMeta) secondItem.getItemMeta();
            Map<Enchantment, Integer> enchantments = meta.getStoredEnchants();

            plugin.getLogger().info("Second item is an enchanted book with " + enchantments.size() + " enchantments");

            for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
                Enchantment enchantment = entry.getKey();
                int level = entry.getValue();
                
                plugin.getLogger().info("Checking enchantment: " + enchantment.getName() + " Level: " + level);

                // Check if the first item can accept this enchantment
                if (enchantment.canEnchantItem(firstItem)) {
                    plugin.getLogger().info("First item can accept this enchantment");

                    // Apply the enchantment with the level from the book, bypassing the vanilla cap
                    result.addUnsafeEnchantment(enchantment, level);
                    validEnchantment = true;
                } else {
                    plugin.getLogger().info("First item cannot accept this enchantment, skipping");
                }
            }

            // If no valid enchantment was applied, set the result to null
            if (!validEnchantment) {
                plugin.getLogger().info("No valid enchantments found, setting result to null");
                event.setResult(null);
            } else {
                // Set the modified result item back in the event if valid
                event.setResult(result);
            }
        } else {
            plugin.getLogger().info("Second item is not an enchanted book or has no meta");
        }
    }
}