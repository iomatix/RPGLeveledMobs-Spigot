package iomatix.spigot.rpgleveledmobs.tools;

import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

import iomatix.spigot.rpgleveledmobs.Main;

public class MoneyModule implements Listener {
    public MoneyModule() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.RPGMobs);
    }
    
    @EventHandler
    public void onMobDrop(final EntityDeathEvent event) {
    }
}
