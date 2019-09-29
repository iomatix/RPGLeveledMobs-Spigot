package iomatix.spigot.rpgleveledmobs.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Collection;

import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.EntityType;

import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.spawnsController.MobNamesMap;
import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.tools.Language;

public enum ConfigKey {

	MIN_LEVEL("Leveling.MinLevel", "The minimum level a mob is allowed to be."),
	MAX_LEVEL("Leveling.MaxLevel", "The maximum level a mob is allowed to be."),
	START_LEVEL("Leveling.StartLevel", "The base level every mob starts at when calculating level."),
	DISTANCE_PER_LEVEL("Leveling.DistancePerLevel", "How many blocks until the level of a mob is increased."),
	HEALTH_MOD_ENABLED("Stats.Health.Enabled", "Whether or not to increase a mobs health with their level."),
	HEALTH_PER_LEVEL("Stats.Health.Multiplier",
			"The percentage of health increase per level. Formula = [BaseHealth + (BaseHealth * level * multiplier)]"),
	DAMAGE_MOD_ENABLE("Stats.Damage.Enabled", "Whether or not to increase the damage a mob will do by their level."),
	DAMAGE_PER_LEVEL("Stats.Damage.Multiplier",
			"The percentage of damage increase per level. Formula = [BaseDamage + (BaseDamage * level * mulitplier)]"),
	DEFENSE_MOD_ENABLE("Stats.Defense.Enabled",
			"Wheter or not to increase the total mob defense by their level. Formula = [damageTaken * (1 - min(20,max(multiplier * level/5,level*multiplier-damageTaken/2))/25)]"),
	DEFENSE_PER_LEVEL("Stats.Defense.Multiplier", "The defense points per level."),
	EXPERIENCE_MOD_ENABLED("Stats.Experience.Enabled",
			"Whether or not to increase the amount of experience dropped by a monster"),
	EXPERIENCE_MODIFIER("Stats.Experience.Multiplier",
			"The percentage of experience increase per level. Formula = [BaseExp + (BaseExp * level * multiplier)]"),
	LEVELED_MOBS("Spawning.LeveledMobs", "A list of mobs that are assigned levels when spawned."),
	BLOCKED_MOBS("Spawning.BlockedMobs", "A list of mobs that are prevented from spawning."),
	LEVELED_SPAWNERS("Spawning.LeveledSpawners", "Should mobs from spawners be leveled?"),
	USE_SUFFIX("Naming.Suffix.Enabled", "Whether or not to end the mob's name with their level."),
	USE_PREFIX("Naming.Prefix.Enabled", "Whether or not to start the mobs name with their level."),
	SUFFIX_FORMAT("Naming.Suffix.Format", "The format for the suffix. Use %l for the mobs level."),
	PREFIX_FORMAT("Naming.Prefix.Format", "The format for the prefix. Use %l for the mobs level."),
	ALWAYS_SHOW_MOB_NAME("Naming.AlwaysShowName.Enabled",
			"Whether or not to always display mob's name no matter distance."),
	NAME_LANGUAGE("Naming.Language", "What language to use for the mob's name."),
	MOB_ARENA_ENABLED("MobArena.Enabled", "Whether or not to allow MobArena mobs to be assigned a level."),
	MOB_ARENA_WAVE_LEVELING("MobArena.WaveLeveling",
			"Whether or not to level the mobs in a mob arena by the wave number."),
	MOB_ARENA_WAVES_PER_LEVEL("MobArena.WavesPerLevel", "How many waves before increasing level"),
	MOB_ARENA_MULTIPLIER("MobArena.Multiplier",
			"What percentage of experience to drop for mobs killed inside of a mob arena. [ModifiedExperience * multiplier]"),
	MONEY_MOBS("Economy.MoneyMobs", "A list of mobs with currency values assigned which each drops on death."),
	MONEY_MOD_ENABLE("Stats.Money.Enabled","Whether or not to increase the money amount a mob will drop by their level."),
	MONEY_PER_LEVEL("Stats.Money.Multiplier","The percentage of money increase per level. Formula = [BaseMoney + (BaseMoney * level * multiplier)]"),
	MONEY_RANDOM("Stats.Money.Random","Value by money drop may increase or decrease."),
	MONEY_TOWNY_RATIO("Economy.Towny.TownyRatio","Percent of the money received by Town - Towny Value."),
	MONEY_TOWNY_SUBTRACT("Economy.Towny.TownySubtract","Boolean: Subtract the Towny Value from income?"),
	MONEY_TOWNY_SUPPORTNATION("Economy.Towny.NationSupport","Boolean: Support both Nation and Town or only the Town?"),
	MONEY_TAKE_MONEY_ON_KILL("Economy.Misc.NoMoneyDrop","Money send to player on mob's kill instead of drop on the ground.");
	
	
	private String path;
	private String description;
	public static Double TownyRatio;
	public static boolean TownySubtract;
	public static boolean TownySupportNation;
	public static HashMap<ConfigKey, Object> defaultMap;
	public static HashMap<EntityType, Double> moneyMap;
	public static HashMap<EntityType, Double> defaultMoneyAll;
	public static ArrayList<EntityType> defaultLeveled; 
	private static final ArrayList<EntityType> defaultBlockedVanilla;
	private static final ArrayList<EntityType> defaultBlockedNether;
	private static final ArrayList<EntityType> defaultBlockedEnd;

