package iomatix.spigot.rpgleveledmobs.userInterface.menu;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.entity.Player;
import org.bukkit.Material;
import org.bukkit.ChatColor;

import iomatix.spigot.rpgleveledmobs.userInterface.Button;
import iomatix.spigot.rpgleveledmobs.userInterface.MenuException;
import iomatix.spigot.rpgleveledmobs.userInterface.MenuInteractionEvent;
import iomatix.spigot.rpgleveledmobs.userInterface.Menu;
import iomatix.spigot.rpgleveledmobs.config.cfgModule;
import iomatix.spigot.rpgleveledmobs.config.WorldConfig;
import iomatix.spigot.rpgleveledmobs.Main;

public class BaseSettingMenu extends Menu {
	public BaseSettingMenu(final cfgModule configModule) {
		this.name =ChatColor.DARK_GRAY + "["+ChatColor.GOLD+"RPGMobs"+ChatColor.DARK_GRAY +"] " + ChatColor.DARK_GRAY + "Settings";
		final Button globalButton = new Button();
		globalButton.setIcon(Material.ENCHANTED_BOOK);
		globalButton.setName(ChatColor.GREEN + "Global Config");		
		globalButton.addLoreLine(
				ChatColor.WHITE + "Configure " + ChatColor.GOLD + "Global " + ChatColor.WHITE + "settings.");
		globalButton.addLoreLine(" ");
		globalButton.addLoreLine(ChatColor.GRAY + "The default settings used");
		globalButton.addLoreLine(ChatColor.GRAY + "for every world if not specifically set.");
		globalButton.setOnPressedListener(new Button.onButtonPressedListener() {
			@Override
			public void onButtonPressed(final MenuInteractionEvent event) {
				final SettingsMenu globalMenu = new SettingsMenu(Main.RPGMobs.getConfigModule().getGlobalConfig());
				globalMenu.ShowMenu(event.getInteractor());
			}
		});
		this.menuMap.put(0, globalButton);
		int position = 1;
		for (final WorldConfig config : configModule.getWorldConfigs()) {
			final Button configButton = new Button();
			configButton.setIcon(Material.WRITABLE_BOOK);
			configButton.setName(
					ChatColor.GOLD + this.formatWorldName(config.getWorld().getName()) + ChatColor.GREEN + " Config");
			if (config.isEnabled()) {
				configButton.addLoreLine(ChatColor.WHITE + "Enabled: " + ChatColor.GREEN + "True");
			} else {
				configButton.addLoreLine(ChatColor.WHITE + "Enabled: " + ChatColor.RED + "False");
			}
			configButton.setOnPressedListener(new Button.onButtonPressedListener() {
				@Override
				public void onButtonPressed(final MenuInteractionEvent event) {
					if (event.getClickType() == ClickType.RIGHT) {
						config.setEnabled(!config.isEnabled());
						configButton.removeLore();
						if (config.isEnabled()) {
							configButton.addLoreLine(ChatColor.WHITE + "Enabled: " + ChatColor.GREEN + "True");
						} else {
							configButton.addLoreLine(ChatColor.WHITE + "Enabled: " + ChatColor.RED + "False");
						}
						configButton.addLoreLine("");
						configButton.addLoreLine(
								ChatColor.GRAY + "Left click to configure, Right click to toggle enabled.");
						BaseSettingMenu.this.ShowMenu(event.getInteractor());
					} else {
						new WorldSettingsMenu(config).ShowMenu(event.getInteractor());
					}
				}
			});
			configButton.addLoreLine("");
			configButton.addLoreLine(ChatColor.GRAY + "Left click to configure, Right click to toggle enabled.");
			this.menuMap.put(position++, configButton);
		}
		final int exitLoc = this.getInventorySize() - 1;
		final Button exitButton = new Button();
		exitButton.setIcon(Material.RED_DYE);
		exitButton.setName(ChatColor.RED + "Close");
		exitButton.setOnPressedListener(new Button.onButtonPressedListener() {
			@Override
			public void onButtonPressed(final MenuInteractionEvent event) {
				event.getInteractor().closeInventory();
			}
		});
		this.menuMap.put(exitLoc, exitButton);
	}

	@Override
	public void OnMenuInteraction(final MenuInteractionEvent event) throws MenuException {
		super.OnMenuInteraction(event);
	}

	@Override
	public void ShowMenu(final Player player) {
		super.ShowMenu(player);
	}

	public String formatWorldName(final String name) {
		String formattedName = "";
		for (final String part : name.split("_")) {
			formattedName = formattedName + part.substring(0, 1).toUpperCase() + part.substring(1) + " ";
		}
		return formattedName.trim();
	}
}
