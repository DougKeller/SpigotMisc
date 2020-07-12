package io.github.dougkeller.spigot_misc.components;

import org.bukkit.plugin.java.JavaPlugin;

public class Component {
    protected JavaPlugin plugin;

    public Component(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
