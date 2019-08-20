package iomatix.spigot.rpgleveledmobs.userInterface;

import java.util.HashMap;

import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.plugin.Plugin;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.event.Listener;

import iomatix.spigot.rpgleveledmobs.Main;

public class MenuHandler implements Listener {
	
    private static HashMap<Inventory, Menu> menuHashMap;
    private static HashMap<Player, Inventory> playerListener;
    
    public MenuHandler() {
        Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.RPGMobs);
    }
    
    public void ListenToMenu(final Player player, final Inventory inv, final Menu menu) {
        if (MenuHandler.playerListener.containsKey(player)) {
            final Inventory inventory = MenuHandler.playerListener.remove(player);
            MenuHandler.menuHashMap.remove(inv);
        }
        MenuHandler.playerListener.put(player, inv);
        MenuHandler.menuHashMap.put(inv, menu);
    }
    
    public void closeMenu(final Player player) {
        final Inventory inv = MenuHandler.playerListener.remove(player);
        if (inv != null) {
            MenuHandler.menuHashMap.remove(inv);
        }
        player.closeInventory();
    }
    
    public static boolean isBusy() {
        return !MenuHandler.playerListener.isEmpty();
    }
    
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onInventoryInteraction(final InventoryClickEvent event) {
        if (MenuHandler.menuHashMap.containsKey(event.getClickedInventory())) {
            if (event.getRawSlot() > 53) {
                event.setCancelled(true);
            }
            try {
                event.setCancelled(true);
                MenuHandler.menuHashMap.get(event.getClickedInventory()).OnMenuInteraction(new MenuInteractionEvent(event, MenuHandler.menuHashMap.get(event.getClickedInventory())));
            }
            catch (MenuException e) {
                e.printStackTrace();
            }
        }
    }
    
    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onPickupItemEvent(final InventoryPickupItemEvent event) {
        if (MenuHandler.menuHashMap.containsKey(event.getInventory())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onDragItemEvent(final InventoryDragEvent event) {
        if (MenuHandler.menuHashMap.containsKey(event.getInventory())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = false, priority = EventPriority.LOWEST)
    public void onItemMoveEvent(final InventoryMoveItemEvent event) {
        if (MenuHandler.menuHashMap.containsKey(event.getDestination()) || MenuHandler.menuHashMap.containsKey(event.getInitiator()) || MenuHandler.menuHashMap.containsKey(event.getSource())) {
            event.setCancelled(true);
        }
    }
    
    @EventHandler(ignoreCancelled = false, priority = EventPriority.MONITOR)
    public void onInventoryCloseEvent(final InventoryCloseEvent event) {
        MenuHandler.menuHashMap.remove(event.getInventory());
        MenuHandler.playerListener.remove(event.getPlayer());
    }
    
    static {
        MenuHandler.menuHashMap = new HashMap<Inventory, Menu>();
        MenuHandler.playerListener = new HashMap<Player, Inventory>();
    }
}
