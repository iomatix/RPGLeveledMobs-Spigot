package iomatix.spigot.rpgleveledmobs.config;

import java.util.Iterator;
import java.util.Map;

import org.bukkit.World;
import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.EntityType;
import org.bukkit.Location;

import com.garbagemule.MobArena.framework.Arena;
import iomatix.spigot.rpgleveledmobs.tools.Language;
import iomatix.spigot.rpgleveledmobs.Main;

public class SpawnNode extends RPGLeveledMobsConfig {

	private Location loc;
	private ArrayList<EntityType> leveledMobs;
	private ArrayList<EntityType> blockedMobs;
	private boolean leveledSpawners;
	private static final HashMap<WorldConfig, ArrayList<SpawnNode>> spawnNodes;
	private final ConfigurationSection nodeConfig;
	private final WorldConfig worldConfig;
	private boolean suffixEnabled;
	private String suffixFormat;
	private boolean prefixEnabled;
	private String prefixFormat;
	private Language language;
	private double distancePerLevel;
	private int maxLevel;
	private int minLevel;
	private int startLevel;
	private boolean damageModified;
	private double damageMult;
	private boolean defenseModified;
	private double defenseMult;
	private boolean moneyModified;
	private double moneyMult;
	private double moneyRandomizer;
	private boolean expModified;
	private double expMult;
	private boolean healthModified;
	private double healthMult;
	private boolean mobArenaLevelingEnabled;
	private boolean mobArenaWaveLevelingEnabled;
	private double mobArenaMultiplier;
	private double mobArenaWavesPerLevel;
	private boolean alwaysShowMobName;
	private final Random rand;
	private HashMap<EntityType, Double> moneyMobs;

	private SpawnNode(final ConfigurationSection nodeSection, final WorldConfig worldConfig) {
		this.rand = new Random();
		this.worldConfig = worldConfig;
		this.nodeConfig = nodeSection;
		this.loc = new Location(worldConfig.getWorld(), this.nodeConfig.getDouble("Location.x", 0.0),
				this.nodeConfig.getDouble("Location.y", 0.0), this.nodeConfig.getDouble("Location.z", 0.0));
		worldConfig.saveNodeConfig();
		this.inheritedValues = worldConfig.getSettings();
	}

	public static SpawnNode CreateSpawnNode(final WorldConfig wc, final World world, final double x, final double y,
			final double z) {
		for (final SpawnNode tempNode : SpawnNode.spawnNodes.get(wc)) {
			if (tempNode.getLocation().getX() == x && tempNode.getLocation().getY() == y
					&& tempNode.getLocation().getZ() == z) {
				return tempNode;
			}
		}
		final SpawnNode node = new SpawnNode(wc.getNodeConfig(x, y, z), wc);
		SpawnNode.spawnNodes.get(wc).add(node);
		return node;
	}

	public static SpawnNode CreateSpawnNode(final ConfigurationSection section, final WorldConfig config) {
		for (final SpawnNode tempNode : SpawnNode.spawnNodes.get(config)) {
			if (tempNode.getLocation().getX() == section.getDouble("Location.x")
					&& tempNode.getLocation().getY() == section.getDouble("Location.y")
					&& tempNode.getLocation().getZ() == section.getDouble("Location.z")) {
				return tempNode;
			}
		}
		final SpawnNode node = new SpawnNode(section, config);
		SpawnNode.spawnNodes.get(config).add(node);
		return node;
	}

	public static SpawnNode getSpawnNode(final World world, final double x, final double y, final double z) {
		for (final SpawnNode node : SpawnNode.spawnNodes.get(world)) {
			if (node.getLocation().getX() == x && node.getLocation().getZ() == y && node.getLocation().getZ() == z) {
				return node;
			}
		}
		return null;
	}

