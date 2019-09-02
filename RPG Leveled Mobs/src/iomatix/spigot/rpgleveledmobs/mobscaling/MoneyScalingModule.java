package iomatix.spigot.rpgleveledmobs.mobscaling;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

import iomatix.spigot.rpgleveledmobs.events.RPGMobsGainMoney;
import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;
import net.md_5.bungee.api.ChatColor;

public class MoneyScalingModule {
	private boolean moneyModuleOnline = false;

	public MoneyScalingModule() {
		if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			LogsModule.info("Found Vault, Enabling Vault Money Mod.");
			new VaultHandler();
			this.moneyModuleOnline = true;
		}
	}

	private class VaultHandler implements Listener {
		public Economy economy = null;

		public VaultHandler() {
			Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
			RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager()
					.getRegistration(net.milkbowl.vault.economy.Economy.class);
			this.economy = economyProvider.getProvider();
		}

		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		public void onEntityDeath(final EntityDeathEvent event) {
			if (economy != null && event.getEntity().hasMetadata(MetaTag.RPGmob.toString())
					&& event.getEntity().hasMetadata(MetaTag.MoneyDrop.toString())) {
				final int level = event.getEntity().getMetadata(MetaTag.Level.toString()).get(0).asInt();
				final int moneyMod = event.getEntity().getMetadata(MetaTag.MoneyMod.toString()).get(0).asInt();
				final double moneyValue = event.getEntity().getMetadata(MetaTag.MoneyDrop.toString()).get(0).asDouble();
				double moneyRandomizer = Math.abs(event.getEntity().getMetadata(MetaTag.MoneyRandomizer.toString()).get(0).asDouble());
				if(moneyRandomizer != 0) moneyRandomizer = Math.random() * (moneyRandomizer - (-moneyRandomizer)) + (-moneyRandomizer);
				final double theRandomizer = moneyRandomizer + moneyRandomizer *((level*moneyMod)/2);	
				final Double theMoney = (double) Math.round(((moneyValue+ theRandomizer + (moneyValue * level * moneyMod))) * 100) / 100;
				if (theMoney > 0) {
					final ItemStack moneyItem = new ItemStack(Material.GOLD_NUGGET);
					ItemMeta meta = moneyItem.getItemMeta();
					meta.setDisplayName(ChatColor.GOLD + theMoney.toString() + "G.");
					meta.setLore(Arrays.asList(ChatColor.GOLD + theMoney.toString() + "G",
							ChatColor.GOLD + "RPGLeveledMobs Dropped money.", "", theMoney.toString()));
					moneyItem.setItemMeta(meta);
					event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation().add(0, 0, 1.35),
							moneyItem);
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		public void onPickup(final EntityPickupItemEvent ev) {
			if (ev.getEntityType() == EntityType.PLAYER) {
				try {
				final Item item = ev.getItem();
				String iName = ChatColor.stripColor(item.getItemStack().getItemMeta().getDisplayName());
					if ("G.".equals(iName.substring(iName.length() - 2, iName.length()))) {
						ev.setCancelled(true);
						final double theMoney = Double.parseDouble(iName.replaceAll("G.", ""));
						economy.depositPlayer((OfflinePlayer) ev.getEntity(), theMoney);
						Player thePlayer = Bukkit.getPlayerExact(ev.getEntity().getName());
						thePlayer.sendMessage(ChatColor.DARK_GREEN + "You have found " + ChatColor.GOLD + ChatColor.BOLD + theMoney + ChatColor.GOLD + ChatColor.BOLD + " coins");
						thePlayer.playSound(ev.getEntity().getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 0.8f, 0.9f);
						
						RPGMobsGainMoney gainMoneyEvent = new RPGMobsGainMoney(theMoney,(Player)ev.getEntity());
						Bukkit.getPluginManager().callEvent(gainMoneyEvent);
						item.remove();
					}
				} catch (Exception e) {
				}
			}
		}
	}

	public boolean isMoneyModuleOnline() {

		return this.moneyModuleOnline;
	}
}
