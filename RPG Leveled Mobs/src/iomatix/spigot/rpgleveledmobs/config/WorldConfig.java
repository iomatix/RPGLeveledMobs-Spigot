package iomatix.spigot.rpgleveledmobs.config;

import java.util.Iterator;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.io.File;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.EntityType;
import org.bukkit.World;

import iomatix.spigot.rpgleveledmobs.tools.Language;
import iomatix.spigot.rpgleveledmobs.Main;

public class WorldConfig extends RPGLeveledMobsConfig {

	private World world;
	private boolean enabled;
	private GlobalConfig global;
	private configHandler nodeConfig;
	private boolean useSuffix;
	private boolean usePrefix;
	private String suffixFormat;
	private String prefixFormat;
	private Language language;
	private double distancePerLevel;
	private int maxLevel;
	private int startLevel;
	private int minLevel;
	private boolean alwaysShowMobName;
	private boolean damageModified;
	private boolean defenseModified;
	private boolean moneyModified;
	private boolean experienceModified;
	private boolean healthModified;
	private double healthMultiplier;
	private double damageMultiplier;
	private double defenseMultiplier;
	private double moneyMultiplier;
	private double moneyRandomizer;
	private double experienceModifier;
	private double healthAddon;
	private double defenseAddon;
	private double damageAddon;
	private double experienceAddon;
	private boolean leveledMobArea;
	private boolean mobArenaWaveLeveling;
	private double mobArenaMultiplier;
	private double wavesPerLevel;
	private double TownyRatio;
	private boolean TownySubtract;
	private boolean TownyNationSupport;
	private boolean NoMoneyDrop;
	private boolean RPGLevelRandomizer;
	private int RPGLevelMax;
	private ArrayList<EntityType> leveledMobs;
	private ArrayList<EntityType> blockedMobs;
	private boolean leveledSpawners;
	private ArrayList<SpawnNode> nodeList;
	private int spawnNodeCount;
	private HashMap<EntityType, Double> moneyMobs;

	public WorldConfig(final World world, final GlobalConfig global) {
		this.nodeList = new ArrayList<SpawnNode>();
		this.spawnNodeCount = 0;
		this.world = world;
		this.inheritedValues = global.getSettings();
		this.config = new configHandler(Main.RPGMobs, world.getName() + File.separator + "config");
		this.global = global;
		SpawnNode.registerWorld(this);
		this.loadDefaults();
		this.loadConfig();
		this.loadNodes();
	}

	private void loadDefaults() {
		if (!this.config.getConfig().contains("Enabled")) {
			this.config.getConfig().set("Enabled", (Object) false);
		}
		this.config.saveConfig();
	}

