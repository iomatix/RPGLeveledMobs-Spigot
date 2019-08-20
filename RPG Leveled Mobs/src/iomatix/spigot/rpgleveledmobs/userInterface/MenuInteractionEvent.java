package iomatix.spigot.rpgleveledmobs.userInterface;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.entity.Player;

public class MenuInteractionEvent {

	private int pressedPosition;
	private Player interactor;
	private Menu menu;
	private ClickType clickType;

	public MenuInteractionEvent(final InventoryClickEvent event, final Menu menu) {
		this.pressedPosition = event.getRawSlot();
		this.interactor = (Player) event.getWhoClicked();
		this.menu = menu;
		this.clickType = event.getClick();
	}

	public int getPositionPressed() {
		return this.pressedPosition;
	}

	public Player getInteractor() {
		return this.interactor;
	}

	public Menu getMenu() {
		return this.menu;
	}

	public ClickType getClickType() {
		return this.clickType;
	}
}
