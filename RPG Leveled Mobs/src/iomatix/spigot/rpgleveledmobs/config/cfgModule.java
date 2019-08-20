package iomatix.spigot.rpgleveledmobs.config;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.UUID;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;
import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;

public class cfgModule {

	private static HashMap<UUID, WorldConfig> configMap;
	private static HashSet<UUID> enabledWorlds;
	private static GlobalConfig global;
	private static cfgModule instance;
	public static double version;

	private cfgModule() {
		cfgModule.configMap = new HashMap<UUID, WorldConfig>();
		cfgModule.enabledWorlds = new HashSet<UUID>();
		cfgModule.global = new GlobalConfig();
		LogsModule.info("Trying to load up config files...");
		this.loadConfig();
		this.handleOldConfig();
		LogsModule.info("Configuration loading success.");
		cfgModule.instance = this;
	}

	private void loadConfig() {
		for (final World world : Bukkit.getWorlds()) {
			cfgModule.configMap.put(world.getUID(), new WorldConfig(world, cfgModule.global));
		}
	}

	private void handleOldConfig() {
		final File dir = new File(Main.RPGMobs.getDataFolder().toString() + File.separator + "Spawning");
		if (dir.exists()) {
			LogsModule.info("Old config Found, Converting to new config!");
			this.convertOldConfig();
			try {
				Files.move(dir, new File(Main.RPGMobs.getDataFolder().toString() + File.separator + "Spawning.old"));
			} catch (IOException e) {
				LogsModule.error("Erorr Moving Old Config.");
				e.printStackTrace();
			}
			LogsModule.info("Conversion complete!");
		}
	}

