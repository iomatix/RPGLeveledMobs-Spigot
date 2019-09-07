package iomatix.spigot.rpgleveledmobs.cmds.core;

import java.util.Iterator;
import java.util.ArrayList;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.LivingEntity;
import org.bukkit.World;
import org.bukkit.Bukkit;

import iomatix.spigot.rpgleveledmobs.cmds.RPGlvlmobsCommand;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;

public class ClearCommand implements RPGlvlmobsCommand {
	@Override
	public boolean execute(final CommandSender sender, final ArrayList<String> args) {
		int removed = 0;
		int worlds = 0;
		for (final World world : Bukkit.getWorlds()) {
			boolean removedHere = false;
			for (final LivingEntity ent : world.getLivingEntities()) {
				if (ent.hasMetadata(MetaTag.RPGmob.toString())) {
					removedHere = true;
					++removed;
					ent.remove();
				}
			}
			if (removedHere) {
				++worlds;
			}
		}
		sender.sendMessage(ChatColor.GOLD + LogsModule.PLUGIN_TITLE + ChatColor.GRAY + " removed " + ChatColor.GOLD
				+ removed + ChatColor.GRAY + " mobs in " + ChatColor.AQUA + worlds + ChatColor.GRAY + " worlds.");
		return true;
	}

	@Override
	public String getCommandLabel() {
		return "clear";
	}

	@Override
	public String getFormattedCommand() {
		return "Clear";
	}

	@Override
	public String getDescription() {
		return "Removes all existing leveled mobs.";
	}

	@Override
	public String getUsage() {
		return "/RPGmobs Clear";
	}

	@Override
	public String getFormattedUsage() {
		return ChatColor.GRAY + "/RPGmobs " + ChatColor.GREEN + "Clear";
	}
}