	void loadConfig() {
		this.enabled = this.config.getConfig().getBoolean("Enabled", false);
		if (this.config.getConfig().contains(ConfigKey.LEVELED_MOBS.toString())) {
			final ArrayList<EntityType> allowed = new ArrayList<EntityType>();
			final ArrayList<String> temp = (ArrayList<String>) this.config.getConfig()
					.get(ConfigKey.LEVELED_MOBS.toString(), (Object) new ArrayList());
			for (final String mob : temp) {
				allowed.add(EntityType.valueOf(mob));
			}
			this.leveledMobs = allowed;
			this.inheritedValues.remove(ConfigKey.LEVELED_MOBS);
		}
		if (this.config.getConfig().contains(ConfigKey.BLOCKED_MOBS.toString())) {
			final ArrayList<EntityType> blocked = new ArrayList<EntityType>();
			final ArrayList<String> temp = (ArrayList<String>) this.config.getConfig()
					.get(ConfigKey.BLOCKED_MOBS.toString(), (Object) new ArrayList());
			for (final String mob : temp) {
				blocked.add(EntityType.valueOf(mob));
			}
			this.blockedMobs = blocked;
			this.inheritedValues.remove(ConfigKey.BLOCKED_MOBS);
		} else {
			this.config.getConfig().set(ConfigKey.BLOCKED_MOBS.toString(),
					(Object) this.entListToStringList(ConfigKey.getDefaultBlocked(this.world)));
			this.config.saveConfig();
			this.inheritedValues.remove(ConfigKey.BLOCKED_MOBS);
			final ArrayList<EntityType> blocked = new ArrayList<EntityType>();
			final ArrayList<String> temp = (ArrayList<String>) this.config.getConfig()
					.get(ConfigKey.BLOCKED_MOBS.toString(), (Object) new ArrayList());
			for (final String mob : temp) {
				blocked.add(EntityType.valueOf(mob));
			}
			this.blockedMobs = blocked;
		}
		if (this.config.getConfig().contains(ConfigKey.MONEY_MOBS.toString())) {
			final HashMap<String, Object> temp = new HashMap<String, Object>();
			final HashMap<EntityType, Double> hashDoubles = new HashMap<EntityType, Double>();
			try {
				temp.putAll(this.config.getConfig().getConfigurationSection(ConfigKey.MONEY_MOBS.toString())
						.getValues(false));
				for (final Map.Entry<String, Object> entry : temp.entrySet()) {
					hashDoubles.put(EntityType.valueOf(entry.getKey()),
							Double.parseDouble(entry.getValue().toString()));
				}
				this.moneyMobs = hashDoubles;
			} catch (NullPointerException e) {
				this.moneyMobs = ConfigKey.getDefaultMoney(this.world);
			}
		} else {
			this.config.getConfig().set(ConfigKey.MONEY_MOBS.toString(),
					(Object) this.MoneyHashMapToStringList(ConfigKey.getDefaultMoney(this.world)));
			this.config.saveConfig();
			this.inheritedValues.remove(ConfigKey.MONEY_MOBS);
			final HashMap<String, Object> temp = new HashMap<String, Object>();
			final HashMap<EntityType, Double> moneyMapHash = new HashMap<EntityType, Double>();
			try {
				temp.putAll(this.config.getConfig().getConfigurationSection(ConfigKey.MONEY_MOBS.toString())
						.getValues(false));
				for (final Map.Entry<String, Object> entry : temp.entrySet()) {
					moneyMapHash.put(EntityType.valueOf(entry.getKey()),
							Double.parseDouble(entry.getValue().toString()));
				}
				this.moneyMobs = moneyMapHash;
			} catch (NullPointerException e) {
				this.moneyMobs = ConfigKey.getDefaultMoney(this.world);
			}
		}
		if (this.config.getConfig().contains(ConfigKey.MIN_LEVEL.toString())) {
			this.minLevel = this.config.getConfig().getInt(ConfigKey.MIN_LEVEL.toString());
			this.inheritedValues.remove(ConfigKey.MIN_LEVEL);
		}
		if (this.config.getConfig().contains(ConfigKey.MAX_LEVEL.toString())) {
			this.maxLevel = this.config.getConfig().getInt(ConfigKey.MAX_LEVEL.toString());
			this.inheritedValues.remove(ConfigKey.MAX_LEVEL);
		}
		if (this.config.getConfig().contains(ConfigKey.START_LEVEL.toString())) {
			this.startLevel = this.config.getConfig().getInt(ConfigKey.START_LEVEL.toString());
			this.inheritedValues.remove(ConfigKey.START_LEVEL);
		}
		if (this.config.getConfig().contains(ConfigKey.DISTANCE_PER_LEVEL.toString())) {
			this.distancePerLevel = this.config.getConfig().getDouble(ConfigKey.DISTANCE_PER_LEVEL.toString());
			this.inheritedValues.remove(ConfigKey.DISTANCE_PER_LEVEL);
		}
		if (this.config.getConfig().contains(ConfigKey.HEALTH_MOD_ENABLED.toString())) {
			this.healthModified = this.config.getConfig().getBoolean(ConfigKey.HEALTH_MOD_ENABLED.toString());
			this.inheritedValues.remove(ConfigKey.HEALTH_MOD_ENABLED);
		}
		if (this.config.getConfig().contains(ConfigKey.HEALTH_PER_LEVEL.toString())) {
			this.healthMultiplier = this.config.getConfig().getDouble(ConfigKey.HEALTH_PER_LEVEL.toString());
			this.inheritedValues.remove(ConfigKey.HEALTH_PER_LEVEL);
		}
		if (this.config.getConfig().contains(ConfigKey.DAMAGE_MOD_ENABLE.toString())) {
			this.damageModified = this.config.getConfig().getBoolean(ConfigKey.DAMAGE_MOD_ENABLE.toString());
			this.inheritedValues.remove(ConfigKey.DAMAGE_MOD_ENABLE);
		}
		if (this.config.getConfig().contains(ConfigKey.DAMAGE_PER_LEVEL.toString())) {
			this.damageMultiplier = this.config.getConfig().getDouble(ConfigKey.DAMAGE_PER_LEVEL.toString());
			this.inheritedValues.remove(ConfigKey.DAMAGE_PER_LEVEL);
		}
		if (this.config.getConfig().contains(ConfigKey.DEFENSE_MOD_ENABLE.toString())) {
			this.defenseModified = this.config.getConfig().getBoolean(ConfigKey.DEFENSE_MOD_ENABLE.toString());
			this.inheritedValues.remove(ConfigKey.DEFENSE_MOD_ENABLE);
		}
		if (this.config.getConfig().contains(ConfigKey.DEFENSE_PER_LEVEL.toString())) {
			this.defenseMultiplier = this.config.getConfig().getDouble(ConfigKey.DEFENSE_PER_LEVEL.toString());
			this.inheritedValues.remove(ConfigKey.DEFENSE_PER_LEVEL);
		}
		if (this.config.getConfig().contains(ConfigKey.MONEY_MOD_ENABLE.toString())) {
			this.moneyModified = this.config.getConfig().getBoolean(ConfigKey.MONEY_MOD_ENABLE.toString());
			this.inheritedValues.remove(ConfigKey.MONEY_MOD_ENABLE);
		}
		if (this.config.getConfig().contains(ConfigKey.MONEY_PER_LEVEL.toString())) {
			this.moneyMultiplier = this.config.getConfig().getDouble(ConfigKey.MONEY_PER_LEVEL.toString());
			this.inheritedValues.remove(ConfigKey.MONEY_PER_LEVEL);
		}
		if (this.config.getConfig().contains(ConfigKey.MONEY_RANDOM.toString())) {
			this.moneyRandomizer = this.config.getConfig().getDouble(ConfigKey.MONEY_RANDOM.toString());
			this.inheritedValues.remove(ConfigKey.MONEY_RANDOM);
		}
		if (this.config.getConfig().contains(ConfigKey.EXPERIENCE_MOD_ENABLED.toString())) {
			this.experienceModified = this.config.getConfig().getBoolean(ConfigKey.EXPERIENCE_MOD_ENABLED.toString());
			this.inheritedValues.remove(ConfigKey.EXPERIENCE_MOD_ENABLED);
		}
		if (this.config.getConfig().contains(ConfigKey.EXPERIENCE_MODIFIER.toString())) {
			this.experienceModifier = this.config.getConfig().getDouble(ConfigKey.EXPERIENCE_MODIFIER.toString());
			this.inheritedValues.remove(ConfigKey.EXPERIENCE_MODIFIER);
		}
		if (this.config.getConfig().contains(ConfigKey.HEALTH_ADDON.toString())) {
			this.healthAddon = this.config.getConfig().getDouble(ConfigKey.HEALTH_ADDON.toString());
			this.inheritedValues.remove(ConfigKey.HEALTH_ADDON);
		}
		if (this.config.getConfig().contains(ConfigKey.DEFENSE_ADDON.toString())) {
			this.defenseAddon = this.config.getConfig().getDouble(ConfigKey.DEFENSE_ADDON.toString());
			this.inheritedValues.remove(ConfigKey.DEFENSE_ADDON);
		}
		if (this.config.getConfig().contains(ConfigKey.DAMAGE_ADDON.toString())) {
			this.damageAddon = this.config.getConfig().getDouble(ConfigKey.DAMAGE_ADDON.toString());
			this.inheritedValues.remove(ConfigKey.DAMAGE_ADDON);
		}
		if (this.config.getConfig().contains(ConfigKey.EXPERIENCE_ADDON.toString())) {
			this.experienceAddon = this.config.getConfig().getDouble(ConfigKey.EXPERIENCE_ADDON.toString());
			this.inheritedValues.remove(ConfigKey.EXPERIENCE_ADDON);
		}
		if (this.config.getConfig().contains(ConfigKey.USE_SUFFIX.toString())) {
			this.useSuffix = this.config.getConfig().getBoolean(ConfigKey.USE_SUFFIX.toString());
			this.inheritedValues.remove(ConfigKey.USE_SUFFIX);
		}
		if (this.config.getConfig().contains(ConfigKey.USE_PREFIX.toString())) {
			this.usePrefix = this.config.getConfig().getBoolean(ConfigKey.USE_PREFIX.toString());
			this.inheritedValues.remove(ConfigKey.USE_PREFIX);
		}
		if (this.config.getConfig().contains(ConfigKey.SUFFIX_FORMAT.toString())) {
			this.suffixFormat = this.config.getConfig().getString(ConfigKey.SUFFIX_FORMAT.toString());
			this.inheritedValues.remove(ConfigKey.SUFFIX_FORMAT);
		}
		if (this.config.getConfig().contains(ConfigKey.PREFIX_FORMAT.toString())) {
			this.prefixFormat = this.config.getConfig().getString(ConfigKey.PREFIX_FORMAT.toString());
			this.inheritedValues.remove(ConfigKey.PREFIX_FORMAT);
		}
		if (this.config.getConfig().contains(ConfigKey.MOB_ARENA_ENABLED.toString())) {
			this.leveledMobArea = this.config.getConfig().getBoolean(ConfigKey.MOB_ARENA_ENABLED.toString());
			this.inheritedValues.remove(ConfigKey.MOB_ARENA_ENABLED);
		}
		if (this.config.getConfig().contains(ConfigKey.MOB_ARENA_WAVE_LEVELING.toString())) {
			this.mobArenaWaveLeveling = this.config.getConfig()
					.getBoolean(ConfigKey.MOB_ARENA_WAVE_LEVELING.toString());
			this.inheritedValues.remove(ConfigKey.MOB_ARENA_WAVE_LEVELING);
		}
		if (this.config.getConfig().contains(ConfigKey.MOB_ARENA_MULTIPLIER.toString())) {
			this.mobArenaMultiplier = this.config.getConfig().getDouble(ConfigKey.MOB_ARENA_MULTIPLIER.toString());
			this.inheritedValues.remove(ConfigKey.MOB_ARENA_MULTIPLIER);
		}
		if (this.config.getConfig().contains(ConfigKey.LEVELED_SPAWNERS.toString())) {
			this.leveledSpawners = this.config.getConfig().getBoolean(ConfigKey.LEVELED_SPAWNERS.toString());
			this.inheritedValues.remove(ConfigKey.LEVELED_SPAWNERS);
		}
		if (this.config.getConfig().contains(ConfigKey.NAME_LANGUAGE.toString())) {
			this.language = Language.valueOf(this.config.getConfig().getString(ConfigKey.NAME_LANGUAGE.toString()).toUpperCase());
			this.inheritedValues.remove(ConfigKey.NAME_LANGUAGE);
		}
		if (this.config.getConfig().contains(ConfigKey.MONEY_TOWNY_RATIO.toString())) {
			this.TownyRatio = this.config.getConfig().getDouble(ConfigKey.MONEY_TOWNY_RATIO.toString());
			this.inheritedValues.remove(ConfigKey.MONEY_TOWNY_RATIO);
		}
		if (this.config.getConfig().contains(ConfigKey.MONEY_TOWNY_SUBTRACT.toString())) {
			this.TownySubtract = this.config.getConfig().getBoolean(ConfigKey.MONEY_TOWNY_SUBTRACT.toString());
			this.inheritedValues.remove(ConfigKey.MONEY_TOWNY_SUBTRACT);
		}
		if (this.config.getConfig().contains(ConfigKey.MONEY_TOWNY_SUPPORTNATION.toString())) {
			this.TownyNationSupport = this.config.getConfig().getBoolean(ConfigKey.MONEY_TOWNY_SUPPORTNATION.toString());
			this.inheritedValues.remove(ConfigKey.MONEY_TOWNY_SUPPORTNATION);
		}
		
		
		if (this.config.getConfig().contains(ConfigKey.RPG_LEVEL_RANDOMIZER.toString())) {
			this.RPGLevelRandomizer = this.config.getConfig().getBoolean(ConfigKey.RPG_LEVEL_RANDOMIZER.toString());
			this.inheritedValues.remove(ConfigKey.RPG_LEVEL_RANDOMIZER);
		}
		if (this.config.getConfig().contains(ConfigKey.RPG_LEVEL_MAX.toString())) {
			this.RPGLevelMax = this.config.getConfig().getInt(ConfigKey.RPG_LEVEL_MAX.toString());
			this.inheritedValues.remove(ConfigKey.RPG_LEVEL_MAX);
		}
		
	}

