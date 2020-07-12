package io.github.dougkeller.spigot_misc.mini_plugins;

import io.github.dougkeller.spigot_misc.common.InventorySorter;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoSortChests extends MiniPlugin {
    public AutoSortChests(JavaPlugin plugin) {
        super(plugin);
    }

    public void handle(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.getType() != InventoryType.CHEST) {
            return;
        }

        InventorySorter sorter = new InventorySorter(inventory);
        sorter.sort();
    }
}
