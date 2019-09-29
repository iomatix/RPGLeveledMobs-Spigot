package iomatix.spigot.rpgleveledmobs.mobscaling;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.EconomyException;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Town;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;

import iomatix.spigot.rpgleveledmobs.events.RPGMobsGainMoney;
import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;
import net.md_5.bungee.api.ChatColor;

public class MoneyScalingModule {
	private boolean moneyModuleOnline = false;
	private boolean townyModuleOnline = false;
	private Economy economy = null;

	public MoneyScalingModule() {
		if (Bukkit.getPluginManager().isPluginEnabled("Vault")) {
			LogsModule.info("Found Vault, Enabling Vault Money Module.");
			this.moneyModuleOnline = true;
			new VaultHandler();
			if (Bukkit.getPluginManager().isPluginEnabled("Towny") && MoneyScalingModule.this.economy != null) {
				LogsModule.info("Found Towny, Enabling Towny Economy Module.");
				this.townyModuleOnline = true;
			}
		}
	}

	private class VaultHandler implements Listener {

		public VaultHandler() {
			Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
			RegisteredServiceProvider<Economy> economyProvider = Bukkit.getServicesManager()
					.getRegistration(net.milkbowl.vault.economy.Economy.class);
			try {
				MoneyScalingModule.this.economy = economyProvider.getProvider();
				LogsModule.info("Economy connected to: " + ChatColor.GREEN + MoneyScalingModule.this.economy.getName());
			} catch (NullPointerException e) {
				LogsModule.warning("No Vault supported economy plugin found. Vault Money Module is disabled.");
				MoneyScalingModule.this.moneyModuleOnline = false;
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		public void onEntityDeath(final EntityDeathEvent event) throws NotRegisteredException, EconomyException {
			if (economy != null && event.getEntity().hasMetadata(MetaTag.RPGmob.toString())
					&& event.getEntity().hasMetadata(MetaTag.MoneyDrop.toString())) {
				final int level = event.getEntity().getMetadata(MetaTag.Level.toString()).get(0).asInt();
				final double moneyMod = event.getEntity().getMetadata(MetaTag.MoneyMod.toString()).get(0).asDouble();
				final double moneyValue = event.getEntity().getMetadata(MetaTag.MoneyDrop.toString()).get(0).asDouble();
				double moneyRandomizer = Math
						.abs(event.getEntity().getMetadata(MetaTag.MoneyRandomizer.toString()).get(0).asDouble());
				if (moneyRandomizer != 0)
					moneyRandomizer = Math.random() * (moneyRandomizer - (-moneyRandomizer)) + (-moneyRandomizer);
				final double theRandomizer = moneyRandomizer + (moneyRandomizer * moneyMod * level / 25);
				final Double theMoney = (double) Math
						.round(((moneyValue + theRandomizer + (moneyValue * level * moneyMod))) * 100) / 100;
				if (theMoney > 0) {
					if (Main.RPGMobs.getConfigModule().getGlobalConfig().isNoMoneyDrop()) {
						if (event.getEntity().getKiller() != null && event.getEntity().getKiller() instanceof Player)
							ThePayment((Player) event.getEntity().getKiller(), theMoney, 1);
					} else {
						final ItemStack moneyItem;
						if (theMoney > 2750) {
							moneyItem = new ItemStack(Material.DIAMOND_BLOCK);
						} else if (theMoney > 1000) {
							moneyItem = new ItemStack(Material.GOLD_BLOCK);
						} else if (theMoney > 750) {
							moneyItem = new ItemStack(Material.EMERALD_BLOCK);
						} else if (theMoney > 300) {
							moneyItem = new ItemStack(Material.DIAMOND);
						} else if (theMoney > 200) {
							moneyItem = new ItemStack(Material.GOLD_INGOT);
						} else if (theMoney > 100) {
							moneyItem = new ItemStack(Material.IRON_INGOT);
						} else if (theMoney > 50) {
							moneyItem = new ItemStack(Material.EMERALD);
						} else if (theMoney > 10) {
							moneyItem = new ItemStack(Material.GOLD_NUGGET);
						} else if (theMoney > 1) {
							moneyItem = new ItemStack(Material.GHAST_TEAR);
						} else {
							moneyItem = new ItemStack(Material.IRON_NUGGET);
						}

						ItemMeta meta = moneyItem.getItemMeta();
						meta.setDisplayName(ChatColor.GOLD + theMoney.toString() + " " + getCurrencyName(false));
						meta.setLore(Arrays.asList(
								"Worth: " + ChatColor.GOLD + theMoney.toString() + " " + getCurrencyName(false),
								ChatColor.GOLD + "RPGLeveledMobs Dropped money.", "", theMoney.toString()));
						moneyItem.setItemMeta(meta);
						event.getEntity().getWorld().dropItemNaturally(event.getEntity().getLocation().add(0, 0, 1.35),
								moneyItem);
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		public void onPickup(final EntityPickupItemEvent ev) {
			if (ev.getEntityType() == EntityType.PLAYER) {
				try {
					final Item item = ev.getItem();
					String iName = ChatColor.stripColor(item.getItemStack().getItemMeta().getDisplayName());
					if ((" " + getCurrencyName(false)).equals(
							iName.substring(iName.length() - (1 + getCurrencyName(false).length()), iName.length()))) {
						ev.setCancelled(true);
						item.remove();
						final double theMoney = Double.parseDouble(iName.replaceAll(" " + getCurrencyName(false), ""));

						Player thePlayer = Bukkit.getPlayerExact(ev.getEntity().getName());

						ThePayment(thePlayer, theMoney, 1);

					}
				} catch (Exception e) {
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		public void onInventoryTouch(final InventoryClickEvent ev) {
			if (ev.getWhoClicked() instanceof Player) {
				try {

					final ItemStack stack = ev.getCurrentItem();
					final int itemAmount = stack.getAmount();
					String iName = ChatColor.stripColor(stack.getItemMeta().getDisplayName());
					if ((" " + getCurrencyName(false)).equals(
							iName.substring(iName.length() - (1 + getCurrencyName(false).length()), iName.length()))) {
						ev.setCancelled(true);
						ev.getInventory().remove(stack);
						final double theMoney = Double.parseDouble(iName.replaceAll(" " + getCurrencyName(false), ""));

						Player thePlayer = Bukkit.getPlayerExact(ev.getWhoClicked().getName());
						ThePayment(thePlayer, theMoney, itemAmount);

					}
				} catch (Exception e) {
				}
			}
		}

		public void ThePayment(Player who, double money, int multiplier)
				throws NotRegisteredException, EconomyException {
			double tempMoney = money * multiplier;
			if (tempMoney > 0) {
				if (isTownyModuleOnline()) {

					Resident resident = TownyAPI.getInstance().getDataSource().getResident(who.getName());
					if (resident.hasTown()) {
						final Double TownyRatio = Math
								.abs(Main.RPGMobs.getConfigModule().getGlobalConfig().getTownyRatio());
						if (TownyRatio != 0) {
							final boolean isTownySubtract = Main.RPGMobs.getConfigModule().getGlobalConfig()
									.getisTownySubtract();
							final boolean isTownyNationSupport = Main.RPGMobs.getConfigModule().getGlobalConfig()
									.getisTownyNationSupport();
							Town town = TownyAPI.getInstance().getDataSource().getTown(resident.getTown().toString());

							if (isTownySubtract)
								tempMoney -= money * TownyRatio;
							town.pay(money * TownyRatio, "[RPGLeveledMobs] " + resident.getName() + " got " + money
									+ ". Town got " + TownyRatio * 100 + "%");
							if (isTownyNationSupport && town.hasNation()) {
								Nation nation = town.getNation();
								if (isTownySubtract)
									tempMoney -= money * TownyRatio * TownyRatio;
								nation.pay(money * TownyRatio * TownyRatio, "[RPGLeveledMobs] " + resident.getName()
										+ " got " + money + ". Nation got " + TownyRatio * TownyRatio * 100 + "%");
							}
							tempMoney = (double) (Math.round(tempMoney * 100)) / 100;
						}
					}
				}
				RPGMobsGainMoney gainMoneyEvent = new RPGMobsGainMoney(tempMoney, who, economy);
				Bukkit.getPluginManager().callEvent(gainMoneyEvent);
				if (!(gainMoneyEvent.isCancelled())) {
					gainMoneyEvent.transaction();
					SendMoneyMessageToPlayer(tempMoney, who);
				}
			}
		}
	}

	public void SendMoneyMessageToPlayer(double amount, Player player) {

		player.sendMessage(ChatColor.DARK_GREEN + "You have got " + ChatColor.GOLD + ChatColor.BOLD + amount
				+ ChatColor.GOLD + ChatColor.BOLD + " " + getCurrencyName(false));
		player.playSound(player.getLocation(), Sound.ENTITY_ENDER_EYE_DEATH, 0.8f, 0.9f);
	}

	public boolean isMoneyModuleOnline() {

		return this.moneyModuleOnline;
	}

	public boolean isTownyModuleOnline() {
		return this.townyModuleOnline;
	}

	public String getCurrencyName(boolean isSingular) {
		if (isSingular) {
			return economy.currencyNameSingular();
		}
		return economy.currencyNamePlural();
	}

}
