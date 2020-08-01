package io.github.dougkeller.spigot_misc.mini_plugins;

import io.github.dougkeller.spigot_misc.common.DepositBox;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class DepositBoxes extends MiniPlugin {

    public DepositBoxes(JavaPlugin plugin) {
        super(plugin);
    }

    public void handle(InventoryCloseEvent event) {
        Inventory inventory = event.getInventory();
        if (!DepositBox.isDepositBox(inventory)) {
            return;
        }

        Player player = (Player) event.getPlayer();
        (new DepositBox(inventory, player)).sort();
    }
}
