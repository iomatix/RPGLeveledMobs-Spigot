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
import iomatix.spigot.rpgleveledmobs.tools.BiasedRandom;
import iomatix.spigot.rpgleveledmobs.tools.Language;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;
import iomatix.spigot.rpgleveledmobs.tools.MobData;

public class RefreshCommand implements RPGlvlmobsCommand {
	public static void RefreshMetaToLevel(LivingEntity livingEntity) {
		Location location = livingEntity.getLocation();

		if (livingEntity.hasMetadata(MetaTag.HealthMod.toString())
				&& livingEntity.hasMetadata(MetaTag.HealthAddon.toString())
				&& livingEntity.hasMetadata(MetaTag.Level.toString())) {
			final int level = livingEntity.getMetadata(MetaTag.Level.toString()).get(0).asInt();
			final double healthModifier = livingEntity.getMetadata(MetaTag.HealthMod.toString()).get(0).asDouble();
			final double healthAddon = livingEntity.getMetadata(MetaTag.HealthAddon.toString()).get(0).asDouble();
			final double BaseHP = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			final double BaseAdditionalHealth = healthAddon * level + BaseHP * healthModifier * level;

			MobData.SetMetaBaseAdditionalHealth(livingEntity, (Object) BaseAdditionalHealth);

			final AttributeModifier HealthMod = new AttributeModifier("RPGMobsHealthMod", BaseAdditionalHealth,
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

			final SpawnNode node = cfgModule.getConfigModule().getSpawnNode(location);
			if (node == null) {
				return;
			}

			String startName = livingEntity.getCustomName();
			startName = MobData.GetMetaCustomName(livingEntity);

			if (startName == null || startName.equals("") || startName.equalsIgnoreCase("null")) {
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

			if (!livingEntity.hasMetadata(MetaTag.CustomName.toString())) {
				MobData.SetMetaCustomName(livingEntity, startName);
			}

			if (node.isPrefixEnabled()) {
				startName = ChatColor.stripColor(startName);
				startName = startName.replaceAll(node.getPrefixFormat().replace("]", "\\]").replace("[", "\\[")
						.replaceAll("&.", "").replace("#", "[0-9]*"), "").replace(" ", "");
				startName = ChatColor.translateAlternateColorCodes('&',
						node.getPrefixFormat().replace("#", level + "") + " " + ChatColor.WHITE + startName);
			}
			if (node.isSuffixEnabled()) {
				startName = ChatColor.stripColor(startName);
				startName = startName.replaceAll(node.getSuffixFormat().replace("]", "\\]").replace("[", "\\[")
						.replaceAll("&.", "").replace("#", "[0-9]*"), "").replace(" ", "");
				startName = ChatColor.translateAlternateColorCodes('&',
						startName + " " + node.getSuffixFormat().replace("#", level + ""));
			}

			if (startName != null && startName.length() > 1 && (node.isSuffixEnabled() || node.isPrefixEnabled())) {
				livingEntity.setCustomName(startName);
			}
			livingEntity.setCustomNameVisible(node.isAlwaysShowMobName());
		} else {
			livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
			MobData.LoadMobMetaData(livingEntity, CreatureSpawnEvent.SpawnReason.DEFAULT);
		}
	}

	public static boolean execute() {
		int refreshed = 0;
		int worlds = 0;
		boolean worldRefresh = false;
		for (final World world : Bukkit.getWorlds()) {
			worldRefresh = false;
			for (final LivingEntity ent : world.getLivingEntities()) {
				MobData.LoadTheMetaData(ent);
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
				MobData.LoadTheMetaData(ent);
				if (worldRefresh == false) {
					worldRefresh = true;
					++worlds;
				}
				++refreshed;
			}
		}
		sender.sendMessage(ChatColor.GOLD + LogsModule.PLUGIN_TITLE + ChatColor.GRAY + " refreshed " + ChatColor.GOLD
				+ refreshed + ChatColor.GRAY + " mobs in " + ChatColor.AQUA + worlds + ChatColor.GRAY + " worlds.");
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