	private ConfigKey(final String path, final String description) {
		this.path = path;
		this.description = description;
	}

	@Override
	public String toString() {
		return this.path;
	}

	public String getDescription() {
		return this.description;
	}

	public static ArrayList<EntityType> getDefaultBlocked(final World world) {
		if (world == null) {
			return ConfigKey.defaultBlockedVanilla;
		}
		switch (world.getEnvironment()) {
		case THE_END: {
			return ConfigKey.defaultBlockedEnd;
		}
		case NETHER: {
			return ConfigKey.defaultBlockedNether;
		}
		case NORMAL: {
			return ConfigKey.defaultBlockedVanilla;
		}
		default: {
			return ConfigKey.defaultBlockedVanilla;
		}
		}
	}
	
	public static HashMap<EntityType,Double> getDefaultMoney(final World world) {
		if (world == null) {
			return ConfigKey.defaultMoneyAll;
		}
		switch (world.getEnvironment()) {
		case THE_END: {
			return ConfigKey.defaultMoneyAll;
		}
		case NETHER: {
			return ConfigKey.defaultMoneyAll;
		}
		case NORMAL: {
			return ConfigKey.defaultMoneyAll;
		}
		default: {
			return ConfigKey.defaultMoneyAll;
		}
		}
	}
	
	

