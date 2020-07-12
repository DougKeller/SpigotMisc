package io.github.dougkeller.spigot_misc.components;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class DeathCoordinatesComponent extends Component {
    public DeathCoordinatesComponent(JavaPlugin plugin) {
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
                "You died at X%d Y%d Z%d",
                location.getBlockX(),
                location.getBlockY(),
                location.getBlockZ()
        );
        player.chat(message);
    }
}
