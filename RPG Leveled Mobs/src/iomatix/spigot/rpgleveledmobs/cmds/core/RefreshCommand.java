package iomatix.spigot.rpgleveledmobs.cmds.core;

import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
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

import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.cmds.RPGlvlmobsCommand;
import iomatix.spigot.rpgleveledmobs.config.SpawnNode;
import iomatix.spigot.rpgleveledmobs.config.cfgModule;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.spawnsController.MobNamesMap;
import iomatix.spigot.rpgleveledmobs.tools.Language;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;

public class RefreshCommand implements RPGlvlmobsCommand {
	public static void RefreshMetaToLevel(LivingEntity livingEntity) {
		Location location = livingEntity.getLocation();
		if (livingEntity.hasMetadata(MetaTag.HealthMod.toString()) && livingEntity.hasMetadata(MetaTag.HealthAddon.toString())
				&& livingEntity.hasMetadata(MetaTag.Level.toString())) {
			final int level = livingEntity.getMetadata(MetaTag.Level.toString()).get(0).asInt();
			final double healthModifier = livingEntity.getMetadata(MetaTag.HealthMod.toString()).get(0).asDouble();
			final double healthAddon = livingEntity.getMetadata(MetaTag.HealthAddon.toString()).get(0).asDouble();
			final double BaseHP = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			final double BaseAdditionalHealth = healthAddon * level + BaseHP * healthModifier * level;

			if (livingEntity.hasMetadata(MetaTag.BaseAdditionalHealth.toString())) {
				livingEntity.removeMetadata(MetaTag.BaseAdditionalHealth.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.BaseAdditionalHealth.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) BaseAdditionalHealth));
			final AttributeModifier HealthMod = new AttributeModifier("RPGMobsHealthMod", BaseAdditionalHealth,
					AttributeModifier.Operation.ADD_NUMBER);
			if (livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers() != null) {
				for (AttributeModifier modifier : livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
						.getModifiers()) {
					livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(modifier);
				}
			}
			livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(HealthMod);
			livingEntity.setHealth(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

			final SpawnNode node = cfgModule.getConfigModule().getSpawnNode(location);
			if (node == null) {
				return;
			}
			String startName = livingEntity.getCustomName();

			if (livingEntity.hasMetadata(MetaTag.CustomName.toString()))
				startName = livingEntity.getMetadata(MetaTag.CustomName.toString()).get(0).asString();

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
			if (!livingEntity.hasMetadata(MetaTag.CustomName.toString()))
				livingEntity.setMetadata(MetaTag.CustomName.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) startName));