	private void loadNodes() {
		this.nodeConfig = new configHandler(Main.RPGMobs, this.world.getName() + File.separator + "SpawnNodes");
		if (!this.nodeConfig.getConfig().contains("SpawnNodes")) {
			this.nodeConfig.getConfig().createSection("SpawnNodes");
		}
		if (this.nodeConfig.getConfig().getConfigurationSection("SpawnNodes").getKeys(false).size() < 1) {
			this.nodeConfig.getConfig().set("SpawnNodes.Spawn1.Location.x", (Object) 0.0);
			this.nodeConfig.getConfig().set("SpawnNodes.Spawn1.Location.y", (Object) 0.0);
			this.nodeConfig.getConfig().set("SpawnNodes.Spawn1.Location.z", (Object) 0.0);
			this.nodeConfig.saveConfig();
		}
		for (final String key : this.nodeConfig.getConfig().getConfigurationSection("SpawnNodes").getKeys(false)) {
			final ConfigurationSection nodeSection = this.nodeConfig.getConfig()
					.getConfigurationSection("SpawnNodes." + key);
			this.nodeList.add(SpawnNode.CreateSpawnNode(nodeSection, this));
		}
	}

	public void saveConfig() {
		this.config.saveConfig();
	}

	public SpawnNode addSpawnNode(final double x, final double y, final double z) {
		final SpawnNode node = SpawnNode.CreateSpawnNode(this, this.getWorld(), x, y, z);
		if (node != null && !this.nodeList.contains(node)) {
			this.nodeList.add(node);
		}
		return node;
	}

	public int createdSpawnNode() {
		return ++this.spawnNodeCount;
	}

	public SpawnNode getClosestSpawnNode(final Location loc) {
		if (this.nodeList.size() == 1) {
			return this.nodeList.get(0);
		}
		double distance = 9.223372036854776E18;
		SpawnNode closest = this.nodeList.get(0);
		for (final SpawnNode node : this.nodeList) {
			final double distanceToSpawnNode = node.getLocation().distanceSquared(loc);
			if (distanceToSpawnNode < distance) {
				closest = node;
				distance = distanceToSpawnNode;
			}
		}
		return closest;
	}

	public String getWorldName() {
		return this.formatWorldName(this.world.getName());
	}

