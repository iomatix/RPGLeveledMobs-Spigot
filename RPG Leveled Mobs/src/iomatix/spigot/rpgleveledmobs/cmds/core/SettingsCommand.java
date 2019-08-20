package iomatix.spigot.rpgleveledmobs.cmds.core;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.command.CommandSender;

import iomatix.spigot.rpgleveledmobs.userInterface.MenuHandler;
import iomatix.spigot.rpgleveledmobs.userInterface.menu.BaseSettingMenu;
import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.cmds.RPGlvlmobsCommand;

public class SettingsCommand implements RPGlvlmobsCommand {

	private static final String label = "settings";

	@Override
	public boolean execute(final CommandSender sender, final ArrayList<String> args) {
		if (!(sender instanceof Player)) {
			return false;
		}
		if (MenuHandler.isBusy()) {
			sender.sendMessage(
					ChatColor.RED + "Busy! " + ChatColor.WHITE + "Someone else is modifying settings, please wait.");
			return true;
		}
		final BaseSettingMenu menu = new BaseSettingMenu(Main.RPGMobs.getConfigModule());
		menu.ShowMenu((Player) sender);
		return true;
	}

	@Override
	public String getCommandLabel() {
		return "settings";
	}

	@Override
	public String getFormattedCommand() {
		return "Settings";
	}

	@Override
	public String getDescription() {
		return "Opens an User Interface to configure plugin settings.";
	}

	@Override
	public String getUsage() {
		return "/RPGmobs Settings";
	}

	@Override
	public String getFormattedUsage() {
		return ChatColor.GRAY + "/RPGmobs " + ChatColor.GREEN + "Settings";
	}

}
