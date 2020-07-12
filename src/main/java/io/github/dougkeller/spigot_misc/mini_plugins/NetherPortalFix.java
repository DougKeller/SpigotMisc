package io.github.dougkeller.spigot_misc.mini_plugins;

import org.bukkit.Location;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class NetherPortalFix extends MiniPlugin {
    public NetherPortalFix(JavaPlugin plugin) {
        super(plugin);
    }

    public void handle(PlayerPortalEvent event) {
        Location to = event.getTo();
        Location from = event.getFrom();
        if (to.getWorld().getName().equals("world_nether")
                && from.getWorld().getName().equals("world")) {
            to.setX(Math.floor(from.getX() / 8));
            to.setZ(Math.floor(from.getZ() / 8));
            // event.setSearchRadius(0);
            // event.setCreationRadius(0);
        }

        if (to.getWorld().getName().equals("world")
                && from.getWorld().getName().equals("world_nether")) {

        }

        plugin.getLogger().info(String.format("Search Radius: %d", event.getSearchRadius()));
        plugin.getLogger().info(String.format("Creation Radius: %d", event.getCreationRadius()));
        plugin.getLogger().info(String.format("Can Create Portal: %b", event.getCanCreatePortal()));

        plugin.getLogger().info(String.format("To: %s (%f, %f, %f)", to.getWorld().getName(), to.getX(),
                to.getY(), to.getZ()));
        plugin.getLogger().info(String.format("From: %s (%f, %f, %f)", from.getWorld().getName(),
                from.getX(), from.getY(), from.getZ()));
    }
}