	private void convertOldConfig() {
		final configHandler oldConfig = new configHandler(Main.RPGMobs, "Spawning" + File.separator + "MobSpawns");
		final List<String> enabledWorlds = (List<String>) oldConfig.getConfig().getStringList("Worlds");
		for (final String world : enabledWorlds) {
			LogsModule.info("Found world: " + world + " - Converting to new config!");
			final World tempWorld = Bukkit.getWorld(world);
			if (tempWorld != null) {
				final WorldConfig wc = cfgModule.configMap.get(tempWorld.getUID());
				if (wc != null) {
					wc.setEnabled(true);
				}
				if (oldConfig.getConfig().contains(world + ".LevelNameFormat")) {
					LogsModule.debug("World: " + world + " - Found LevelNameFormat, saving setting!");
					wc.setPrefixFormat(oldConfig.getConfig().getString(world + ".LevelNameFormat"));
					wc.setSuffixFormat(oldConfig.getConfig().getString(world + ".LevelNameFormat"));
				}
				if (oldConfig.getConfig().contains(world + ".UsePrefix")) {
					LogsModule.debug("World: " + world + " - Found UsePrefix, saving setting!");
					wc.setPrefixEnabled(oldConfig.getConfig().getBoolean(world + ".UsePrefix"));
				}
				if (oldConfig.getConfig().contains(world + ".UseSuffix")) {
					LogsModule.debug("World: " + world + " - Found UseSuffix, saving setting!");
					wc.setSuffixEnabled(oldConfig.getConfig().getBoolean(world + ".UseSuffix"));
				}
				if (oldConfig.getConfig().contains(world + ".DamageModifierEnabled")) {
					LogsModule.debug("World: " + world + " - Found DamageModifierEnabled, saving setting!");
					wc.setDamageModified(oldConfig.getConfig().getBoolean(world + "DamageModifierEnabled"));
				}
				if (oldConfig.getConfig().contains(world + ".DefenseModifierEnabled")) {
					LogsModule.debug("World: " + world + " - Found DefenseModifierEnabled, saving setting!");
					wc.setDefenseModified(oldConfig.getConfig().getBoolean(world + "DefenseModifierEnabled"));
				}
				if (oldConfig.getConfig().contains(world + ".MobArenaWaveLeveling")) {
					LogsModule.debug("World: " + world + " - Found MobArenaWaveLeveling, saving setting!");
					wc.setMobArenaWaveLeveled(oldConfig.getConfig().getBoolean(world + "MobArenaWaveLeveling"));
					wc.setMobArenaLeveled(oldConfig.getConfig().getBoolean(world + "MobArenaWaveLeveling"));
				}
				if (oldConfig.getConfig().contains(world + ".DistancePerLevel")) {
					LogsModule.debug("World: " + world + " - Found DistancePerLevel, saving setting!");
					wc.setDistancePerLevel(oldConfig.getConfig().getDouble(world + "DistancePerLevel"));
				}
				if (oldConfig.getConfig().contains(world + ".MaxLevel")) {
					LogsModule.debug("World: " + world + " - Found MaxLevel, saving setting!");
					wc.setMaxLevel(oldConfig.getConfig().getInt(world + ".MaxLevel"));
				}
				if (oldConfig.getConfig().contains(world + ".ExperienceModifier")) {
					LogsModule.debug("World: " + world + " - Found ExperienceModifier, saving setting!");
					wc.setExperienceMultiplier(oldConfig.getConfig().getDouble(world + ".ExperienceModifier"));
					wc.setExperienceModified(true);
				}
				if (oldConfig.getConfig().contains(world + ".HealthMultiplier")) {
					LogsModule.debug("World: " + world + " - Found HealthMultiplier, saving setting!");
					wc.setHealthMultiplier(oldConfig.getConfig().getDouble(world + ".HealthMultiplier"));
					wc.setHealthModified(true);
				}
				if (oldConfig.getConfig().contains(world + ".DamageMultiplier")) {
					LogsModule.debug("World: " + world + " - Found DamageMultiplier, saving setting!");
					wc.setDamageMultiplier(oldConfig.getConfig().getDouble(world + ".DamageMultiplier"));
					wc.setDamageModified(true);
				}
				if (oldConfig.getConfig().contains(world + ".DefenseMultiplier")) {
					LogsModule.debug("World: " + world + " - Found DefenseMultiplier, saving setting!");
					wc.setDefenseMultiplier(oldConfig.getConfig().getDouble(world + ".DefenseMultiplier"));
					wc.setDefenseModified(true);
				}
				for (final String nodeKey : oldConfig.getConfig().getConfigurationSection(world + ".spawnLocations")
						.getKeys(false)) {
					LogsModule
							.debug("World: " + world + " - Found SpawnPoint: " + nodeKey + " - Loading Configuration!");
					final String tag = world + ".spawnLocations." + nodeKey;
					final SpawnNode node = wc.addSpawnNode(oldConfig.getConfig().getDouble(tag + ".x", 0.0),
							oldConfig.getConfig().getDouble(tag + ".y", 0.0),
							oldConfig.getConfig().getDouble(tag + ".z", 0.0));
					if (oldConfig.getConfig().contains(tag + ".LevelNameFormat")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found LevelNameFormat, saving setting!");
						node.setPrefixFormat(oldConfig.getConfig().getString(tag + ".LevelNameFormat"));
						node.setSuffixFormat(oldConfig.getConfig().getString(tag + ".LevelNameFormat"));
					}
					if (oldConfig.getConfig().contains(tag + ".startLevel")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found StartLevel, saving setting!");
						node.setStartLevel(oldConfig.getConfig().getInt(tag + ".startLevel"));
					}
					if (oldConfig.getConfig().contains(tag + ". UsePrefix")) {
						LogsModule.debug(
								"World: " + world + " - SpawnNode: " + nodeKey + " - Found UsePrefix, saving setting!");
						node.setPrefixEnabled(oldConfig.getConfig().getBoolean(tag + ".UsePrefix"));
					}
					if (oldConfig.getConfig().contains(tag + ".UseSuffix")) {
						LogsModule.debug(
								"World: " + world + " - SpawnNode: " + nodeKey + " - Found UseSuffix, saving setting!");
						node.setSuffixEnabled(oldConfig.getConfig().getBoolean(tag + ".UseSuffix"));
					}
					if (oldConfig.getConfig().contains(tag + ".DamageModifierEnabled")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found DamageModifierEnabled, saving setting!");
						node.setDamageModified(oldConfig.getConfig().getBoolean(tag + ".DamageModifierEnabled"));
					}
					if (oldConfig.getConfig().contains(tag + ".DefenseModifierEnabled")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found DefenseModifierEnabled, saving setting!");
						node.setDefenseModified(oldConfig.getConfig().getBoolean(tag + ".DefenseModifierEnabled"));
					}
					if (oldConfig.getConfig().contains(tag + ".MobArenaWaveLeveling")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found MobArenaWaveLeveling, saving setting!");
						node.setMobArenaWaveLeveled(oldConfig.getConfig().getBoolean(tag + ".MobArenaWaveLeveling"));
					}
					if (oldConfig.getConfig().contains(tag + ".DistancePerLevel")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found DistancePerLevel, saving setting!");
						node.setDistancePerLevel(oldConfig.getConfig().getDouble(tag + ".DistancePerLevel"));
					}
					if (oldConfig.getConfig().contains(tag + ".MaxLevel")) {
						LogsModule.debug(
								"World: " + world + " - SpawnNode: " + nodeKey + " - Found MaxLevel, saving setting!");
						node.setMaxLevel(oldConfig.getConfig().getInt(tag + ".MaxLevel"));
					}
					if (oldConfig.getConfig().contains(tag + ".ExperienceModifier")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found ExperienceModifier, saving setting!");
						node.setExperienceMultiplier(oldConfig.getConfig().getDouble(tag + ".ExperienceModifier"));
					}
					if (oldConfig.getConfig().contains(tag + ".HealthMultiplier")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found HealthMultiplier, saving setting!");
						node.setHealthMultiplier(oldConfig.getConfig().getDouble(tag + ".HealthMultiplier"));
					}
					if (oldConfig.getConfig().contains(tag + ".DamageMultiplier")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found DamageMultiplier, saving setting!");
						node.setDamageMultiplier(oldConfig.getConfig().getDouble(tag + ".DamageMultiplier"));
					}
					if (oldConfig.getConfig().contains(tag + ".DefenseMultiplier")) {
						LogsModule.debug("World: " + world + " - SpawnNode: " + nodeKey
								+ " - Found DefenseMultiplier, saving setting!");
						node.setDefenseMultiplier(oldConfig.getConfig().getDouble(tag + ".DefenseMultiplier"));
					}
					node.saveConfig();
				}
				wc.saveConfig();
				wc.saveNodeConfig();
			} else {
				LogsModule.warning("Error - bukkit world not found. Aborting!");
			}
		}
	}

	public Collection<WorldConfig> getWorldConfigs() {
		return cfgModule.configMap.values();
	}

	public WorldConfig getConfig(final String name) {
		final World world = Bukkit.getWorld(name);
		if (world == null) {
			return null;
		}
		return this.getConfig(world);
	}

	public void globalUpdate() {
		for (final WorldConfig config : cfgModule.configMap.values()) {
			config.updateInheritedValues();
		}
	}

	public WorldConfig getConfig(final World world) {
		return this.getConfig(world.getUID());
	}

	public WorldConfig getConfig(final UUID uuid) {
		return cfgModule.configMap.get(uuid);
	}

	public GlobalConfig getGlobalConfig() {
		return cfgModule.global;
	}

	public static cfgModule getConfigModule() {
		if (cfgModule.instance == null) {
			new cfgModule();
		}
		return cfgModule.instance;
	}

	public SpawnNode getSpawnNode(final Location loc) {
		final WorldConfig config = this.getConfig(loc.getWorld());
		if (config == null || !config.isEnabled()) {
			return null;
		}
		return config.getClosestSpawnNode(loc);
	}

	static {
		cfgModule.configMap = new HashMap<UUID, WorldConfig>();
	}
}
