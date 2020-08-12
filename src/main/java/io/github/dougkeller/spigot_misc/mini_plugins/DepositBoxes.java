package io.github.dougkeller.spigot_misc.mini_plugins;

import io.github.dougkeller.spigot_misc.common.DepositBox;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
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

        Chest chest = DepositBox.getAutoSortableChest(inventory);
        Player player = (Player) event.getPlayer();
        DepositBox.newFromChest(chest, player).sort();
    }

    public void handle(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        BlockState blockState = event.getClickedBlock().getState();
        if (!(blockState instanceof Sign)) {
            return;
        }

        Sign sign = (Sign) blockState;
        if (!DepositBox.isDepositBox(sign)) {
            return;
        }

        DepositBox.newFromSign(sign, event.getPlayer()).sort();
    }
}
