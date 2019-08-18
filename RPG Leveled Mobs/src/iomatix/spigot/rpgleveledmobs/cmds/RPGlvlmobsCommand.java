package iomatix.spigot.rpgleveledmobs.cmds;

import java.util.ArrayList;
import org.bukkit.command.CommandSender;

public interface RPGlvlmobsCommand {
	boolean execute(final CommandSender p0, final ArrayList<String> p1);

	String getCommandLabel();

	String getFormattedCommand();

	String getDescription();

	String getUsage();

	String getFormattedUsage();
}