	static {
		ConfigKey.defaultMap = new HashMap<ConfigKey, Object>();
		ConfigKey.moneyMap = new HashMap<EntityType, Double>();
		ConfigKey.defaultLeveled = new ArrayList<EntityType>();
		defaultBlockedVanilla = new ArrayList<EntityType>();
		defaultBlockedNether = new ArrayList<EntityType>();
		defaultBlockedEnd = new ArrayList<EntityType>();
		ConfigKey.defaultBlockedVanilla.add(EntityType.ENDER_DRAGON);
		ConfigKey.defaultBlockedVanilla.add(EntityType.GHAST);
		ConfigKey.defaultBlockedVanilla.add(EntityType.MAGMA_CUBE);
		ConfigKey.defaultBlockedVanilla.add(EntityType.PIG_ZOMBIE);
		ConfigKey.defaultLeveled.add(EntityType.BLAZE);
		ConfigKey.defaultLeveled.add(EntityType.CAVE_SPIDER);
		ConfigKey.defaultLeveled.add(EntityType.CREEPER);
		ConfigKey.defaultLeveled.add(EntityType.ENDER_DRAGON);
		ConfigKey.defaultLeveled.add(EntityType.ENDERMAN);
		ConfigKey.defaultLeveled.add(EntityType.GHAST);
		ConfigKey.defaultLeveled.add(EntityType.GIANT);
		ConfigKey.defaultLeveled.add(EntityType.ZOMBIE);
		ConfigKey.defaultLeveled.add(EntityType.IRON_GOLEM);
		ConfigKey.defaultLeveled.add(EntityType.MAGMA_CUBE);
		ConfigKey.defaultLeveled.add(EntityType.PIG_ZOMBIE);
		ConfigKey.defaultLeveled.add(EntityType.WITHER);
		ConfigKey.defaultLeveled.add(EntityType.SILVERFISH);
		ConfigKey.defaultLeveled.add(EntityType.SKELETON);
		ConfigKey.defaultLeveled.add(EntityType.SLIME);
		ConfigKey.defaultLeveled.add(EntityType.SNOWMAN);
		ConfigKey.defaultLeveled.add(EntityType.SPIDER);
		ConfigKey.defaultLeveled.add(EntityType.WITCH);
		ConfigKey.defaultBlockedEnd.addAll(ConfigKey.defaultLeveled);
		ConfigKey.defaultBlockedEnd.add(EntityType.MAGMA_CUBE);
		ConfigKey.defaultBlockedEnd.remove(EntityType.ENDER_DRAGON);
		ConfigKey.defaultBlockedEnd.remove(EntityType.ENDERMAN);
		ConfigKey.defaultBlockedEnd.add(EntityType.HORSE);
		ConfigKey.defaultBlockedEnd.add(EntityType.BAT);
		ConfigKey.defaultBlockedEnd.add(EntityType.PIG);
		ConfigKey.defaultBlockedEnd.add(EntityType.COW);
		ConfigKey.defaultBlockedEnd.add(EntityType.MUSHROOM_COW);
		ConfigKey.defaultBlockedEnd.add(EntityType.CHICKEN);
		ConfigKey.defaultBlockedEnd.add(EntityType.OCELOT);
		ConfigKey.defaultBlockedEnd.add(EntityType.SHEEP);
		ConfigKey.defaultBlockedEnd.add(EntityType.SQUID);
		ConfigKey.defaultBlockedEnd.add(EntityType.VILLAGER);
		ConfigKey.defaultBlockedEnd.add(EntityType.WOLF);
		ConfigKey.defaultBlockedNether.addAll(ConfigKey.defaultBlockedEnd);
		ConfigKey.defaultBlockedNether.add(EntityType.ENDERMAN);
		ConfigKey.defaultBlockedNether.add(EntityType.ENDER_DRAGON);
		ConfigKey.defaultBlockedNether.remove(EntityType.GHAST);
		ConfigKey.defaultBlockedNether.remove(EntityType.PIG_ZOMBIE);
		ConfigKey.defaultBlockedNether.remove(EntityType.BLAZE);
		ConfigKey.defaultBlockedNether.remove(EntityType.SKELETON);
		ConfigKey.defaultBlockedNether.remove(EntityType.MAGMA_CUBE);
		ConfigKey.defaultMap.put(ConfigKey.MIN_LEVEL, 1);
		ConfigKey.defaultMap.put(ConfigKey.MAX_LEVEL, 100);
		ConfigKey.defaultMap.put(ConfigKey.START_LEVEL, 1);
		ConfigKey.defaultMap.put(ConfigKey.DISTANCE_PER_LEVEL, 35.0);
		ConfigKey.defaultMap.put(ConfigKey.HEALTH_MOD_ENABLED, true);
		ConfigKey.defaultMap.put(ConfigKey.HEALTH_PER_LEVEL, 0.2);
		ConfigKey.defaultMap.put(ConfigKey.DAMAGE_MOD_ENABLE, true);
		ConfigKey.defaultMap.put(ConfigKey.DAMAGE_PER_LEVEL, 1.0);
		ConfigKey.defaultMap.put(ConfigKey.DEFENSE_MOD_ENABLE, true);
		ConfigKey.defaultMap.put(ConfigKey.DEFENSE_PER_LEVEL, 3.0);
		ConfigKey.defaultMap.put(ConfigKey.MONEY_MOD_ENABLE, true);
		ConfigKey.defaultMap.put(ConfigKey.MONEY_PER_LEVEL, 0.35);
		ConfigKey.defaultMap.put(ConfigKey.MONEY_RANDOM, 1.5);
		ConfigKey.defaultMap.put(ConfigKey.LEVELED_MOBS, ConfigKey.defaultLeveled);
		ConfigKey.defaultMap.put(ConfigKey.BLOCKED_MOBS, ConfigKey.defaultBlockedVanilla);
		ConfigKey.defaultMap.put(ConfigKey.LEVELED_SPAWNERS, true);
		ConfigKey.defaultMap.put(ConfigKey.MOB_ARENA_ENABLED, false);
		ConfigKey.defaultMap.put(ConfigKey.MOB_ARENA_WAVE_LEVELING, false);
		ConfigKey.defaultMap.put(ConfigKey.MOB_ARENA_MULTIPLIER, 0.1);
		ConfigKey.defaultMap.put(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL, 1.0);
		ConfigKey.defaultMap.put(ConfigKey.USE_PREFIX, true);
		ConfigKey.defaultMap.put(ConfigKey.USE_SUFFIX, false);
		ConfigKey.defaultMap.put(ConfigKey.PREFIX_FORMAT, "[level #]");
		ConfigKey.defaultMap.put(ConfigKey.SUFFIX_FORMAT, "#");
		ConfigKey.defaultMap.put(ConfigKey.EXPERIENCE_MOD_ENABLED, true);
		ConfigKey.defaultMap.put(ConfigKey.EXPERIENCE_MODIFIER, 1.0);
		ConfigKey.defaultMap.put(ConfigKey.ALWAYS_SHOW_MOB_NAME, true);
		ConfigKey.defaultMap.put(ConfigKey.NAME_LANGUAGE, Language.ENGLISH);
		ConfigKey.defaultMap.put(ConfigKey.MONEY_TOWNY_RATIO, 0.04);
		ConfigKey.defaultMap.put(ConfigKey.MONEY_TOWNY_SUBTRACT, true);
		ConfigKey.defaultMap.put(ConfigKey.MONEY_TOWNY_SUPPORTNATION, true);
		ConfigKey.defaultMap.put(ConfigKey.MONEY_TAKE_MONEY_ON_KILL, false);
		try {
			ConfigKey.defaultBlockedEnd.remove(EntityType.ENDERMITE);
			ConfigKey.defaultLeveled.add(EntityType.GUARDIAN);
			cfgModule.version = 1.8;
		} catch (NoSuchFieldError e) {
			e.printStackTrace();
			LogsModule.error(ChatColor.RED + "RPGLeveledMobs officialy supports 1.14.4 and above.");
			Bukkit.getPluginManager().disablePlugin((Plugin) Main.RPGMobs);
		}
		try {
			ConfigKey.defaultLeveled.add(EntityType.SHULKER);
			ConfigKey.defaultBlockedNether.add(EntityType.SHULKER);
			ConfigKey.defaultBlockedVanilla.add(EntityType.SHULKER);
			cfgModule.version = 1.9;
		} catch (NoSuchFieldError e) {
		}
		try {
			ConfigKey.defaultLeveled.add(EntityType.STRAY);
			ConfigKey.defaultLeveled.add(EntityType.HUSK);
			ConfigKey.defaultLeveled.add(EntityType.POLAR_BEAR);
			cfgModule.version = 1.10;
		} catch (NoSuchFieldError e) {
			e.printStackTrace();
		}
		try {
			ConfigKey.defaultLeveled.add(EntityType.EVOKER);
			ConfigKey.defaultLeveled.add(EntityType.VEX);
			ConfigKey.defaultLeveled.add(EntityType.VINDICATOR);
			ConfigKey.defaultLeveled.add(EntityType.ELDER_GUARDIAN);
			cfgModule.version = 1.11;
		} catch (NoSuchFieldError e) {
		}
		try {
			ConfigKey.defaultLeveled.add(EntityType.PARROT);
			ConfigKey.defaultLeveled.add(EntityType.DOLPHIN);
			ConfigKey.defaultLeveled.add(EntityType.DROWNED);
			ConfigKey.defaultLeveled.add(EntityType.PHANTOM);
			ConfigKey.defaultLeveled.add(EntityType.COD);
			ConfigKey.defaultLeveled.add(EntityType.SALMON);
			ConfigKey.defaultLeveled.add(EntityType.PUFFERFISH);
			ConfigKey.defaultLeveled.add(EntityType.TROPICAL_FISH);
			ConfigKey.defaultLeveled.add(EntityType.TURTLE);
			cfgModule.version = 1.13;
		} catch (NoSuchFieldError e) {
		}
		try {
			ConfigKey.defaultLeveled.add(EntityType.CAT);
			ConfigKey.defaultLeveled.add(EntityType.FOX);
			ConfigKey.defaultLeveled.add(EntityType.PANDA);
			ConfigKey.defaultLeveled.add(EntityType.PILLAGER);
			ConfigKey.defaultLeveled.add(EntityType.RAVAGER);
			ConfigKey.defaultLeveled.add(EntityType.TRADER_LLAMA);
			ConfigKey.defaultLeveled.add(EntityType.WANDERING_TRADER);
			cfgModule.version = 1.14;
		} catch (NoSuchFieldError e) {
		}
		try {
			ConfigKey.moneyMap.put(EntityType.BAT,0.0);
			ConfigKey.moneyMap.put(EntityType.BLAZE,0.0);
			ConfigKey.moneyMap.put(EntityType.CAT,0.0);
			ConfigKey.moneyMap.put(EntityType.CAVE_SPIDER,0.0);
			ConfigKey.moneyMap.put(EntityType.CHICKEN,0.0);
			ConfigKey.moneyMap.put(EntityType.COD,0.0);
			ConfigKey.moneyMap.put(EntityType.COW,0.0);
			ConfigKey.moneyMap.put(EntityType.CREEPER,0.0);
			ConfigKey.moneyMap.put(EntityType.DOLPHIN,0.0);
			ConfigKey.moneyMap.put(EntityType.DONKEY,0.0);
			ConfigKey.moneyMap.put(EntityType.DROWNED,0.0);
			ConfigKey.moneyMap.put(EntityType.ELDER_GUARDIAN,0.0);
			ConfigKey.moneyMap.put(EntityType.ENDER_DRAGON,0.0);
			ConfigKey.moneyMap.put(EntityType.ENDERMAN,0.0);
			ConfigKey.moneyMap.put(EntityType.ENDERMITE,0.0);
			ConfigKey.moneyMap.put(EntityType.EVOKER,0.0);
			ConfigKey.moneyMap.put(EntityType.FOX,0.0);
			ConfigKey.moneyMap.put(EntityType.GHAST,0.0);
			ConfigKey.moneyMap.put(EntityType.GIANT,0.0);
			ConfigKey.moneyMap.put(EntityType.GUARDIAN,0.0);
			ConfigKey.moneyMap.put(EntityType.HORSE,0.0);
			ConfigKey.moneyMap.put(EntityType.HUSK,0.0);
			ConfigKey.moneyMap.put(EntityType.ILLUSIONER,0.0);
			ConfigKey.moneyMap.put(EntityType.IRON_GOLEM,0.0);
			ConfigKey.moneyMap.put(EntityType.LLAMA,0.0);
			ConfigKey.moneyMap.put(EntityType.MAGMA_CUBE,0.0);
			ConfigKey.moneyMap.put(EntityType.MUSHROOM_COW,0.0);
			ConfigKey.moneyMap.put(EntityType.MULE,0.0);
			ConfigKey.moneyMap.put(EntityType.OCELOT,0.0);
			ConfigKey.moneyMap.put(EntityType.PANDA,0.0);
			ConfigKey.moneyMap.put(EntityType.PARROT,0.0);
			ConfigKey.moneyMap.put(EntityType.PHANTOM,0.0);
			ConfigKey.moneyMap.put(EntityType.PIG,0.0);
			ConfigKey.moneyMap.put(EntityType.PILLAGER,0.0);
			ConfigKey.moneyMap.put(EntityType.POLAR_BEAR,0.0);
			ConfigKey.moneyMap.put(EntityType.PUFFERFISH,0.0);
			ConfigKey.moneyMap.put(EntityType.PUFFERFISH,0.0);
			ConfigKey.moneyMap.put(EntityType.RABBIT,0.0);
			ConfigKey.moneyMap.put(EntityType.RAVAGER,0.0);
			ConfigKey.moneyMap.put(EntityType.SALMON,0.0);
			ConfigKey.moneyMap.put(EntityType.SHEEP,0.0);
			ConfigKey.moneyMap.put(EntityType.SHULKER,0.0);
			ConfigKey.moneyMap.put(EntityType.SILVERFISH,0.0);
			ConfigKey.moneyMap.put(EntityType.SKELETON,0.0);
			ConfigKey.moneyMap.put(EntityType.SKELETON_HORSE,0.0);
			ConfigKey.moneyMap.put(EntityType.SLIME,0.0);
			ConfigKey.moneyMap.put(EntityType.SNOWMAN,0.0);
			ConfigKey.moneyMap.put(EntityType.SPIDER,0.0);
			ConfigKey.moneyMap.put(EntityType.SQUID,0.0);
			ConfigKey.moneyMap.put(EntityType.STRAY,0.0);
			ConfigKey.moneyMap.put(EntityType.TURTLE, 0.0);
			ConfigKey.moneyMap.put(EntityType.TRADER_LLAMA,0.0);
			ConfigKey.moneyMap.put(EntityType.TROPICAL_FISH,0.0);
			ConfigKey.moneyMap.put(EntityType.VEX, 0.0);
			ConfigKey.moneyMap.put(EntityType.VILLAGER, 0.0);
			ConfigKey.moneyMap.put(EntityType.VINDICATOR, 0.0);
			ConfigKey.moneyMap.put(EntityType.WANDERING_TRADER, 0.0);
			ConfigKey.moneyMap.put(EntityType.WITCH, 0.0);
			ConfigKey.moneyMap.put(EntityType.WITHER, 0.0);
			ConfigKey.moneyMap.put(EntityType.WITHER_SKELETON, 0.0);
			ConfigKey.moneyMap.put(EntityType.WOLF, 0.0);
			ConfigKey.moneyMap.put(EntityType.ZOMBIE, 0.0);
			ConfigKey.moneyMap.put(EntityType.ZOMBIE_HORSE, 0.0);
			ConfigKey.moneyMap.put(EntityType.PIG_ZOMBIE, 0.0);
			ConfigKey.moneyMap.put(EntityType.ZOMBIE_VILLAGER, 0.0);
			ConfigKey.defaultMoneyAll = ConfigKey.moneyMap;
			ConfigKey.defaultMap.put(ConfigKey.MONEY_MOBS, ConfigKey.moneyMap);
			cfgModule.version = 1.14;
		}catch(NoSuchFieldError e) {
		}
		
	}

}