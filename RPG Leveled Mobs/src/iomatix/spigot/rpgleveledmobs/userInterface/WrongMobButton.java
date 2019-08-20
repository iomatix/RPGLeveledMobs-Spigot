package iomatix.spigot.rpgleveledmobs.userInterface;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public class WrongMobButton extends Button {
	   public WrongMobButton(final String name, final String Version) {
	        this.setIcon(Material.BARRIER);
	        this.setName(ChatColor.YELLOW + name + ChatColor.WHITE + " - " + ChatColor.RED + "Disabled");
	        this.addLoreLine("");
	        this.addLoreLine(ChatColor.RED + "Requires MC " + Version);
}
}