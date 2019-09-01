package iomatix.spigot.rpgleveledmobs.cmds;

import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.cmds.core.ClearCommand;
import iomatix.spigot.rpgleveledmobs.cmds.core.NearestSpawnCommand;
import iomatix.spigot.rpgleveledmobs.cmds.core.RefreshCommand;
import iomatix.spigot.rpgleveledmobs.cmds.core.ResetCommand;
import iomatix.spigot.rpgleveledmobs.cmds.core.SettingsCommand;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;

public class cmdModule implements CommandExecutor, Listener {
	public static final String COMMAND_TAG = "[CommandModule]";
	private static final HashMap<String, RPGlvlmobsCommand> commandMap;
	private final String HELP_HEADER;
	private final String HELP_FOOTER;
	private final String JSON = "[\"\",{\"text\":\"[\",\"color\":\"white\"},{\"text\":\"?\",\"color\":\"yellow\",\"bold\":true,\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"<hover_command>\\n\",\"color\":\"blue\"},{\"text\":\"<hover_description>\\n\",\"color\":\"yellow\"},{\"text\":\"<hover_usage>\",\"color\":\"yellow\"}]}}},{\"text\":\"]\",\"color\":\"white\",\"bold\":false},{\"text\":\"<suggest_command_text>\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"<suggest_command>\"}}]";

	public cmdModule() {
		this.HELP_HEADER = ChatColor.DARK_GRAY + "[ " + ChatColor.GOLD + "RPG Leveled Mobs " + ChatColor.GREEN + "Help"
				+ ChatColor.DARK_GRAY + " ]";
		this.HELP_FOOTER = ChatColor.DARK_GRAY + "[ " + ChatColor.YELLOW + "? " + ChatColor.WHITE + "for help, "
				+ ChatColor.GREEN + "click " + ChatColor.WHITE + "for command" + ChatColor.DARK_GRAY + " ]";
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
		Main.RPGMobs.getCommand("rpgmobs").setExecutor((CommandExecutor) this);
		this.registerCommand(new SettingsCommand());
		this.registerCommand(new NearestSpawnCommand());
		this.registerCommand(new ClearCommand());
		this.registerCommand(new RefreshCommand());
		this.registerCommand(new ResetCommand());
	}

	public boolean onCommand(final CommandSender commandSender, final Command command, final String label,
			final String[] args) {
		if (!(commandSender instanceof Player)) {
			this.displayBadSenderMessage(commandSender);
			return false;
		}
		if (args.length < 1) {
			this.displayHelp(commandSender);
			return false;
		}
		if (cmdModule.commandMap.containsKey(args[0].toLowerCase())) {
			LogsModule.debug("[CommandModule]Found executed command - " + label + " - Relaying command");
			final ArrayList<String> list = new ArrayList<String>();
			Collections.addAll(list, args);
			list.remove(0);
			return cmdModule.commandMap.get(args[0].toLowerCase()).execute(commandSender, list);
		}
		this.displayHelp(commandSender);
		return false;
	}

	public void registerCommand(final RPGlvlmobsCommand command) {
		LogsModule.debug("[CommandModule]Registered command - " + command.getCommandLabel());
		cmdModule.commandMap.put(command.getCommandLabel(), command);
	}

	public void displayHelp(final CommandSender sender) {
		sender.sendMessage(this.HELP_HEADER);
		for (final String subCommand : cmdModule.commandMap.keySet()) {
			final RPGlvlmobsCommand command = cmdModule.commandMap.get(subCommand);
			String formatted = "[\"\",{\"text\":\"[\",\"color\":\"white\"},{\"text\":\"?\",\"color\":\"yellow\",\"bold\":true,\"hoverEvent\":{\"action\":\"show_text\",\"value\":{\"text\":\"\",\"extra\":[{\"text\":\"<hover_command>\\n\",\"color\":\"blue\"},{\"text\":\"<hover_description>\\n\",\"color\":\"yellow\"},{\"text\":\"<hover_usage>\",\"color\":\"yellow\"}]}}},{\"text\":\"]\",\"color\":\"white\",\"bold\":false},{\"text\":\"<suggest_command_text>\",\"color\":\"gray\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"<suggest_command>\"}}]"
					.replace("<hover_command>", command.getFormattedCommand());
			formatted = formatted.replace("<hover_description>",
					"Description: " + ChatColor.GRAY + command.getDescription());
			formatted = formatted.replace("<hover_usage>", "Usage: " + ChatColor.GRAY + command.getUsage());
			formatted = formatted.replace("<suggest_command>", command.getUsage());
			formatted = formatted.replace("<suggest_command_text>", ChatColor.GRAY + command.getFormattedUsage());
			Bukkit.getServer().dispatchCommand((CommandSender) Bukkit.getConsoleSender(),
					"tellraw " + sender.getName() + " " + formatted);
		}
		sender.sendMessage(this.HELP_FOOTER);
	}

	public void displayBadSenderMessage(final CommandSender sender) {
		LogsModule.warning("The command is available only for players.");
	}

	static {
		commandMap = new HashMap<String, RPGlvlmobsCommand>();
	}
}
