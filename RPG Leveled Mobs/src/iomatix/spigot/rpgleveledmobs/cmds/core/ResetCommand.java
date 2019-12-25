package iomatix.spigot.rpgleveledmobs.cmds.core;

import java.util.Iterator;
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
import iomatix.spigot.rpgleveledmobs.spawnsController.SpawnModule;
import iomatix.spigot.rpgleveledmobs.tools.BiasedRandom;
import iomatix.spigot.rpgleveledmobs.tools.Language;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;
import iomatix.spigot.rpgleveledmobs.tools.MobData;

public class ResetCommand implements RPGlvlmobsCommand {

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
		sender.sendMessage(ChatColor.GOLD + LogsModule.PLUGIN_TITLE + ChatColor.GRAY + " force reseted "
				+ ChatColor.GOLD + refreshed + ChatColor.GRAY + " mobs in " + ChatColor.AQUA + worlds + ChatColor.GRAY
				+ " worlds.");
		return true;
	}

	@Override
	public String getCommandLabel() {
		return "reset";
	}

	@Override
	public String getFormattedCommand() {
		return "Reset Mobs";
	}

	@Override
	public String getDescription() {
		return "Reset all existing mobs to the current nodes settings.";
	}

	@Override
	public String getUsage() {
		return "/RPGmobs reset";
	}

	@Override
	public String getFormattedUsage() {
		return ChatColor.GRAY + "/RPGmobs " + ChatColor.GREEN + "Reset";
	}
}
