package io.github.dougkeller.spigot_misc;

import io.github.dougkeller.spigot_misc.mini_plugins.AutoSortChests;
import io.github.dougkeller.spigot_misc.mini_plugins.DeathCoordinates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpigotMisc extends JavaPlugin implements Listener {
    @Override
    public void onDisable() {
        getLogger().info("SpigotMisc disabled!");
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        getLogger().info("SpigotMisc loaded!");
    }

    //    @EventHandler
    //    public void onPlayerPortal(PlayerPortalEvent event) {
    //        NetherPortalFix plugin = new NetherPortalFix(this);
    //        plugin.handle(event);
    //    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        DeathCoordinates plugin = new DeathCoordinates(this);
        plugin.handle(event);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        AutoSortChests plugin = new AutoSortChests(this);
        plugin.handle(event);
    }

    //    @EventHandler
    //    public void onEntityPickupItem(EntityPickupItemEvent event) {
    //        AutoCombine plugin = new AutoCombine(this);
    //        plugin.handle(event);
    //    }

    //    @EventHandler
    //    public void onInventory(InventoryEvent event) {
    //        getLogger().info(event.getEventName());
    //    }
}
