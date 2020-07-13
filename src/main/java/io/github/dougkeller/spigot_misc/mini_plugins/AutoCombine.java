package io.github.dougkeller.spigot_misc.mini_plugins;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class AutoCombine extends MiniPlugin {
    public AutoCombine(JavaPlugin plugin) {
        super(plugin);
    }

    public void handle(EntityPickupItemEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        compressInventory((Player) entity);
    }

    public void compressInventory(Player player) {
        Inventory inventory = player.getInventory();
        ItemStack[] itemStacks = inventory.getStorageContents();

        for (int i = 0; i < itemStacks.length; ++i) {
            ItemStack itemStack = itemStacks[i];
            if (itemStack == null) {
                continue;
            }

            String message = String.format(
                    "[%d]: %s",
                    i,
                    itemStack.toString()
            );
            plugin.getLogger().info(message);
        }
    }
}
