package io.github.dougkeller.spigot_misc.mini_plugins;

import io.github.dougkeller.spigot_misc.common.InventoryCombiner;
import io.github.dougkeller.spigot_misc.common.InventorySorter;
import org.bukkit.block.Chest;
import org.bukkit.block.DoubleChest;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSortChests extends MiniPlugin {
    private final int MAIN_INVENTORY_START = 9;
    private final int MAIN_INVENTORY_SIZE = 27;

    public AutoSortChests(JavaPlugin plugin) {
        super(plugin);
    }

    public void handle(InventoryOpenEvent event) {
        Inventory inventory = event.getInventory();
        if (!isStorageChest(inventory)) {
            return;
        }

        (new InventoryCombiner(inventory)).combine();
        (new InventorySorter(inventory)).sort();
    }

    private boolean isStorageChest(Inventory inventory) {
        return isWoodenChest(inventory) || isEnderChest(inventory);
    }

    private boolean isWoodenChest(Inventory inventory) {
        if (inventory.getType() != InventoryType.CHEST) {
            return false;
        }

        InventoryHolder holder = inventory.getHolder();
        return holder instanceof Chest || holder instanceof DoubleChest;
    }

    private boolean isEnderChest(Inventory inventory) {
        return inventory.getType() == InventoryType.ENDER_CHEST;
    }

    public void handle(EntityPickupItemEvent event) {
        boolean isPlayer = event.getEntity() instanceof Player;
        if (!isPlayer) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                Player player = (Player) event.getEntity();
                sortInventory(player.getInventory());
            }
        }.runTaskLater(plugin, 1);
    }

    private void sortInventory(Inventory inventory) {
        if (isStorageChest(inventory)) {
            (new InventoryCombiner(inventory)).combine();
            (new InventorySorter(inventory)).sort();
        }

        if (isPlayerInventory(inventory)) {
            (new InventoryCombiner(inventory)).combine();
            (new InventorySorter(inventory, MAIN_INVENTORY_START, MAIN_INVENTORY_SIZE)).sort();
        }
    }

    private boolean isPlayerInventory(Inventory inventory) {
        return inventory instanceof PlayerInventory;
    }

    public void handle(InventoryClickEvent event) {
        InventoryView view = event.getView();
        Inventory oppositeInventory = event.getClickedInventory() == view.getTopInventory() ? view.getBottomInventory() : view.getTopInventory();
        if (!isStorageChest(oppositeInventory) && !isPlayerInventory(oppositeInventory)) {
            return;
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                sortInventory(oppositeInventory);
            }
        }.runTaskLater(plugin, 1);
    }
}
