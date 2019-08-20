package iomatix.spigot.rpgleveledmobs;

import java.io.IOException;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

import com.garbagemule.MobArena.MobArena;

import iomatix.spigot.rpgleveledmobs.config.cfgModule;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.mobscaling.ExperienceScalingModule;
import iomatix.spigot.rpgleveledmobs.mobscaling.StatsScalingModule;
import iomatix.spigot.rpgleveledmobs.spawnsController.SpawnModule;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;
import iomatix.spigot.rpgleveledmobs.tools.Metrics;
import iomatix.spigot.rpgleveledmobs.userInterface.MenuHandler;
import iomatix.spigot.rpgleveledmobs.cmds.RPGlvlmobsCommand;
import iomatix.spigot.rpgleveledmobs.cmds.cmdModule;

public class Main extends JavaPlugin {
	
	public static Main RPGMobs;
	
	cmdModule commandModule;
	cfgModule configModule;
	MenuHandler menuHandler;
    SpawnModule spawnModule;
    ExperienceScalingModule experienceModule;
    StatsScalingModule scalingModule;

	@Override
	public void onEnable() {
		final long startTime = System.currentTimeMillis();
		(Main.RPGMobs = this).loadModules();

		final long endTime = System.currentTimeMillis();
		LogsModule.info(ChatColor.GRAY + " [" + ChatColor.GOLD + "RPG Leveled Mobs" + ChatColor.GRAY + "] "
				+ ChatColor.GRAY + "enabled in " + ChatColor.GOLD + (endTime * 1.0 - startTime * 1.0) / 1000.0
				+ ChatColor.GRAY + " seconds");
		try {
            final Metrics metrics = new Metrics((Plugin)this);
            metrics.start();
        }
        catch (IOException e) {
            LogsModule.warning("Error starting metrics!");
        
        }
		
	}

	@Override
	public void onDisable() {
		LogsModule.saveLog();
		 for (final World world : Bukkit.getWorlds()) {
	            for (final LivingEntity ent : world.getLivingEntities()) {
	                if (ent.hasMetadata(MetaTag.RPGmob.toString())) {
	                    for (final MetaTag tag : MetaTag.values()) {
	                        ent.removeMetadata(tag.toString(), (Plugin)this);
	                    }
	                    ent.remove();
	                }
	            }
	        }
	}

	private void loadModules() {
		this.configModule = this.getConfigModule();
		this.commandModule = new cmdModule();
		this.menuHandler = new MenuHandler();
		this.spawnModule = new SpawnModule();
		Bukkit.getScheduler().runTaskLaterAsynchronously((Plugin) this, (Runnable) new Runnable() {
			@Override
			public void run() {
                Main.this.experienceModule = new ExperienceScalingModule();
                Main.this.scalingModule = new StatsScalingModule();
			}
		}, 40L);
	}

	public void registerCommand(final RPGlvlmobsCommand command) {
		this.commandModule.registerCommand(command);
	}

	public cfgModule getConfigModule() {
		return cfgModule.getConfigModule();
	}

	    public static MobArena getMobArena() {
	        return (MobArena)Bukkit.getPluginManager().getPlugin("MobArena");
	    }
	    
	    public static boolean isMobArenaLoaded() {
	        return Bukkit.getPluginManager().isPluginEnabled("MobArena");
	    }
	    
	    public static boolean isLeveledMob(final Entity ent) {
	        return ent.hasMetadata(MetaTag.RPGmob.toString()) && ent.hasMetadata(MetaTag.Level.toString());
	    }
	    
	    public static int getMobLevel(final Entity ent) {
	        if (!isLeveledMob(ent)) {
	            return -1;
	        }
	        return ent.getMetadata(MetaTag.Level.toString()).get(0).asInt();
	    }

}