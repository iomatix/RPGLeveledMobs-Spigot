package iomatix.spigot.rpgleveledmobs.tools;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.cmds.core.RefreshCommand;

public class AutomaticRefreshListener implements Listener {
	
	public AutomaticRefreshListener()
	{
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
	}
	public void AutomaticRefresh() {
	if(Bukkit.getOnlinePlayers().isEmpty() || Bukkit.getOnlinePlayers().size() < 2) RefreshCommand.execute();	
	}
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void onPlayerJoinRefresh(final PlayerJoinEvent event) {
		AutomaticRefresh();
	}

}