	private String formatWorldName(final String name) {
		String formattedName = "";
		for (final String part : name.split("_")) {
			formattedName = formattedName + part.substring(0, 1).toUpperCase() + part.substring(1) + " ";
		}
		return formattedName.trim();
	}

	public boolean isEnabled() {
		return this.enabled;
	}

	public void setEnabled(final boolean enabled) {
		this.enabled = enabled;
		this.config.getConfig().set("Enabled", (Object) enabled);
		this.saveConfig();
	}

	public World getWorld() {
		return this.world;
	}

	@Override
	public boolean isSuffixEnabled() {
		if (this.inheritedValues.containsKey(ConfigKey.USE_SUFFIX)) {
			return (boolean) this.inheritedValues.get(ConfigKey.USE_SUFFIX);
		}
		return this.useSuffix;
	}

	@Override
	public void setSuffixEnabled(final boolean useSuffix) {
		if (this.inheritedValues.containsKey(ConfigKey.USE_SUFFIX)) {
			this.inheritedValues.remove(ConfigKey.USE_SUFFIX);
		}
		this.useSuffix = useSuffix;
		this.config.getConfig().set(ConfigKey.USE_SUFFIX.toString(), (Object) useSuffix);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean isPrefixEnabled() {
		if (!this.inheritedValues.containsKey(ConfigKey.USE_PREFIX)) {
			return this.usePrefix;
		}
		return (boolean) this.inheritedValues.get(ConfigKey.USE_PREFIX);
	}

	@Override
	public void setPrefixEnabled(final boolean usePrefix) {
		if (this.inheritedValues.containsKey(ConfigKey.USE_PREFIX)) {
			this.inheritedValues.remove(ConfigKey.USE_PREFIX);
		}
		this.usePrefix = usePrefix;
		this.config.getConfig().set(ConfigKey.USE_PREFIX.toString(), (Object) usePrefix);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public String getSuffixFormat() {
		if (!this.inheritedValues.containsKey(ConfigKey.SUFFIX_FORMAT)) {
			return this.suffixFormat;
		}
		return (String) this.inheritedValues.get(ConfigKey.SUFFIX_FORMAT);
	}

	@Override
	public void setSuffixFormat(final String suffixFormat) {
		if (this.inheritedValues.containsKey(ConfigKey.SUFFIX_FORMAT)) {
			this.inheritedValues.remove(ConfigKey.SUFFIX_FORMAT);
		}
		this.suffixFormat = suffixFormat;
		this.config.getConfig().set(ConfigKey.SUFFIX_FORMAT.toString(), (Object) suffixFormat);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public String getPrefixFormat() {
		if (!this.inheritedValues.containsKey(ConfigKey.PREFIX_FORMAT)) {
			return this.prefixFormat;
		}
		return (String) this.inheritedValues.get(ConfigKey.PREFIX_FORMAT);
	}

	@Override
	public void setPrefixFormat(final String prefixFormat) {
		if (this.inheritedValues.containsKey(ConfigKey.PREFIX_FORMAT)) {
			this.inheritedValues.remove(ConfigKey.PREFIX_FORMAT);
		}
		this.prefixFormat = prefixFormat;
		this.config.getConfig().set(ConfigKey.PREFIX_FORMAT.toString(), (Object) prefixFormat);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean isDamageModified() {
		if (!this.inheritedValues.containsKey(ConfigKey.DAMAGE_MOD_ENABLE)) {
			return this.damageModified;
		}
		return (boolean) this.inheritedValues.get(ConfigKey.DAMAGE_MOD_ENABLE);
	}

	@Override
	public void setDamageModified(final boolean enabled) {
		if (this.inheritedValues.containsKey(ConfigKey.DAMAGE_MOD_ENABLE)) {
			this.inheritedValues.remove(ConfigKey.DAMAGE_MOD_ENABLE);
		}
		this.damageModified = enabled;
		this.config.getConfig().set(ConfigKey.DAMAGE_MOD_ENABLE.toString(), (Object) this.damageModified);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public void setDefenseModified(final boolean enabled) {
		if (this.inheritedValues.containsKey(ConfigKey.DEFENSE_MOD_ENABLE)) {
			this.inheritedValues.remove(ConfigKey.DEFENSE_MOD_ENABLE);
		}
		this.defenseModified = enabled;
		this.config.getConfig().set(ConfigKey.DEFENSE_MOD_ENABLE.toString(), (Object) this.defenseModified);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean isDefenseModified() {
		if (!this.inheritedValues.containsKey(ConfigKey.DEFENSE_MOD_ENABLE)) {
			return this.defenseModified;
		}
		return (boolean) this.inheritedValues.get(ConfigKey.DEFENSE_MOD_ENABLE);
	}

	@Override
	public void setMoneyModified(final boolean enabled) {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_MOD_ENABLE)) {
			this.inheritedValues.remove(ConfigKey.MONEY_MOD_ENABLE);
		}
		this.moneyModified = enabled;
		this.config.getConfig().set(ConfigKey.MONEY_MOD_ENABLE.toString(), (Object) this.moneyModified);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean isMoneyModified() {
		if (!this.inheritedValues.containsKey(ConfigKey.MONEY_MOD_ENABLE)) {
			return this.moneyModified;
		}
		return (boolean) this.inheritedValues.get(ConfigKey.MONEY_MOD_ENABLE);
	}

	@Override
	public boolean isHealthModified() {
		if (!this.inheritedValues.containsKey(ConfigKey.HEALTH_MOD_ENABLED)) {
			return this.healthModified;
		}
		return (boolean) this.inheritedValues.get(ConfigKey.HEALTH_MOD_ENABLED);
	}

	@Override
	public void setHealthModified(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.HEALTH_MOD_ENABLED);
		this.healthModified = enabled;
		this.config.getConfig().set(ConfigKey.HEALTH_MOD_ENABLED.toString(), (Object) enabled);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean isMobArenaLeveled() {
		if (!this.inheritedValues.containsKey(ConfigKey.MOB_ARENA_ENABLED)) {
			return this.leveledMobArea;
		}
		return (boolean) this.inheritedValues.get(ConfigKey.MOB_ARENA_ENABLED);
	}

	@Override
	public void setMobArenaLeveled(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.MOB_ARENA_ENABLED);
		this.leveledMobArea = enabled;
		this.config.getConfig().set(ConfigKey.MOB_ARENA_ENABLED.toString(), (Object) enabled);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean isMobArenaWaveLeveled() {
		if (!this.inheritedValues.containsKey(ConfigKey.MOB_ARENA_WAVE_LEVELING)) {
			return this.mobArenaWaveLeveling;
		}
		return (boolean) this.inheritedValues.get(ConfigKey.MOB_ARENA_WAVE_LEVELING);
	}

	@Override
	public void setMobArenaWaveLeveled(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.MOB_ARENA_WAVE_LEVELING);
		this.mobArenaWaveLeveling = enabled;
		this.config.getConfig().set(ConfigKey.MOB_ARENA_WAVE_LEVELING.toString(), (Object) enabled);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public double getDistancePerLevel() {
		if (!this.inheritedValues.containsKey(ConfigKey.DISTANCE_PER_LEVEL)) {
			return this.distancePerLevel;
		}
		final Object dpl = this.inheritedValues.get(ConfigKey.DISTANCE_PER_LEVEL);
		if (dpl instanceof Double) {
			return (double) dpl;
		}
		return (int) dpl;
	}

	@Override
	public void setDistancePerLevel(final double distance) {
		this.inheritedValues.remove(ConfigKey.DISTANCE_PER_LEVEL);
		this.distancePerLevel = distance;
		this.config.getConfig().set(ConfigKey.DISTANCE_PER_LEVEL.toString(), (Object) distance);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public int getStartLevel() {
		if (!this.inheritedValues.containsKey(ConfigKey.START_LEVEL)) {
			return this.startLevel;
		}
		return (int) this.inheritedValues.get(ConfigKey.START_LEVEL);
	}

	@Override
	public void setStartLevel(final int startLevel) {
		this.inheritedValues.remove(ConfigKey.START_LEVEL);
		this.startLevel = startLevel;
		this.config.getConfig().set(ConfigKey.START_LEVEL.toString(), (Object) startLevel);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public int getMaxLevel() {
		if (!this.inheritedValues.containsKey(ConfigKey.MAX_LEVEL)) {
			return this.maxLevel;
		}
		return (int) this.inheritedValues.get(ConfigKey.MAX_LEVEL);
	}

	@Override
	public void setMaxLevel(final int maxLevel) {
		this.inheritedValues.remove(ConfigKey.MAX_LEVEL);
		this.maxLevel = maxLevel;
		this.config.getConfig().set(ConfigKey.MAX_LEVEL.toString(), (Object) maxLevel);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public int getMinLevel() {
		if (!this.inheritedValues.containsKey(ConfigKey.MIN_LEVEL)) {
			return this.minLevel;
		}
		return (int) this.inheritedValues.get(ConfigKey.MIN_LEVEL);
	}

	@Override
	public void setMinLevel(final int minLevel) {
		this.inheritedValues.remove(ConfigKey.MIN_LEVEL);
		this.minLevel = minLevel;
		this.config.getConfig().set(ConfigKey.MIN_LEVEL.toString(), (Object) minLevel);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean isExperienceModified() {
		if (!this.inheritedValues.containsKey(ConfigKey.EXPERIENCE_MOD_ENABLED)) {
			return this.experienceModified;
		}
		return (boolean) this.inheritedValues.get(ConfigKey.EXPERIENCE_MOD_ENABLED);
	}

	@Override
	public void setExperienceModified(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.EXPERIENCE_MOD_ENABLED);
		this.experienceModified = enabled;
		this.config.getConfig().set(ConfigKey.EXPERIENCE_MOD_ENABLED.toString(), (Object) enabled);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public double getExperienceMultiplier() {
		if (!this.inheritedValues.containsKey(ConfigKey.EXPERIENCE_MODIFIER)) {
			return this.experienceModifier;
		}
		return (double) this.inheritedValues.get(ConfigKey.EXPERIENCE_MODIFIER);
	}

	@Override
	public void setExperienceMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.EXPERIENCE_MODIFIER);
		this.experienceModifier = multiplier;
		this.config.getConfig().set(ConfigKey.EXPERIENCE_MODIFIER.toString(), (Object) multiplier);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public double getHealthMultiplier() {
		if (!this.inheritedValues.containsKey(ConfigKey.HEALTH_PER_LEVEL)) {
			return this.healthMultiplier;
		}
		return (double) this.inheritedValues.get(ConfigKey.HEALTH_PER_LEVEL);
	}

	@Override
	public void setHealthMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.HEALTH_PER_LEVEL);
		this.healthMultiplier = multiplier;
		this.config.getConfig().set(ConfigKey.HEALTH_PER_LEVEL.toString(), (Object) multiplier);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public double getDamageMultiplier() {
		if (!this.inheritedValues.containsKey(ConfigKey.DAMAGE_PER_LEVEL)) {
			return this.damageMultiplier;
		}
		return (double) this.inheritedValues.get(ConfigKey.DAMAGE_PER_LEVEL);
	}

	@Override
	public void setDamageMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.DAMAGE_PER_LEVEL);
		this.damageMultiplier = multiplier;
		this.config.getConfig().set(ConfigKey.DAMAGE_PER_LEVEL.toString(), (Object) multiplier);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public double getDefenseMultiplier() {
		if (!this.inheritedValues.containsKey(ConfigKey.DEFENSE_PER_LEVEL)) {
			return this.defenseMultiplier;
		}
		return (double) this.inheritedValues.get(ConfigKey.DEFENSE_PER_LEVEL);
	}

	@Override
	public void setDefenseMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.DEFENSE_PER_LEVEL);
		this.defenseMultiplier = multiplier;
		this.config.getConfig().set(ConfigKey.DEFENSE_PER_LEVEL.toString(), (Object) multiplier);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public double getMoneyMultiplier() {
		if (!this.inheritedValues.containsKey(ConfigKey.MONEY_PER_LEVEL)) {
			return this.moneyMultiplier;
		}
		return (double) this.inheritedValues.get(ConfigKey.MONEY_PER_LEVEL);
	}

	@Override
	public void setMoneyMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.MONEY_PER_LEVEL);
		this.moneyMultiplier = multiplier;
		this.config.getConfig().set(ConfigKey.MONEY_PER_LEVEL.toString(), (Object) multiplier);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public double getMoneyRandomizer() {
		if (!this.inheritedValues.containsKey(ConfigKey.MONEY_RANDOM)) {
			return this.moneyRandomizer;
		}
		return (double) this.inheritedValues.get(ConfigKey.MONEY_RANDOM);
	}

	@Override
	public void setMoneyRandomizer(final double randomizer) {
		this.inheritedValues.remove(ConfigKey.MONEY_RANDOM);
		this.moneyRandomizer = randomizer;
		this.config.getConfig().set(ConfigKey.MONEY_RANDOM.toString(), (Object) randomizer);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public double getMobArenaMultiplier() {
		if (!this.inheritedValues.containsKey(ConfigKey.MOB_ARENA_MULTIPLIER)) {
			return this.mobArenaMultiplier;
		}
		return (double) this.inheritedValues.get(ConfigKey.MOB_ARENA_MULTIPLIER);
	}

	@Override
	public void setMobArenaMultiplier(final double mult) {
		this.inheritedValues.remove(ConfigKey.MOB_ARENA_MULTIPLIER);
		this.mobArenaMultiplier = mult;
		this.config.getConfig().set(ConfigKey.MOB_ARENA_MULTIPLIER.toString(), (Object) mult);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public double getMobArenaWavesPerLevel() {
		if (!this.inheritedValues.containsKey(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL)) {
			return this.wavesPerLevel;
		}
		return (double) this.inheritedValues.get(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL);
	}

	@Override
	public void setMobArenaWavesPerLevel(final double wavesPerLevel) {
		this.inheritedValues.remove(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL);
		this.wavesPerLevel = wavesPerLevel;
		this.config.getConfig().set(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL.toString(), (Object) wavesPerLevel);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public void setLeveledMobs(final ArrayList<EntityType> allowed) {
		this.inheritedValues.remove(ConfigKey.LEVELED_MOBS);
		this.leveledMobs = allowed;
		final ArrayList<String> temp = new ArrayList<String>();
		for (final EntityType type : allowed) {
			temp.add(type.toString());
		}
		this.config.getConfig().set(ConfigKey.LEVELED_MOBS.toString(), (Object) temp);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean canLevel(final EntityType ent) {
		if (!this.inheritedValues.containsKey(ConfigKey.LEVELED_MOBS)) {
			return this.leveledMobs.contains(ent);
		}
		return ((ArrayList<EntityType>) this.inheritedValues.get(ConfigKey.LEVELED_MOBS)).contains(ent);
	}

	@Override
	public void addLeveledMob(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.LEVELED_MOBS)) {
			this.leveledMobs = (ArrayList<EntityType>) this.inheritedValues.remove(ConfigKey.LEVELED_MOBS);
		}
		if (!this.leveledMobs.contains(ent)) {
			this.leveledMobs.add(ent);
			this.config.getConfig().set(ConfigKey.LEVELED_MOBS.toString(),
					(Object) this.entListToStringList(this.leveledMobs));
			this.config.saveConfig();
		}
		this.updateChildrenValues();
	}

	@Override
	public void setBlockedMobs(final ArrayList<EntityType> blocked) {
		this.inheritedValues.remove(ConfigKey.BLOCKED_MOBS);
		this.blockedMobs = (ArrayList<EntityType>) blocked.clone();
		final ArrayList<String> temp = new ArrayList<String>();
		for (final EntityType type : this.blockedMobs) {
			temp.add(type.toString());
		}
		this.config.getConfig().set(ConfigKey.BLOCKED_MOBS.toString(), (Object) temp);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean isBlocked(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.BLOCKED_MOBS)) {
			return ((ArrayList<EntityType>) this.inheritedValues.get(ConfigKey.BLOCKED_MOBS)).contains(ent);
		}
		return this.blockedMobs.contains(ent);
	}

	@Override
	public double getMoneyMob(EntityType ent) {
		if (this.getMoneyMobs().get(ent) == null)
			this.moneyMobs.put(ent, 0.0);
		return this.getMoneyMobs().get(ent);
	}

	@Override
	public HashMap<EntityType, Double> getMoneyMobs() {
		if (this.moneyMobs == null) {
			this.moneyMobs = new HashMap<EntityType, Double>();
		}
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_MOBS)) {
			try {

				final HashMap<String, Object> temp = new HashMap<String, Object>();
				final HashMap<EntityType, Double> hashDoubles = new HashMap<EntityType, Double>();
				Object var = this.inheritedValues.get(ConfigKey.MONEY_MOBS);
				MemorySection MS = (MemorySection) var;
				temp.putAll(MS.getValues(false));

				for (final Map.Entry<String, Object> entry : temp.entrySet()) {
					hashDoubles.put(EntityType.valueOf(entry.getKey()),
							Double.parseDouble(entry.getValue().toString()));
				}
				return hashDoubles;
			} catch (Exception e) {
				try {
					final HashMap<String, Object> temp = new HashMap<String, Object>();
					final HashMap<EntityType, Double> hashDoubles = new HashMap<EntityType, Double>();
					Object var = this.inheritedValues.get(ConfigKey.MONEY_MOBS);
					temp.putAll((HashMap<String, Object>) var);
					for (final Map.Entry<String, Object> entry : temp.entrySet()) {
						hashDoubles.put(EntityType.valueOf(entry.getKey()),
								Double.parseDouble(entry.getValue().toString()));
					}
					return hashDoubles;
				} catch (NullPointerException e2) {
				}

			}
		}
		return this.moneyMobs;
	}

	@Override
	public void addBlockedMob(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.BLOCKED_MOBS)) {
			this.blockedMobs = (ArrayList<EntityType>) this.inheritedValues.remove(ConfigKey.BLOCKED_MOBS);
		}
		if (!this.blockedMobs.contains(ent)) {
			this.blockedMobs.add(ent);
			this.config.getConfig().set(ConfigKey.BLOCKED_MOBS.toString(),
					(Object) this.entListToStringList(this.blockedMobs));
			this.config.saveConfig();
			this.updateChildrenValues();
		}
	}

	@Override
	public void removeBlockedMob(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.BLOCKED_MOBS)) {
			this.blockedMobs = (ArrayList<EntityType>) this.inheritedValues.remove(ConfigKey.BLOCKED_MOBS);
		}
		this.blockedMobs.remove(ent);
		this.config.getConfig().set(ConfigKey.BLOCKED_MOBS.toString(),
				(Object) this.entListToStringList(this.blockedMobs));
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public void removeLeveledMob(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.LEVELED_MOBS)) {
			this.leveledMobs = (ArrayList<EntityType>) this.inheritedValues.remove(ConfigKey.LEVELED_MOBS);
		}
		this.leveledMobs.remove(ent);
		this.config.getConfig().set(ConfigKey.LEVELED_MOBS.toString(),
				(Object) this.entListToStringList(this.leveledMobs));
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public void setMoneyMobs(final HashMap<EntityType, Double> moneyMob) {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_MOBS)) {
			this.inheritedValues.remove(ConfigKey.MONEY_MOBS);
		}
		final HashMap<EntityType, Double> temp = new HashMap<EntityType, Double>();
		this.moneyMobs = (HashMap<EntityType, Double>) moneyMob.clone();
		for (Map.Entry<EntityType, Double> type : this.moneyMobs.entrySet()) {
			temp.put(type.getKey(), type.getValue());
		}
		this.config.getConfig().set(ConfigKey.MONEY_MOBS.toString(), (Object) this.MoneyHashMapToStringList(temp));
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public void addMoneyMob(final EntityType ent, final double amount) {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_MOBS)) {
			this.inheritedValues.remove(ConfigKey.MONEY_MOBS);
		}
		this.moneyMobs.put(ent, amount);
		this.config.getConfig().set(ConfigKey.MONEY_MOBS.toString(),
				(Object) this.MoneyHashMapToStringList(this.moneyMobs));
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public void removeMoneyMob(final EntityType ent) {
		this.moneyMobs.put(ent, 0.0);
		this.config.getConfig().set(ConfigKey.MONEY_MOBS.toString(),
				(Object) this.MoneyHashMapToStringList(this.moneyMobs));
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public void useInheritedValue(final ConfigKey key) {
		this.inheritedValues.put(key, this.global.getValue(key));
		this.config.getConfig().set(key.toString(), (Object) null);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	public void updateInheritedValues() {
		for (final ConfigKey key : this.inheritedValues.keySet()) {
			this.inheritedValues.put(key, this.global.getValue(key));
		}
	}

	public void updateChildrenValues() {
		for (final SpawnNode node : this.nodeList) {
			node.updateInheritedValues();
		}
	}

	@Override
	public ArrayList<EntityType> getLeveledMobs() {
		if (this.inheritedValues.containsKey(ConfigKey.LEVELED_MOBS)) {
			return (ArrayList<EntityType>) this.inheritedValues.get(ConfigKey.LEVELED_MOBS);
		}
		return this.leveledMobs;
	}

	@Override
	public ArrayList<EntityType> getBlockedMobs() {
		if (this.inheritedValues.containsKey(ConfigKey.BLOCKED_MOBS)) {
			return (ArrayList<EntityType>) this.inheritedValues.get(ConfigKey.BLOCKED_MOBS);
		}
		return this.blockedMobs;
	}

	@Override
	public boolean isLeveledSpawners() {
		if (this.inheritedValues.containsKey(ConfigKey.LEVELED_SPAWNERS)) {
			return (boolean) this.inheritedValues.get(ConfigKey.LEVELED_SPAWNERS);
		}
		return this.leveledSpawners;
	}

	@Override
	public void setLeveledSpawners(final boolean leveledSpawners) {
		this.inheritedValues.remove(ConfigKey.LEVELED_SPAWNERS);
		this.leveledSpawners = leveledSpawners;
		this.config.getConfig().set(ConfigKey.LEVELED_SPAWNERS.toString(), (Object) leveledSpawners);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public boolean isAlwaysShowMobName() {
		if (this.inheritedValues.containsKey(ConfigKey.ALWAYS_SHOW_MOB_NAME)) {
			return (boolean) this.inheritedValues.get(ConfigKey.ALWAYS_SHOW_MOB_NAME);
		}
		return this.alwaysShowMobName;
	}

	@Override
	public void setAlwaysShowMobName(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.ALWAYS_SHOW_MOB_NAME);
		this.alwaysShowMobName = enabled;
		this.config.getConfig().set(ConfigKey.ALWAYS_SHOW_MOB_NAME.toString(), (Object) this.alwaysShowMobName);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public void setMobNameLanguage(final Language lang) {
		this.inheritedValues.remove(ConfigKey.NAME_LANGUAGE);
		this.language = lang;
		this.config.getConfig().set(ConfigKey.NAME_LANGUAGE.toString(), (Object) lang.toString());
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public Language getMobNameLanguage() {
		if (this.inheritedValues.containsKey(ConfigKey.NAME_LANGUAGE)) {
			return (Language) this.inheritedValues.get(ConfigKey.NAME_LANGUAGE);
		}
		return this.language;
	}

	@Override
	public void setTownyRatio(final double ratio) {
		this.inheritedValues.remove(ConfigKey.MONEY_TOWNY_RATIO);
		this.TownyRatio = ratio;
		this.config.getConfig().set(ConfigKey.MONEY_TOWNY_RATIO.toString(), (Object) this.TownyRatio);
		this.config.saveConfig();
		this.updateChildrenValues();
	}
	@Override
	public double getTownyRatio() {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_TOWNY_RATIO)) {
			return (double) this.inheritedValues.get(ConfigKey.MONEY_TOWNY_RATIO);
		}
		return this.TownyRatio;
	}
	@Override
	public void setisTownySubtract(final boolean isSubtract) {
		this.inheritedValues.remove(ConfigKey.MONEY_TOWNY_SUBTRACT);
		this.TownySubtract = isSubtract;
		this.config.getConfig().set(ConfigKey.MONEY_TOWNY_SUBTRACT.toString(), (Object) this.TownySubtract);
		this.config.saveConfig();
		Main.RPGMobs.getConfigModule().globalUpdate();
		this.updateChildrenValues();
	}
	@Override
	public boolean getisTownySubtract() {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_TOWNY_SUBTRACT)) {
			return (boolean) this.inheritedValues.get(ConfigKey.MONEY_TOWNY_SUBTRACT);
		}
		return this.TownySubtract;
	}
	@Override
	public void setisTownyNationSupport(final boolean isNationSupport) {
		this.inheritedValues.remove(ConfigKey.MONEY_TOWNY_SUPPORTNATION);
		this.TownyNationSupport = isNationSupport;
		this.config.getConfig().set(ConfigKey.MONEY_TOWNY_SUPPORTNATION.toString(), (Object) this.TownyNationSupport);
		this.config.saveConfig();
		Main.RPGMobs.getConfigModule().globalUpdate();
		this.updateChildrenValues();
	}
	@Override
	public boolean getisTownyNationSupport() {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_TOWNY_SUPPORTNATION)) {
			return (boolean) this.inheritedValues.get(ConfigKey.MONEY_TOWNY_SUPPORTNATION);
		}
		return this.TownyNationSupport;
	}
	@Override
	public boolean isNoMoneyDrop() {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_TAKE_MONEY_ON_KILL)) {
			return (boolean) this.inheritedValues.get(ConfigKey.MONEY_TAKE_MONEY_ON_KILL);
		}
		return this.NoMoneyDrop;
	}
	@Override
	public void setNoMoneyDrop(boolean noMoneyDrop) {
		this.inheritedValues.remove(ConfigKey.MONEY_TAKE_MONEY_ON_KILL);
		this.NoMoneyDrop = noMoneyDrop;
		this.config.getConfig().set(ConfigKey.MONEY_TAKE_MONEY_ON_KILL.toString(), (Object) noMoneyDrop);
		this.config.saveConfig();
		Main.RPGMobs.getConfigModule().globalUpdate();
		this.updateChildrenValues();
	}
	
	@Override
	public double getHealthAddon() {
		if (this.inheritedValues.containsKey(ConfigKey.HEALTH_ADDON)) {
			return (double) this.inheritedValues.get(ConfigKey.HEALTH_ADDON);
		}
		return this.healthAddon;
	}
	@Override
	public void setHealthAddon(double healthAdd) {
		this.inheritedValues.remove(ConfigKey.HEALTH_ADDON);
		this.healthAddon = healthAdd;
		this.config.getConfig().set(ConfigKey.HEALTH_ADDON.toString(), (Object) healthAdd);
		this.config.saveConfig();
		this.updateChildrenValues();
	}
	@Override
	public double getDefenseAddon() {
		if (this.inheritedValues.containsKey(ConfigKey.DEFENSE_ADDON)) {
			return (double) this.inheritedValues.get(ConfigKey.DEFENSE_ADDON);
		}
		return this.defenseAddon;
	}
	@Override
	public void setDefenseAddon(double defenseAdd) {
		this.inheritedValues.remove(ConfigKey.DEFENSE_ADDON);
		this.defenseAddon = defenseAdd;
		this.config.getConfig().set(ConfigKey.DEFENSE_ADDON.toString(), (Object) defenseAdd);
		this.config.saveConfig();
		this.updateChildrenValues();
	}
	@Override
	public double getDamageAddon() {
		if (this.inheritedValues.containsKey(ConfigKey.DAMAGE_ADDON)) {
			return (double) this.inheritedValues.get(ConfigKey.DAMAGE_ADDON);
		}
		return this.damageAddon;
	}
	@Override
	public void setDamageAddon(double damageAdd) {
		this.inheritedValues.remove(ConfigKey.DAMAGE_ADDON);
		this.damageAddon = damageAdd;
		this.config.getConfig().set(ConfigKey.DAMAGE_ADDON.toString(), (Object) damageAdd);
		this.config.saveConfig();
		this.updateChildrenValues();
	}
	@Override
	public double getExperienceAddon() {
		if (this.inheritedValues.containsKey(ConfigKey.EXPERIENCE_ADDON)) {
			return (double) this.inheritedValues.get(ConfigKey.EXPERIENCE_ADDON);
		}
		return this.experienceAddon;
	}
	@Override
	public void setExperienceAddon(double experienceAdd) {
		this.inheritedValues.remove(ConfigKey.EXPERIENCE_ADDON);
		this.experienceAddon = experienceAdd;
		this.config.getConfig().set(ConfigKey.EXPERIENCE_ADDON.toString(), (Object) experienceAdd);
		this.config.saveConfig();
		this.updateChildrenValues();
	}
	
	@Override
	public boolean isRPGLevelRandomizer() {
		if (this.inheritedValues.containsKey(ConfigKey.RPG_LEVEL_RANDOMIZER)) {
			return (boolean) this.inheritedValues.get(ConfigKey.RPG_LEVEL_RANDOMIZER);
		}
		return this.RPGLevelRandomizer;
	}
	
	@Override
	public void setRPGLevelRandomizer(boolean RPGLevelRandomizer) {
		this.inheritedValues.remove(ConfigKey.RPG_LEVEL_RANDOMIZER);
		this.RPGLevelRandomizer = RPGLevelRandomizer;
		this.config.getConfig().set(ConfigKey.RPG_LEVEL_RANDOMIZER.toString(), (Object) RPGLevelRandomizer);
		this.config.saveConfig();
		this.updateChildrenValues();
	}

	@Override
	public int getRPGLevelMax() {
		if (this.inheritedValues.containsKey(ConfigKey.RPG_LEVEL_MAX)) {
			return (int) this.inheritedValues.get(ConfigKey.RPG_LEVEL_MAX);
		}
		return this.RPGLevelMax;
	}
	@Override
	public void setRPGLevelMax(int RPGLevelMax) {
		this.inheritedValues.remove(ConfigKey.RPG_LEVEL_MAX);
		this.RPGLevelMax = RPGLevelMax;
		this.config.getConfig().set(ConfigKey.RPG_LEVEL_MAX.toString(), (Object) RPGLevelMax);
		this.config.saveConfig();
		this.updateChildrenValues();
	}
	
	public ArrayList<SpawnNode> getNodes() {
		return this.nodeList;
	}

	public ConfigurationSection getNodeConfig(final double x, final double y, final double z) {
		for (final String key : this.nodeConfig.getConfig().getConfigurationSection("SpawnNodes").getKeys(false)) {
			if (this.nodeConfig.getConfig().getDouble("SpawnNodes." + key + "Location.x", 0.0) == x
					&& this.nodeConfig.getConfig().getDouble("SpawnNodes." + key + "Location.y", 0.0) == y
					&& this.nodeConfig.getConfig().getDouble("SpawnNodes." + key + "Location.z", 0.0) == z) {
				return this.nodeConfig.getConfig().getConfigurationSection("SpawnNodes").getConfigurationSection(key);
			}
		}
		final ConfigurationSection sec = this.nodeConfig.getConfig().createSection("SpawnNodes.SpawnPoint"
				+ (this.nodeConfig.getConfig().getConfigurationSection("SpawnNodes").getKeys(false).size() + 1));
		sec.set("Location.x", (Object) x);
		sec.set("Location.y", (Object) y);
		sec.set("Location.z", (Object) z);
		this.nodeConfig.saveConfig();
		return sec;
	}

	public void saveNodeConfig() {
		this.nodeConfig.saveConfig();
	}

	public void removeNode(final SpawnNode node) {
		final boolean success = this.nodeList.remove(node);
		if (this.nodeList.isEmpty()) {
			this.nodeList.add(
					SpawnNode.CreateSpawnNode(node.getWorldConfig(), node.getLocation().getWorld(), 0.0, 0.0, 0.0));
		}
		if (success) {
			this.nodeConfig.getConfig().set("SpawnNodes." + node.getName(), (Object) null);
			this.nodeConfig.saveConfig();
		}
	}



}