	public static SpawnNode getSpawnNode(final Location loc) {
		return getSpawnNode(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
	}

	public static void registerWorld(final WorldConfig world) {
		SpawnNode.spawnNodes.put(world, new ArrayList<SpawnNode>());
	}

	public void saveConfig() {
		this.worldConfig.saveNodeConfig();
	}

	public Location getLocation() {
		return this.loc;
	}

	public String getName() {
		return this.nodeConfig.getCurrentPath().substring(this.nodeConfig.getCurrentPath().lastIndexOf(46) + 1);
	}

	public int getLevel(final Location loc) {
		final double distance = loc.distance(this.loc);
		int wave = 0;
		if (Main.getMobArena() != null && this.isMobArenaWaveLeveled()) {
			final Arena arena = Main.getMobArena().getArenaMaster().getArenaAtLocation(loc);
			if (arena != null) {
				wave = arena.getWaveManager().getWaveNumber();
			}
		}
		final int level = (int) Math.floor(distance / this.getDistancePerLevel()) + this.getStartLevel() + wave;
		if (level < this.getMinLevel()) {
			return this.getMinLevel();
		}
		if (level > this.getMaxLevel()) {
			return this.getMaxLevel();
		}
		return level;
	}

	public boolean isArenaLocation(final Location loc) {
		if (Main.getMobArena() == null) {
			return false;
		}
		final Arena arena = Main.getMobArena().getArenaMaster().getArenaAtLocation(loc);
		return arena != null;
	}

	@Override
	public boolean isSuffixEnabled() {
		if (this.inheritedValues.containsKey(ConfigKey.USE_SUFFIX)) {
			return (boolean) this.inheritedValues.get(ConfigKey.USE_SUFFIX);
		}
		return this.suffixEnabled;
	}

	@Override
	public void setSuffixEnabled(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.USE_SUFFIX);
		this.suffixEnabled = enabled;
		this.nodeConfig.set(ConfigKey.USE_SUFFIX.toString(), (Object) enabled);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public boolean isPrefixEnabled() {
		if (this.inheritedValues.containsKey(ConfigKey.USE_PREFIX)) {
			return (boolean) this.inheritedValues.get(ConfigKey.USE_PREFIX);
		}
		return this.prefixEnabled;
	}

	@Override
	public void setPrefixEnabled(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.USE_PREFIX);
		this.prefixEnabled = enabled;
		this.nodeConfig.set(ConfigKey.USE_PREFIX.toString(), (Object) enabled);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public String getSuffixFormat() {
		if (this.inheritedValues.containsKey(ConfigKey.SUFFIX_FORMAT)) {
			return (String) this.inheritedValues.get(ConfigKey.SUFFIX_FORMAT);
		}
		return this.suffixFormat;
	}

	@Override
	public void setSuffixFormat(final String format) {
		this.inheritedValues.remove(ConfigKey.SUFFIX_FORMAT);
		this.suffixFormat = format;
		this.nodeConfig.set(ConfigKey.SUFFIX_FORMAT.toString(), (Object) format);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public String getPrefixFormat() {
		if (this.inheritedValues.containsKey(ConfigKey.PREFIX_FORMAT)) {
			return (String) this.inheritedValues.get(ConfigKey.PREFIX_FORMAT);
		}
		return this.prefixFormat;
	}

	@Override
	public void setPrefixFormat(final String format) {
		this.inheritedValues.remove(ConfigKey.PREFIX_FORMAT);
		this.prefixFormat = format;
		this.nodeConfig.set(ConfigKey.PREFIX_FORMAT.toString(), (Object) this.prefixFormat);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public double getDistancePerLevel() {
		if (this.inheritedValues.containsKey(ConfigKey.DISTANCE_PER_LEVEL)) {
			return (double) this.inheritedValues.get(ConfigKey.DISTANCE_PER_LEVEL);
		}
		return this.distancePerLevel;
	}

	@Override
	public void setDistancePerLevel(final double distance) {
		this.inheritedValues.remove(ConfigKey.DISTANCE_PER_LEVEL);
		this.distancePerLevel = distance;
		this.nodeConfig.set(ConfigKey.DISTANCE_PER_LEVEL.toString(), (Object) this.distancePerLevel);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public int getMaxLevel() {
		if (this.inheritedValues.containsKey(ConfigKey.MAX_LEVEL)) {
			return (int) this.inheritedValues.get(ConfigKey.MAX_LEVEL);
		}
		return this.maxLevel;
	}

	@Override
	public void setMaxLevel(final int maxLevel) {
		this.inheritedValues.remove(ConfigKey.MAX_LEVEL);
		this.maxLevel = maxLevel;
		this.nodeConfig.set(ConfigKey.MAX_LEVEL.toString(), (Object) maxLevel);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public int getMinLevel() {
		if (this.inheritedValues.containsKey(ConfigKey.MIN_LEVEL)) {
			return (int) this.inheritedValues.get(ConfigKey.MIN_LEVEL);
		}
		return this.minLevel;
	}

	@Override
	public void setMinLevel(final int minLevel) {
		this.inheritedValues.remove(ConfigKey.MIN_LEVEL);
		this.minLevel = minLevel;
		this.nodeConfig.set(ConfigKey.MIN_LEVEL.toString(), (Object) minLevel);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public int getStartLevel() {
		if (this.inheritedValues.containsKey(ConfigKey.START_LEVEL)) {
			return (int) this.inheritedValues.get(ConfigKey.START_LEVEL);
		}
		return this.startLevel;
	}

	@Override
	public void setStartLevel(final int startLevel) {
		this.inheritedValues.remove(ConfigKey.START_LEVEL);
		this.startLevel = startLevel;
		this.nodeConfig.set(ConfigKey.START_LEVEL.toString(), (Object) startLevel);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public boolean isDamageModified() {
		if (this.inheritedValues.containsKey(ConfigKey.DAMAGE_MOD_ENABLE)) {
			return (boolean) this.inheritedValues.get(ConfigKey.DAMAGE_MOD_ENABLE);
		}
		return this.damageModified;
	}

	@Override
	public void setDamageModified(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.DAMAGE_MOD_ENABLE);
		this.damageModified = enabled;
		this.nodeConfig.set(ConfigKey.DAMAGE_MOD_ENABLE.toString(), (Object) this.damageModified);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public boolean isDefenseModified() {
		if (this.inheritedValues.containsKey(ConfigKey.DEFENSE_MOD_ENABLE)) {
			return (boolean) this.inheritedValues.get(ConfigKey.DEFENSE_MOD_ENABLE);
		}
		return this.defenseModified;
	}

	@Override
	public void setDefenseModified(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.DEFENSE_MOD_ENABLE);
		this.defenseModified = enabled;
		this.nodeConfig.set(ConfigKey.DEFENSE_MOD_ENABLE.toString(), (Object) this.defenseModified);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public boolean isMoneyModified() {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_MOD_ENABLE)) {
			return (boolean) this.inheritedValues.get(ConfigKey.MONEY_MOD_ENABLE);
		}
		return this.moneyModified;
	}

	@Override
	public void setMoneyModified(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.MONEY_MOD_ENABLE);
		this.moneyModified = enabled;
		this.nodeConfig.set(ConfigKey.MONEY_MOD_ENABLE.toString(), (Object) this.moneyModified);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public boolean isExperienceModified() {
		if (this.inheritedValues.containsKey(ConfigKey.EXPERIENCE_MOD_ENABLED)) {
			return (boolean) this.inheritedValues.get(ConfigKey.EXPERIENCE_MOD_ENABLED);
		}
		return this.expModified;
	}

	@Override
	public void setExperienceModified(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.EXPERIENCE_MOD_ENABLED);
		this.expModified = enabled;
		this.nodeConfig.set(ConfigKey.EXPERIENCE_MOD_ENABLED.toString(), (Object) this.expModified);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public boolean isHealthModified() {
		if (this.inheritedValues.containsKey(ConfigKey.HEALTH_MOD_ENABLED)) {
			return (boolean) this.inheritedValues.get(ConfigKey.HEALTH_MOD_ENABLED);
		}
		return this.healthModified;
	}

	@Override
	public void setHealthModified(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.HEALTH_MOD_ENABLED);
		this.healthModified = enabled;
		this.nodeConfig.set(ConfigKey.HEALTH_MOD_ENABLED.toString(), (Object) this.healthModified);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public double getHealthMultiplier() {
		if (this.inheritedValues.containsKey(ConfigKey.HEALTH_PER_LEVEL)) {
			return (double) this.inheritedValues.get(ConfigKey.HEALTH_PER_LEVEL);
		}
		return this.healthMult;
	}

	@Override
	public void setHealthMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.HEALTH_PER_LEVEL);
		this.healthMult = multiplier;
		this.nodeConfig.set(ConfigKey.HEALTH_PER_LEVEL.toString(), (Object) this.healthMult);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public double getDamageMultiplier() {
		if (this.inheritedValues.containsKey(ConfigKey.DAMAGE_PER_LEVEL)) {
			return (double) this.inheritedValues.get(ConfigKey.DAMAGE_PER_LEVEL);
		}
		return this.damageMult;
	}

	@Override
	public void setDamageMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.DAMAGE_PER_LEVEL);
		this.damageMult = multiplier;
		this.nodeConfig.set(ConfigKey.DAMAGE_PER_LEVEL.toString(), (Object) this.damageMult);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public double getDefenseMultiplier() {
		if (this.inheritedValues.containsKey(ConfigKey.DEFENSE_PER_LEVEL)) {
			return (double) this.inheritedValues.get(ConfigKey.DEFENSE_PER_LEVEL);
		}
		return this.defenseMult;
	}

	@Override
	public void setDefenseMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.DEFENSE_PER_LEVEL);
		this.defenseMult = multiplier;
		this.nodeConfig.set(ConfigKey.DEFENSE_PER_LEVEL.toString(), (Object) this.defenseMult);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public double getMoneyMultiplier() {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_PER_LEVEL)) {
			return (double) this.inheritedValues.get(ConfigKey.MONEY_PER_LEVEL);
		}
		return this.moneyMult;
	}

