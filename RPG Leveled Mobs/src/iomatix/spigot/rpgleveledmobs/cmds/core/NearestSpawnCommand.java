package iomatix.spigot.rpgleveledmobs.cmds.core;

import java.util.ArrayList;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import iomatix.spigot.rpgleveledmobs.cmds.RPGlvlmobsCommand;
import iomatix.spigot.rpgleveledmobs.config.SpawnNode;
import iomatix.spigot.rpgleveledmobs.config.cfgModule;

public class NearestSpawnCommand implements RPGlvlmobsCommand {

	private static final String label = "closestspawn";
	NumberFormat formatter;

	public NearestSpawnCommand() {
		this.formatter = new DecimalFormat("#0.00");
	}

	@Override
	public boolean execute(final CommandSender sender, final ArrayList<String> args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You must be a player to use this command");
			return false;
		}
		if (!cfgModule.getConfigModule().getConfig(((Player) sender).getWorld()).isEnabled()) {
			sender.sendMessage(ChatColor.RED + "Current World Disabled");
			return true;
		}
		final SpawnNode node = cfgModule.getConfigModule().getSpawnNode(((Player) sender).getLocation());
		final String worldName = cfgModule.getConfigModule().getConfig(((Player) sender).getWorld()).getWorldName();
		sender.sendMessage(ChatColor.GREEN + worldName + " - " + ChatColor.YELLOW + node.getName() + " - "
				+ ChatColor.BLUE + this.formatter.format(node.getLocation().distance(((Player) sender).getLocation()))
				+ " Blocks");
		return true;
	}

	@Override
	public String getCommandLabel() {
		return "closestspawn";
	}

	@Override
	public String getFormattedCommand() {
		return "Nearest Spawn Node";
	}

	@Override
	public String getDescription() {
		return "Shows coordination points on the closest leveled mobs spawn.";
	}

	@Override
	public String getUsage() {
		return "/RPGmobs ClosestSpawn";
	}

	@Override
	public String getFormattedUsage() {
		return ChatColor.GRAY + "/RPGmobs " + ChatColor.GREEN + "ClosestSpawn";
	}

}
