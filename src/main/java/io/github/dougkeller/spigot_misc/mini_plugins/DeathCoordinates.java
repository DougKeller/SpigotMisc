package io.github.dougkeller.spigot_misc.mini_plugins;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathCoordinates extends MiniPlugin {
    public DeathCoordinates(JavaPlugin plugin) {
        super(plugin);
    }

    public void handle(EntityDeathEvent event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof Player)) {
            return;
        }

        notifyCoordinates((Player) entity);
    }

    public void notifyCoordinates(Player player) {
        Location location = player.getLocation();
        String message = String.format(
                "%sYou died at %s%d %d %d",
                ChatColor.AQUA,
                ChatColor.GOLD,
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );

        player.sendMessage(message);
    }
}