	@Override
	public void setMoneyMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.MONEY_PER_LEVEL);
		this.moneyMult = multiplier;
		this.nodeConfig.set(ConfigKey.MONEY_PER_LEVEL.toString(), (Object) this.moneyMult);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public double getMoneyRandomizer() {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_RANDOM)) {
			return (double) this.inheritedValues.get(ConfigKey.MONEY_RANDOM);
		}
		return this.moneyRandomizer;
	}

	@Override
	public void setMoneyRandomizer(final double randomizer) {
		this.inheritedValues.remove(ConfigKey.MONEY_RANDOM);
		this.moneyRandomizer = randomizer;
		this.nodeConfig.set(ConfigKey.MONEY_RANDOM.toString(), (Object) this.moneyRandomizer);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public double getExperienceMultiplier() {
		if (this.inheritedValues.containsKey(ConfigKey.EXPERIENCE_MODIFIER)) {
			return (double) this.inheritedValues.get(ConfigKey.EXPERIENCE_MODIFIER);
		}
		return this.expMult;
	}

	@Override
	public void setExperienceMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.EXPERIENCE_MODIFIER);
		this.expMult = multiplier;
		this.nodeConfig.set(ConfigKey.EXPERIENCE_MODIFIER.toString(), (Object) this.expMult);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public boolean isMobArenaLeveled() {
		if (this.inheritedValues.containsKey(ConfigKey.MOB_ARENA_ENABLED)) {
			return (boolean) this.inheritedValues.containsKey(ConfigKey.MOB_ARENA_ENABLED);
		}
		return this.mobArenaLevelingEnabled;
	}

	@Override
	public void setMobArenaLeveled(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.MOB_ARENA_ENABLED);
		this.mobArenaLevelingEnabled = enabled;
		this.nodeConfig.set(ConfigKey.MOB_ARENA_ENABLED.toString(), (Object) this.mobArenaLevelingEnabled);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public boolean isMobArenaWaveLeveled() {
		if (this.inheritedValues.containsKey(ConfigKey.MOB_ARENA_WAVE_LEVELING)) {
			return (boolean) this.inheritedValues.get(ConfigKey.MOB_ARENA_WAVE_LEVELING);
		}
		return this.mobArenaWaveLevelingEnabled;
	}

	@Override
	public void setMobArenaWaveLeveled(final boolean enabled) {
		this.inheritedValues.remove(ConfigKey.MOB_ARENA_WAVE_LEVELING);
		this.mobArenaWaveLevelingEnabled = enabled;
		this.nodeConfig.set(ConfigKey.MOB_ARENA_WAVE_LEVELING.toString(), (Object) this.mobArenaWaveLevelingEnabled);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public double getMobArenaMultiplier() {
		if (this.inheritedValues.containsKey(ConfigKey.MOB_ARENA_MULTIPLIER)) {
			return (double) this.inheritedValues.get(ConfigKey.MOB_ARENA_MULTIPLIER);
		}
		return this.mobArenaMultiplier;
	}

	@Override
	public void setMobArenaMultiplier(final double multiplier) {
		this.inheritedValues.remove(ConfigKey.MOB_ARENA_MULTIPLIER);
		this.mobArenaMultiplier = multiplier;
		this.nodeConfig.set(ConfigKey.MOB_ARENA_MULTIPLIER.toString(), (Object) this.mobArenaMultiplier);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public double getMobArenaWavesPerLevel() {
		if (this.inheritedValues.containsKey(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL)) {
			return (double) this.inheritedValues.get(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL);
		}
		return this.mobArenaWavesPerLevel;
	}

	@Override
	public void setMobArenaWavesPerLevel(final double wavesPerLevel) {
		this.inheritedValues.remove(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL);
		this.mobArenaWavesPerLevel = wavesPerLevel;
		this.nodeConfig.set(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL.toString(), (Object) this.mobArenaWavesPerLevel);
		this.worldConfig.saveNodeConfig();
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
		this.nodeConfig.set(ConfigKey.ALWAYS_SHOW_MOB_NAME.toString(), (Object) this.alwaysShowMobName);
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public void setMobNameLanguage(final Language lang) {
		this.inheritedValues.remove(ConfigKey.NAME_LANGUAGE);
		this.language = lang;
		this.nodeConfig.set(ConfigKey.NAME_LANGUAGE.toString(), (Object) lang.toString());
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public Language getMobNameLanguage() {
		if (this.inheritedValues.containsKey(ConfigKey.NAME_LANGUAGE)) {
			return (Language) this.inheritedValues.get(ConfigKey.NAME_LANGUAGE);
		}
		return this.language;
	}

	@Override
	public boolean canLevel(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.LEVELED_MOBS)) {
			return ((ArrayList<EntityType>) this.inheritedValues.get(ConfigKey.LEVELED_MOBS)).contains(ent);
		}
		return this.leveledMobs.contains(ent);
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
		if(this.getMoneyMobs().get(ent) == null)this.moneyMobs.put(ent,0.0);
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
	public void setLeveledMobs(final ArrayList<EntityType> leveledMobs) {
		this.inheritedValues.remove(ConfigKey.LEVELED_MOBS);
		this.leveledMobs = (ArrayList<EntityType>) leveledMobs.clone();
		this.nodeConfig.set(ConfigKey.LEVELED_MOBS.toString(), (Object) this.entListToStringList(leveledMobs));
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public void addLeveledMob(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.LEVELED_MOBS)) {
			this.leveledMobs = (ArrayList<EntityType>) this.inheritedValues.remove(ConfigKey.LEVELED_MOBS);
		}
		if (!this.leveledMobs.contains(ent)) {
			this.leveledMobs.add(ent);
			this.nodeConfig.set(ConfigKey.LEVELED_MOBS.toString(), (Object) this.entListToStringList(this.leveledMobs));
			this.worldConfig.saveNodeConfig();
		}
	}

	@Override
	public void removeLeveledMob(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.LEVELED_MOBS)) {
			this.leveledMobs = (ArrayList<EntityType>) this.inheritedValues.remove(ConfigKey.LEVELED_MOBS);
		}
		if (this.leveledMobs.contains(ent)) {
			this.leveledMobs.remove(ent);
			this.nodeConfig.set(ConfigKey.LEVELED_MOBS.toString(), (Object) this.entListToStringList(this.leveledMobs));
			this.worldConfig.saveNodeConfig();
		}
	}

	@Override
	public void setBlockedMobs(final ArrayList<EntityType> blockedMobs) {
		this.inheritedValues.remove(ConfigKey.BLOCKED_MOBS);
		this.blockedMobs = blockedMobs;
		this.nodeConfig.set(ConfigKey.BLOCKED_MOBS.toString(), (Object) this.entListToStringList(blockedMobs));
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public void addBlockedMob(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.BLOCKED_MOBS)) {
			this.blockedMobs = (ArrayList<EntityType>) this.inheritedValues.remove(ConfigKey.BLOCKED_MOBS);
		}
		if (!this.blockedMobs.contains(ent)) {
			this.blockedMobs.add(ent);
			this.nodeConfig.set(ConfigKey.BLOCKED_MOBS.toString(), (Object) this.entListToStringList(this.blockedMobs));
			this.worldConfig.saveNodeConfig();
		}
	}

	@Override
	public void removeBlockedMob(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.BLOCKED_MOBS)) {
			this.blockedMobs = (ArrayList<EntityType>) this.inheritedValues.remove(ConfigKey.BLOCKED_MOBS);
		}
		if (this.blockedMobs.contains(ent)) {
			this.blockedMobs.remove(ent);
			this.nodeConfig.set(ConfigKey.BLOCKED_MOBS.toString(), (Object) this.entListToStringList(this.blockedMobs));
			this.worldConfig.saveNodeConfig();
		}
	}

	@Override
	public void setMoneyMobs(final HashMap<EntityType, Double> moneyMobs) {
		this.inheritedValues.remove(ConfigKey.MONEY_MOBS);
		this.moneyMobs = (HashMap<EntityType, Double>) moneyMobs;
		this.nodeConfig.set(ConfigKey.MONEY_MOBS.toString(), (Object) this.MoneyHashMapToStringList(moneyMobs));
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public void addMoneyMob(final EntityType ent, final double amount) {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_MOBS)) {
			this.inheritedValues.remove(ConfigKey.MONEY_MOBS);
		}
		if(this.moneyMobs != null) {
		this.moneyMobs.put(ent, amount);
		this.nodeConfig.set(ConfigKey.MONEY_MOBS.toString(),
				(Object) this.MoneyHashMapToStringList(this.moneyMobs));
		this.worldConfig.saveNodeConfig();
		}else
		{
			this.getMoneyMobs();
			this.moneyMobs.put(ent, amount);
			this.nodeConfig.set(ConfigKey.MONEY_MOBS.toString(),
					(Object) this.MoneyHashMapToStringList(this.moneyMobs));
			this.worldConfig.saveNodeConfig();	
		}
	}

	@Override
	public void removeMoneyMob(final EntityType ent) {
		if (this.inheritedValues.containsKey(ConfigKey.MONEY_MOBS)) {
			this.moneyMobs = (HashMap<EntityType, Double>) this.inheritedValues.remove(ConfigKey.MONEY_MOBS);
		}
		this.moneyMobs.put(ent, 0.0);
		this.nodeConfig.set(ConfigKey.MONEY_MOBS.toString(), (Object) this.MoneyHashMapToStringList(this.moneyMobs));
		this.worldConfig.saveNodeConfig();
	}

	@Override
	public void useInheritedValue(final ConfigKey key) {
		this.inheritedValues.put(key, this.worldConfig.getValue(key));
		this.nodeConfig.set(key.toString(), (Object) null);
		this.worldConfig.saveNodeConfig();
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
		this.nodeConfig.set(ConfigKey.LEVELED_SPAWNERS.toString(), (Object) leveledSpawners);
		this.worldConfig.saveNodeConfig();
	}

	public void updateInheritedValues() {
		for (final ConfigKey key : this.inheritedValues.keySet()) {
			this.inheritedValues.put(key, this.worldConfig.getValue(key));
		}
	}

	public EntityType getRandomAllowedMob() {
		ArrayList<EntityType> blocked;
		if (this.isValueInherited(ConfigKey.BLOCKED_MOBS)) {
			blocked = (ArrayList<EntityType>) this.inheritedValues.get(ConfigKey.BLOCKED_MOBS);
		} else {
			blocked = this.blockedMobs;
		}
		ArrayList<EntityType> allowed;
		if (this.isValueInherited(ConfigKey.LEVELED_MOBS)) {
			allowed = (ArrayList<EntityType>) this.inheritedValues.get(ConfigKey.LEVELED_MOBS);
		} else {
			allowed = this.blockedMobs;
		}
		int maxTries;
		int currentTries;
		int random;
		for (maxTries = 5, currentTries = 0, random = this.rand.nextInt(allowed.size()); blocked.contains(
				allowed.get(random)) && currentTries++ < maxTries; random = this.rand.nextInt(allowed.size())) {
		}
		return allowed.get(random);
	}

	public WorldConfig getWorldConfig() {
		return this.worldConfig;
	}

	static {
		spawnNodes = new HashMap<WorldConfig, ArrayList<SpawnNode>>();
	}

}
