package io.github.dougkeller.spigot_misc;

import io.github.dougkeller.spigot_misc.components.DeathCoordinatesComponent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
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
    //        NetherPortalFixComponent handler = new NetherPortalFixComponent(this);
    //        handler.handle(event);
    //    }

    @EventHandler
    public void onPlayerDeath(EntityDeathEvent event) {
        DeathCoordinatesComponent handler = new DeathCoordinatesComponent(this);
        handler.handle(event);
    }
}
