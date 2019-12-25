package iomatix.spigot.rpgleveledmobs.tools;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.config.SpawnNode;
import iomatix.spigot.rpgleveledmobs.config.cfgModule;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.spawnsController.MobNamesMap;

public class MobData {

	public static void LoadMobsData() {
		for (final World world : Bukkit.getWorlds()) {
			for (final LivingEntity ent : world.getLivingEntities()) {
				LoadTheMetaData(ent);
			}
		}
	}

	public static void LoadTheMetaData(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.RPGmob.toString()))
			livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
		LoadMobMetaData(livingEntity, CreatureSpawnEvent.SpawnReason.DEFAULT);
	}

	public static void LoadMobMetaData(LivingEntity livingEntity, CreatureSpawnEvent.SpawnReason SpawnReason) {
		final EntityType entityType = livingEntity.getType();
		final Location location = livingEntity.getLocation();
		if (livingEntity.hasMetadata(MetaTag.RPGmob.toString())) {
			return;
		}
		boolean slime = false;
		if (SpawnReason == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
			slime = true;
		}
		final SpawnNode node = cfgModule.getConfigModule().getSpawnNode(location);
		if (node == null) {
			return;
		}
		if (!node.isLeveledSpawners() && SpawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER) {
			return;
		}
		if (node.isBlocked(entityType)) {
			return;
		}
		if (!node.canLevel(entityType)) {
			return;
		}
		if (node.isExperienceModified()) {
			if (node.isArenaLocation(location)) {
				if (!node.isMobArenaLeveled()) {
					return;
				}

				SetMetaArenaExpMod(livingEntity,
						(Object) (node.getMobArenaMultiplier() * node.getExperienceMultiplier()));

			} else {
				SetMetaExpMod(livingEntity, (Object) node.getExperienceMultiplier());
				SetMetaExpAddon(livingEntity, (Object) node.getExperienceAddon());
			}
		}
		int level = node.getLevel(location);
		if (node.isRPGLevelRandomizer() && node.getRPGLevelMax() > 0) {
			if (node.getRPGLevelFormula().toUpperCase().contains("MAIN"))
				level = level + BiasedRandom.randomInt(0, node.getRPGLevelMax());
			else if (node.getRPGLevelFormula().toUpperCase().contains("ALTER"))
				level = level + BiasedRandom.randomIntAlter(0, node.getRPGLevelMax());
			else
				level = level + BiasedRandom.randomIntOld(0, node.getRPGLevelMax());
		}
		if (Main.RPGMobs.getExperienceScalingModuleInstance().isSkillApiHandled()) {
			if (livingEntity instanceof Tameable) {
				if (((Tameable) livingEntity).getOwner() != null) {
					PlayerData playerData = SkillAPI
							.getPlayerData((OfflinePlayer) ((Tameable) livingEntity).getOwner());
					int levelSKILLAPI = playerData.hasClass() ? playerData.getMainClass().getLevel() : 0;
					if (levelSKILLAPI > 0) {
						level = levelSKILLAPI;
					}
				}
			}
		}
		SetMetaRPGMob(livingEntity);
		SetMetaLevel(livingEntity, (Object) level);
		if (node.isDamageModified()) {
			SetMetaDamageMod(livingEntity, (Object) node.getDamageMultiplier());
			SetMetaDamageAddon(livingEntity, (Object) node.getDamageAddon());
		}
		if (node.isDefenseModified()) {
			SetMetaDefenseMod(livingEntity, (Object) node.getDefenseMultiplier());
			SetMetaDefenseAddon(livingEntity, (Object) node.getDefenseAddon());
		}
		if (node.isMoneyModified()) {
			SetMetaMoneyRandomizer(livingEntity, (Object) node.getMoneyRandomizer());
			SetMetaMoneyDrop(livingEntity, (Object) node.getMoneyMob(livingEntity.getType()));
			SetMetaMoneyMod(livingEntity, (Object) node.getMoneyMultiplier());

		}
		if (node.isHealthModified()) {

			final double startMaxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue(); // .getDefaultValue()
			final double healthMultiplier = node.getHealthMultiplier();
			final double healthAddon = node.getHealthAddon();
			final double NewHealthMod = healthAddon * level + startMaxHealth * level * healthMultiplier;

			SetMetaHealthMod(livingEntity, (Object) healthMultiplier);
			SetMetaHealthAddon(livingEntity, (Object) node.getHealthAddon());
			SetMetaBaseAdditionalHealth(livingEntity, (Object) NewHealthMod);

			final AttributeModifier HealthMod = new AttributeModifier("RPGMobsHealthMod", NewHealthMod,
					AttributeModifier.Operation.ADD_NUMBER);
			if (livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers() != null) {
				for (AttributeModifier modifier : livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
						.getModifiers()) {
					if (modifier.getName().equalsIgnoreCase("RPGMobsHealthMod"))
						livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(modifier);
				}
			}
			livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(HealthMod);
			livingEntity.setHealth(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		}

		String startName = livingEntity.getCustomName();
		if (startName == null || startName.toLowerCase().equals("null")) {
			if (node.getMobNameLanguage() != null) {
				if (MobNamesMap.getMobName(node.getMobNameLanguage(), livingEntity.getType()) != null) {
					startName = ChatColor.WHITE
							+ MobNamesMap.getMobName(node.getMobNameLanguage(), livingEntity.getType());
				} else {
					startName = "";
				}
			} else {
				startName = livingEntity.getName();
			}
		}

		SetMetaCustomName(livingEntity, (Object) startName);

		if (!slime && node.isPrefixEnabled()) {
			startName = ChatColor.stripColor(startName);
			startName = startName.replaceAll(node.getPrefixFormat().replace("]", "\\]").replace("[", "\\[")
					.replaceAll("&.", "").replace("#", "[0-9]*"), "").replace(" ", "");

			startName = ChatColor.translateAlternateColorCodes('&',
					node.getPrefixFormat().replace("#", level + "") + " " + ChatColor.WHITE + startName);
		}

		if (!slime && node.isSuffixEnabled()) {
			startName = ChatColor.stripColor(startName);
			startName = startName.replaceAll(node.getSuffixFormat().replace("]", "\\]").replace("[", "\\[")
					.replaceAll("&.", "").replace("#", "[0-9]*"), "").replace(" ", "");

			startName = ChatColor.translateAlternateColorCodes('&',
					startName + " " + node.getSuffixFormat().replace("#", level + ""));
		}
		if (!slime && startName != null && startName.length() > 1
				&& (node.isSuffixEnabled() || node.isPrefixEnabled())) {
			livingEntity.setCustomName(startName);
		}
		livingEntity.setCustomNameVisible(node.isAlwaysShowMobName());

	}

	// Meta Methods:

	public static void SetMetaCustomName(LivingEntity livingEntity, Object value) {
		if (!livingEntity.hasMetadata(MetaTag.CustomName.toString()))
			livingEntity.setMetadata(MetaTag.CustomName.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static String GetMetaCustomName(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.CustomName.toString())) {
			return livingEntity.getMetadata(MetaTag.CustomName.toString()).get(0).asString();
		} else {
			return "";
		}

	}

	public static void SetMetaBaseAdditionalHealth(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.BaseAdditionalHealth.toString()))
			livingEntity.removeMetadata(MetaTag.BaseAdditionalHealth.toString(), (Plugin) Main.RPGMobs);
		livingEntity.setMetadata(MetaTag.BaseAdditionalHealth.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaBaseAdditionalHealth(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.BaseAdditionalHealth.toString())) {
			return livingEntity.getMetadata(MetaTag.BaseAdditionalHealth.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaHealthAddon(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.HealthAddon.toString())) {
			livingEntity.removeMetadata(MetaTag.HealthAddon.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.HealthAddon.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaHealthAddon(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.HealthAddon.toString())) {
			return livingEntity.getMetadata(MetaTag.HealthAddon.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaHealthMod(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.HealthMod.toString()))
			livingEntity.removeMetadata(MetaTag.HealthMod.toString(), (Plugin) Main.RPGMobs);
		livingEntity.setMetadata(MetaTag.HealthMod.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaHealthMod(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.HealthMod.toString())) {
			return livingEntity.getMetadata(MetaTag.HealthMod.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaMoneyMod(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.MoneyMod.toString())) {
			livingEntity.removeMetadata(MetaTag.MoneyMod.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.MoneyMod.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaMoneyMod(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.MoneyMod.toString())) {
			return livingEntity.getMetadata(MetaTag.MoneyMod.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaMoneyDrop(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.MoneyDrop.toString())) {
			livingEntity.removeMetadata(MetaTag.MoneyDrop.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.MoneyDrop.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaMoneyDrop(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.MoneyDrop.toString())) {
			return livingEntity.getMetadata(MetaTag.MoneyDrop.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaMoneyRandomizer(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.MoneyRandomizer.toString())) {
			livingEntity.removeMetadata(MetaTag.MoneyRandomizer.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.MoneyRandomizer.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaMoneyRandomizer(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.MoneyRandomizer.toString())) {
			return livingEntity.getMetadata(MetaTag.MoneyRandomizer.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaDefenseAddon(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.DefenseAddon.toString())) {
			livingEntity.removeMetadata(MetaTag.DefenseAddon.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.DefenseAddon.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));

	}

	public static double GetMetaDefenseAddon(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.DefenseAddon.toString())) {
			return livingEntity.getMetadata(MetaTag.DefenseAddon.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaDefenseMod(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.DefenseMod.toString())) {
			livingEntity.removeMetadata(MetaTag.DefenseMod.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.DefenseMod.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaDefenseMod(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.DefenseMod.toString())) {
			return livingEntity.getMetadata(MetaTag.DefenseMod.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaDamageAddon(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.DamageAddon.toString())) {
			livingEntity.removeMetadata(MetaTag.DamageAddon.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.DamageAddon.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));

	}

	public static double GetMetaDamageAddon(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.DamageAddon.toString())) {
			return livingEntity.getMetadata(MetaTag.DamageAddon.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaDamageMod(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.DamageMod.toString())) {
			livingEntity.removeMetadata(MetaTag.DamageMod.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.DamageMod.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaDamageMod(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.DamageMod.toString())) {
			return livingEntity.getMetadata(MetaTag.DamageMod.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaLevel(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.Level.toString())) {
			livingEntity.removeMetadata(MetaTag.Level.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.Level.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));

	}

	public static int GetMetaLevel(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.Level.toString())) {
			return livingEntity.getMetadata(MetaTag.Level.toString()).get(0).asInt();
		} else {
			return 0;
		}

	}

	public static void SetMetaExpAddon(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.ExpAddon.toString())) {
			livingEntity.removeMetadata(MetaTag.ExpAddon.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.ExpAddon.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));

	}

	public static double GetMetaExpAddon(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.ExpAddon.toString())) {
			return livingEntity.getMetadata(MetaTag.ExpAddon.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaExpMod(LivingEntity livingEntity, Object value) {

		if (livingEntity.hasMetadata(MetaTag.ExpMod.toString())) {
			livingEntity.removeMetadata(MetaTag.ExpMod.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.ExpMod.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaExpMod(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.ExpMod.toString())) {
			return livingEntity.getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaArenaExpMod(LivingEntity livingEntity, Object value) {
		if (livingEntity.hasMetadata(MetaTag.ArenaExpMod.toString())) {
			livingEntity.removeMetadata(MetaTag.ArenaExpMod.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.ArenaExpMod.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, value));
	}

	public static double GetMetaArenaExpMod(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.ArenaExpMod.toString())) {
			return livingEntity.getMetadata(MetaTag.ArenaExpMod.toString()).get(0).asDouble();
		} else {
			return 0;
		}

	}

	public static void SetMetaRPGMob(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.RPGmob.toString())) {
			livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.RPGmob.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) true));

	}

	public static boolean GetMetaRPGMob(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.RPGmob.toString())) {
			return true;
		}
		return false;
	}

}
