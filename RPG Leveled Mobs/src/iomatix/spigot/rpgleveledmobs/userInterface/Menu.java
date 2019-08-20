package iomatix.spigot.rpgleveledmobs.userInterface;

import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

import org.bukkit.inventory.InventoryHolder;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.entity.Player;

import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.config.cfgModule;

public abstract class Menu {
	public HashMap<Integer, Button> menuMap;
	protected static MenuHandler menuHandler;
	protected String name;

	public Menu() {
		this.menuMap = new HashMap<Integer, Button>();
	}

	public void OnMenuInteraction(final MenuInteractionEvent event) throws MenuException {
		final Button button = this.menuMap.get(event.getPositionPressed());
		if (button != null) {
			button.buttonPressed(event);
		}
	}

	public void ShowMenu(final Player player) {
		final Inventory menu = this.GenerateMenu(player);
		Menu.menuHandler.ListenToMenu(player, menu, this);
		Bukkit.getScheduler().runTask(Main.RPGMobs, () -> {
			player.openInventory(menu);
		});
	}

	protected Inventory GenerateMenu(final Player player) {
		if (cfgModule.version <= 1.8 && this.name.length() > 32) {
			this.name = this.name.substring(0, 32);
		}
		Inventory menu;
		if (this.name != null) {
			menu = Bukkit.createInventory((InventoryHolder) player, this.getInventorySize(), this.name);
		} else {
			menu = Bukkit.createInventory((InventoryHolder) player, this.getInventorySize());
		}
		for (final int pos : this.menuMap.keySet()) {
			if (pos > 53) {
				break;
			}
			menu.setItem(pos, this.menuMap.get(pos).getItemStack());
		}
		return menu;
	}

	protected final int getInventorySize() {
		final ArrayList<Integer> positions = new ArrayList<Integer>(this.menuMap.keySet());
		Collections.sort(positions);
		if (positions.isEmpty()) {
			return 9;
		}
		final int greatestPos = positions.get(positions.size() - 1);
		if (greatestPos >= 45) {
			return 54;
		}
		if (greatestPos >= 36) {
			return 45;
		}
		if (greatestPos >= 27) {
			return 36;
		}
		if (greatestPos >= 18) {
			return 27;
		}
		if (greatestPos >= 9) {
			return 18;
		}
		return 9;
	}

	public String getName() {
		return this.name;
	}

	static {
		Menu.menuHandler = new MenuHandler();
	}
}
