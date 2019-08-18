package iomatix.spigot.rpgleveledmobs;

import java.io.IOException;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import iomatix.spigot.rpgleveledmobs.config.cfgModule;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.cmds.RPGlvlmobsCommand;
import iomatix.spigot.rpgleveledmobs.cmds.cmdModule;

public class Main extends JavaPlugin {
	public static Main RPGMobs;

	cmdModule commandModule;
	cfgModule configModule;

	@Override
	public void onEnable() {
		final long startTime = System.currentTimeMillis();
		(Main.RPGMobs = this).loadModules();

		final long endTime = System.currentTimeMillis();
		LogsModule.info(ChatColor.GRAY + " [" + ChatColor.GOLD + "RPG Leveled Mobs" + ChatColor.GRAY + "] "
				+ ChatColor.GRAY + "enabled in " + ChatColor.GOLD + (endTime * 1.0 - startTime * 1.0) / 1000.0
				+ ChatColor.GRAY + " seconds");
		// metrics

	}

	@Override
	public void onDisable() {
		LogsModule.saveLog();
//mobs removal?
	}

	private void loadModules() {
		this.configModule = this.getConfigModule();
		this.commandModule = new cmdModule();
		Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) this, (Runnable) new Runnable() {
			@Override
			public void run() {
				// dmg
				// experience
			}
		}, 40L);
	}

	public void registerCommand(final RPGlvlmobsCommand command) {
		this.commandModule.registerCommand(command);
	}

	public cfgModule getConfigModule() {
		return cfgModule.getConfigModule();
	}

//mobarenax2

//leveledmobs

//moblevels

}
