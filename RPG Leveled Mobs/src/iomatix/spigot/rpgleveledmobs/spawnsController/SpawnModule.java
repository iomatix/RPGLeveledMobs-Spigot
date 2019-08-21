package iomatix.spigot.rpgleveledmobs.spawnsController;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.metadata.FixedMetadataValue;

import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.config.SpawnNode;
import iomatix.spigot.rpgleveledmobs.config.cfgModule;
import iomatix.spigot.rpgleveledmobs.tools.Language;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;

public class SpawnModule implements Listener {

	public SpawnModule() {
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMobSpawn(final CreatureSpawnEvent event) {

		final LivingEntity livingEntity = event.getEntity();
		if (event.getEntity().hasMetadata(MetaTag.RPGmob.toString())) {
			return;
		}
		boolean slime = false;
		if (event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
			slime = true;
		}
		final SpawnNode node = cfgModule.getConfigModule().getSpawnNode(event.getLocation());
		if (node == null) {
			return;
		}
		if (!node.isLeveledSpawners() && event.getSpawnReason() == CreatureSpawnEvent.SpawnReason.SPAWNER) {
			return;
		}
		if (node.isBlocked(event.getEntityType())) {
			return;
		}
		if (!node.canLevel(event.getEntityType())) {
			return;
		}
		if (node.isExperienceModified()) {
			if (node.isArenaLocation(event.getLocation())) {
				if (!node.isMobArenaLeveled()) {
					return;
				}
				livingEntity.setMetadata(MetaTag.ArenaExpMod.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
								(Object) (node.getMobArenaMultiplier() * node.getExperienceMultiplier())));
			} else {
				livingEntity.setMetadata(MetaTag.ExpMod.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
								(Object) node.getExperienceMultiplier()));
			}
		}
		final int level = node.getLevel(event.getLocation());
		livingEntity.setMetadata(MetaTag.RPGmob.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) true));
		livingEntity.setMetadata(MetaTag.Level.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) level));
		if (node.isDamageModified()) {
			livingEntity.setMetadata(MetaTag.DamageMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getDamageMultiplier()));
		}
		if (node.isDefenseModified()) {
			livingEntity.setMetadata(MetaTag.DefenseMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
							(Object) node.getDefenseMultiplier()));
		}	
		if (node.isMoneyModified()) {
			
			double money = node.getMoneyMob(livingEntity.getType());
			if (node.getMoneyRandomizer() != 0.0) {
				money = Math.abs(node.getMoneyRandomizer());
				money = Math.random() * (money - (-money)) + (-money);
				money = node.getMoneyMob(livingEntity.getType())+money;
			}
			livingEntity.setMetadata(MetaTag.MoneyDrop.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) money));
			livingEntity.setMetadata(MetaTag.MoneyMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getMoneyMultiplier()));
		}
		if (node.isHealthModified()) {
			final double startMaxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			final double newMaxHealth = startMaxHealth + startMaxHealth * level * node.getHealthMultiplier();
			livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(newMaxHealth);
			livingEntity.setHealth(newMaxHealth);
		}
		String startName = livingEntity.getCustomName();
		if (startName == null || startName.toLowerCase().equals("null")) {
			if (node.getMobNameLanguage() != Language.ENGLISH) {
				if (MobNamesMap.getMobName(node.getMobNameLanguage(), livingEntity.getType()) != null) {
					startName = ChatColor.WHITE
							+ MobNamesMap.getMobName(node.getMobNameLanguage(), livingEntity.getType());
				} else {
					startName = "";
				}
			} else {
				startName = livingEntity.getName();
			}
		}
		if (!slime && node.isPrefixEnabled()) {
			startName = ChatColor.translateAlternateColorCodes('&',
					node.getPrefixFormat().replace("#", level + "") + " " + ChatColor.WHITE + startName);
		}
		if (!slime && node.isSuffixEnabled()) {
			startName = ChatColor.translateAlternateColorCodes('&',
					startName + " " + node.getSuffixFormat().replace("#", level + ""));
		}
		if (!slime && startName != null && startName.length() > 1
				&& (node.isSuffixEnabled() || node.isPrefixEnabled())) {
			livingEntity.setCustomName(startName);
		}
		livingEntity.setCustomNameVisible(node.isAlwaysShowMobName());
	}

}