			if (node.isPrefixEnabled()) {
				startName = ChatColor.translateAlternateColorCodes('&',
						node.getPrefixFormat().replace("#", level + "") + " " + ChatColor.WHITE + startName);
			}
			if (node.isSuffixEnabled()) {
				startName = ChatColor.translateAlternateColorCodes('&',
						startName + " " + node.getSuffixFormat().replace("#", level + ""));
			}
			if (startName != null && startName.length() > 1 && (node.isSuffixEnabled() || node.isPrefixEnabled())) {
				livingEntity.setCustomName(startName);
			}
			livingEntity.setCustomNameVisible(node.isAlwaysShowMobName());
		}else {
		livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
		LoadMobMetaData(livingEntity, CreatureSpawnEvent.SpawnReason.DEFAULT);
		}
	}

	public static void LoadTheMetaData(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.RPGmob.toString()) && livingEntity.hasMetadata(MetaTag.Level.toString())
				&& livingEntity.hasMetadata(MetaTag.ExpMod.toString())
				&& livingEntity.hasMetadata(MetaTag.MoneyDrop.toString())
				&& livingEntity.hasMetadata(MetaTag.MoneyMod.toString())) {
			final int level = livingEntity.getMetadata(MetaTag.Level.toString()).get(0).asInt();
			final double moneyMod = livingEntity.getMetadata(MetaTag.MoneyMod.toString()).get(0).asDouble();
			final double moneyDrop = livingEntity.getMetadata(MetaTag.MoneyDrop.toString()).get(0).asDouble();
			final double ExpMod = livingEntity.getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble();
			if (!(level > 0) || !(ExpMod > 0) || !(moneyDrop > 0) || !(moneyMod > 0)) {
				livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
				LoadMobMetaData(livingEntity, CreatureSpawnEvent.SpawnReason.DEFAULT);
				return;
			}
			RefreshMetaToLevel(livingEntity);
		} else {
			if (livingEntity.hasMetadata(MetaTag.RPGmob.toString()))
				livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
			LoadMobMetaData(livingEntity, CreatureSpawnEvent.SpawnReason.DEFAULT);
		}
	}

	public static void LoadMobMetaData(LivingEntity livingEntity, CreatureSpawnEvent.SpawnReason SpawnReason) {
		EntityType entityType = livingEntity.getType();
		Location location = livingEntity.getLocation();
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

				if (livingEntity.hasMetadata(MetaTag.ArenaExpMod.toString())) {
					livingEntity.removeMetadata(MetaTag.ArenaExpMod.toString(), (Plugin) Main.RPGMobs);
				}
				livingEntity.setMetadata(MetaTag.ArenaExpMod.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
								(Object) (node.getMobArenaMultiplier() * node.getExperienceMultiplier())));
			} else {
				if (livingEntity.hasMetadata(MetaTag.ExpMod.toString())) {
					livingEntity.removeMetadata(MetaTag.ExpMod.toString(), (Plugin) Main.RPGMobs);
				}
				livingEntity.setMetadata(MetaTag.ExpMod.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
								(Object) node.getExperienceMultiplier()));
				if (livingEntity.hasMetadata(MetaTag.ExpAddon.toString())) {
					livingEntity.removeMetadata(MetaTag.ExpAddon.toString(), (Plugin) Main.RPGMobs);
				}
				livingEntity.setMetadata(MetaTag.ExpAddon.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
								(Object) node.getExperienceAddon()));
			}
		}
		int level = node.getLevel(location);
		if (Main.RPGMobs.getExperienceScalingModuleInstance().isSkillApiHandled()) {
			if (livingEntity instanceof Tameable) {
				if (((Tameable) livingEntity).getOwner() != null) {
					if (SkillAPI.getPlayerData((OfflinePlayer) ((Tameable) livingEntity).getOwner()) != null) {
						PlayerData playerData = SkillAPI
								.getPlayerData((OfflinePlayer) ((Tameable) livingEntity).getOwner());

						int levelSKILLAPI = playerData.hasClass() ? playerData.getMainClass().getLevel() : 0;
						if (levelSKILLAPI > 0) {
							level = levelSKILLAPI;
						}
					}
				}
			}
		}
		if (livingEntity.hasMetadata(MetaTag.RPGmob.toString())) {
			livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.RPGmob.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) true));
		if (livingEntity.hasMetadata(MetaTag.Level.toString())) {
			livingEntity.removeMetadata(MetaTag.Level.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.Level.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) level));
		if (node.isDamageModified()) {
			if (livingEntity.hasMetadata(MetaTag.DamageMod.toString())) {
				livingEntity.removeMetadata(MetaTag.DamageMod.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.DamageMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getDamageMultiplier()));
			if (livingEntity.hasMetadata(MetaTag.DamageAddon.toString())) {
				livingEntity.removeMetadata(MetaTag.DamageAddon.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.DamageAddon.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getDamageAddon()));

		}
		if (node.isDefenseModified()) {
			if (livingEntity.hasMetadata(MetaTag.DefenseMod.toString())) {
				livingEntity.removeMetadata(MetaTag.DefenseMod.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.DefenseMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
							(Object) node.getDefenseMultiplier()));
			if (livingEntity.hasMetadata(MetaTag.DefenseAddon.toString())) {
				livingEntity.removeMetadata(MetaTag.DefenseAddon.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.DefenseAddon.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getDefenseAddon()));
		}
		if (node.isMoneyModified()) {
			if (livingEntity.hasMetadata(MetaTag.MoneyRandomizer.toString())) {
				livingEntity.removeMetadata(MetaTag.MoneyRandomizer.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.MoneyRandomizer.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getMoneyRandomizer()));
			if (livingEntity.hasMetadata(MetaTag.MoneyDrop.toString())) {
				livingEntity.removeMetadata(MetaTag.MoneyDrop.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.MoneyDrop.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
							(Object) node.getMoneyMob(livingEntity.getType())));
			if (livingEntity.hasMetadata(MetaTag.MoneyMod.toString())) {
				livingEntity.removeMetadata(MetaTag.MoneyMod.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.MoneyMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getMoneyMultiplier()));
		}
		if (node.isHealthModified()) {

			final double startMaxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			final double healthMultiplier = node.getHealthMultiplier();
			final double healthAddon = node.getHealthAddon();
			final double newHealthMod = healthAddon*level + startMaxHealth*level*healthMultiplier;
			if (!livingEntity.hasMetadata(MetaTag.HealthMod.toString()))
				livingEntity.setMetadata(MetaTag.HealthMod.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) healthMultiplier));
			if (!livingEntity.hasMetadata(MetaTag.BaseAdditionalHealth.toString()))
				livingEntity.setMetadata(MetaTag.BaseAdditionalHealth.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) newHealthMod));
			final AttributeModifier HealthMod = new AttributeModifier("RPGMobsHealthMod", newHealthMod,
					AttributeModifier.Operation.ADD_NUMBER);
			if (livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers() != null) {
				for (AttributeModifier modifier : livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
						.getModifiers()) {
					livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(modifier);
				}
			}
			livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(HealthMod);
			livingEntity.setHealth(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		}
		String startName = livingEntity.getCustomName();

		if (livingEntity.hasMetadata(MetaTag.CustomName.toString()))
			startName = livingEntity.getMetadata(MetaTag.CustomName.toString()).get(0).asString();

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
		if (!livingEntity.hasMetadata(MetaTag.CustomName.toString()))
			livingEntity.setMetadata(MetaTag.CustomName.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) startName));

		if (!slime && node.isPrefixEnabled()) {
			startName = ChatColor.translateAlternateColorCodes('&',
					node.getPrefixFormat().replace("#", level + "") + " " + ChatColor.WHITE + startName);
		}
		if (!slime && node.isSuffixEnabled()) {
			startName = ChatColor.translateAlternateColorCodes('&',
					startName + " " + node.getSuffixFormat().replace("#", level + ""));
		}
		if (!slime && startName != null && startName.length() > 1
				&& (node.isSuffixEnabled() || node.isPrefixEnabled())) {
			livingEntity.setCustomName(startName);
		}
		livingEntity.setCustomNameVisible(node.isAlwaysShowMobName());

	}

	public static boolean execute() {
		int refreshed = 0;
		int worlds = 0;
		boolean worldRefresh = false;
		for (final World world : Bukkit.getWorlds()) {
			worldRefresh = false;
			for (final LivingEntity ent : world.getLivingEntities()) {
				LoadTheMetaData(ent);
				if (worldRefresh == false) {
					worldRefresh = true;
					++worlds;
				}
				++refreshed;
			}
		}
		LogsModule.info("refreshed " + ChatColor.GOLD + refreshed + ChatColor.GRAY + " mobs in " + ChatColor.AQUA
				+ worlds + ChatColor.GRAY + " worlds.");
		return true;

	}

	@Override
	public boolean execute(final CommandSender sender, final ArrayList<String> args) {
		int refreshed = 0;
		int worlds = 0;
		boolean worldRefresh = false;
		for (final World world : Bukkit.getWorlds()) {
			worldRefresh = false;
			for (final LivingEntity ent : world.getLivingEntities()) {
				LoadTheMetaData(ent);
				if (worldRefresh == false) {
					worldRefresh = true;
					++worlds;
				}
				++refreshed;
			}
		}
		LogsModule.info("refreshed " + ChatColor.GOLD + refreshed + ChatColor.GRAY + " mobs in " + ChatColor.AQUA
				+ worlds + ChatColor.GRAY + " worlds.");
		return true;

	}

	@Override
	public String getCommandLabel() {
		return "refresh";
	}

	@Override
	public String getFormattedCommand() {
		return "Refresh Mobs";
	}

	@Override
	public String getDescription() {
		return "Refresh all existing mobs to their current metadata settings - fix the mobs and convert non-leveled ones to the RPGmobs.";
	}

	@Override
	public String getUsage() {
		return "/RPGmobs Refresh";
	}

	@Override
	public String getFormattedUsage() {
		return ChatColor.GRAY + "/RPGmobs " + ChatColor.GREEN + "Refresh";
	}
}
