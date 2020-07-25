package io.github.dougkeller.spigot_misc.mini_plugins;

import io.github.dougkeller.spigot_misc.common.InventorySorter;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoSortChests extends MiniPlugin {
    public AutoSortChests(JavaPlugin plugin) {
        super(plugin);
    }

    public void handle(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        if (!canSortInventory(inventory)) {
            return;
        }

        InventorySorter sorter = new InventorySorter(inventory);
        sorter.sort();
    }

    private boolean canSortInventory(Inventory inventory) {
        return isStorageChest(inventory) || isEnderChest(inventory);
    }

    private boolean isStorageChest(Inventory inventory) {
        if (inventory.getType() != InventoryType.CHEST) {
            return false;
        }

        InventoryHolder holder = inventory.getHolder();
        return holder instanceof Chest || holder instanceof DoubleChest;
    }

    private boolean isEnderChest(Inventory inventory) {
        return inventory.getType() == InventoryType.ENDER_CHEST;
    }
}
