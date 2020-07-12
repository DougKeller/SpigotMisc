package io.github.dougkeller.spigot_misc.mini_plugins;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityPickupItemEvent;
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
        ItemStack[] itemStacks = player.getInventory().getStorageContents();
        for (int i = 0; i < itemStacks.length; ++i) {
            ItemStack itemStack = itemStacks[i];
            String message;

            if (itemStack == null) {
                message = String.format(
                        "[%d] Nothing",
                        i
                );
            } else {
                message = String.format(
                        "[%d] %dx %s",
                        i,
                        itemStack.getAmount(),
                        itemStack.getData().toString()
                );
            }

            plugin.getLogger().info(message);
        }
    }
}
