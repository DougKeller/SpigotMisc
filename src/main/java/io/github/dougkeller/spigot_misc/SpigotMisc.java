package io.github.dougkeller.spigot_misc;

import io.github.dougkeller.spigot_misc.mini_plugins.AutoSortChests;
import io.github.dougkeller.spigot_misc.mini_plugins.DeathCoordinates;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
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

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        DeathCoordinates plugin = new DeathCoordinates(this);
        plugin.handle(event);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        AutoSortChests plugin = new AutoSortChests(this);
        plugin.handle(event);
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent event) {
        AutoSortChests plugin = new AutoSortChests(this);
        plugin.handle(event);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        AutoSortChests plugin = new AutoSortChests(this);
        plugin.handle(event);
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        AutoSortChests plugin = new AutoSortChests(this);
        plugin.handle(event);
    }

    @EventHandler
    public void onInventoryPickupItem(InventoryPickupItemEvent event) {
        AutoSortChests plugin = new AutoSortChests(this);
        plugin.handle(event);
    }
}
