package iomatix.spigot.rpgleveledmobs.spawnsController;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import com.sucy.skill.SkillAPI;
import com.sucy.skill.api.player.PlayerData;

import org.bukkit.metadata.FixedMetadataValue;

import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.cmds.core.RefreshCommand;
import iomatix.spigot.rpgleveledmobs.cmds.core.ResetCommand;
import iomatix.spigot.rpgleveledmobs.config.SpawnNode;
import iomatix.spigot.rpgleveledmobs.config.cfgModule;
import iomatix.spigot.rpgleveledmobs.tools.Language;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;
import iomatix.spigot.rpgleveledmobs.mobscaling.ExperienceScalingModule;

public class SpawnModule implements Listener {

	public SpawnModule() {
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
	}

	public void LoadTheMetaData(LivingEntity livingEntity) {
		if (livingEntity.hasMetadata(MetaTag.RPGmob.toString()) && livingEntity.hasMetadata(MetaTag.Level.toString())
				&& livingEntity.hasMetadata(MetaTag.ExpMod.toString())
				&& livingEntity.hasMetadata(MetaTag.MoneyDrop.toString())
				&& livingEntity.hasMetadata(MetaTag.MoneyMod.toString())) {
			final int level = livingEntity.getMetadata(MetaTag.Level.toString()).get(0).asInt();
			final double moneyMod = livingEntity.getMetadata(MetaTag.MoneyMod.toString()).get(0).asDouble();
			final double moneyDrop = livingEntity.getMetadata(MetaTag.MoneyDrop.toString()).get(0).asDouble();
			final double ExpMod = livingEntity.getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble();
			if (!(level > 0) || !(ExpMod > 0) || !(moneyDrop > 0) || !(moneyMod > 0)) {
				livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
				LoadMobMetaData(livingEntity, CreatureSpawnEvent.SpawnReason.DEFAULT);
				return;
			}
		} else {
			if (livingEntity.hasMetadata(MetaTag.RPGmob.toString()))
				livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
			LoadMobMetaData(livingEntity, CreatureSpawnEvent.SpawnReason.DEFAULT);
		}
	}

	public void LoadMobMetaData(LivingEntity livingEntity, CreatureSpawnEvent.SpawnReason SpawnReason) {
		final EntityType entityType = livingEntity.getType();
		final Location location = livingEntity.getLocation();
		if (livingEntity.hasMetadata(MetaTag.RPGmob.toString())) {
			return;
		}
		boolean slime = false;
		if (SpawnReason == CreatureSpawnEvent.SpawnReason.SLIME_SPLIT) {
			slime = true;
		}
		final SpawnNode node = cfgModule.getConfigModule().getSpawnNode(location);
		if (node == null) {
			return;
		}
		if (!node.isLeveledSpawners() && SpawnReason == CreatureSpawnEvent.SpawnReason.SPAWNER) {
			return;
		}
		if (node.isBlocked(entityType)) {
			return;
		}
		if (!node.canLevel(entityType)) {
			return;
		}
		if (node.isExperienceModified()) {
			if (node.isArenaLocation(location)) {
				if (!node.isMobArenaLeveled()) {
					return;
				}

				if (livingEntity.hasMetadata(MetaTag.ArenaExpMod.toString())) {
					livingEntity.removeMetadata(MetaTag.ArenaExpMod.toString(), (Plugin) Main.RPGMobs);
				}
				livingEntity.setMetadata(MetaTag.ArenaExpMod.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
								(Object) (node.getMobArenaMultiplier() * node.getExperienceMultiplier())));
			} else {
				if (livingEntity.hasMetadata(MetaTag.ExpMod.toString())) {
					livingEntity.removeMetadata(MetaTag.ExpMod.toString(), (Plugin) Main.RPGMobs);
				}
				livingEntity.setMetadata(MetaTag.ExpMod.toString(),
						(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
								(Object) node.getExperienceMultiplier()));
			}
		}
		int level = node.getLevel(location);
		if (Main.RPGMobs.getExperienceScalingModuleInstance().isSkillApiHandled()) {
			if (livingEntity instanceof Tameable) {
				if (((Tameable) livingEntity).getOwner() != null
						&& ((Player) ((Tameable) livingEntity).getOwner()).hasPermission("skillapi.exp")) {
					PlayerData playerData = SkillAPI
							.getPlayerData((OfflinePlayer) ((Tameable) livingEntity).getOwner());
					int levelSKILLAPI = playerData.hasClass() ? playerData.getMainClass().getLevel() : 0;
					if (levelSKILLAPI > 0) {
						level = levelSKILLAPI;
					}
				}
			}
		}
		if (livingEntity.hasMetadata(MetaTag.RPGmob.toString())) {
			livingEntity.removeMetadata(MetaTag.RPGmob.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.RPGmob.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) true));
		if (livingEntity.hasMetadata(MetaTag.Level.toString())) {
			livingEntity.removeMetadata(MetaTag.Level.toString(), (Plugin) Main.RPGMobs);
		}
		livingEntity.setMetadata(MetaTag.Level.toString(),
				(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) level));
		if (node.isDamageModified()) {
			if (livingEntity.hasMetadata(MetaTag.DamageMod.toString())) {
				livingEntity.removeMetadata(MetaTag.DamageMod.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.DamageMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getDamageMultiplier()));
		}
		if (node.isDefenseModified()) {
			if (livingEntity.hasMetadata(MetaTag.DefenseMod.toString())) {
				livingEntity.removeMetadata(MetaTag.DefenseMod.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.DefenseMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
							(Object) node.getDefenseMultiplier()));
		}
		if (node.isMoneyModified()) {
			if (livingEntity.hasMetadata(MetaTag.MoneyRandomizer.toString())) {
				livingEntity.removeMetadata(MetaTag.MoneyRandomizer.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.MoneyRandomizer.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getMoneyRandomizer()));
			if (livingEntity.hasMetadata(MetaTag.MoneyDrop.toString())) {
				livingEntity.removeMetadata(MetaTag.MoneyDrop.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.MoneyDrop.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
							(Object) node.getMoneyMob(livingEntity.getType())));
			if (livingEntity.hasMetadata(MetaTag.MoneyMod.toString())) {
				livingEntity.removeMetadata(MetaTag.MoneyMod.toString(), (Plugin) Main.RPGMobs);
			}
			livingEntity.setMetadata(MetaTag.MoneyMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) node.getMoneyMultiplier()));
		}
		if (node.isHealthModified()) {
			final double startMaxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
			final double healthMultiplier = node.getHealthMultiplier();
			if (livingEntity.hasMetadata(MetaTag.HealthMod.toString()))
				livingEntity.removeMetadata(MetaTag.HealthMod.toString(), (Plugin) Main.RPGMobs);
			livingEntity.setMetadata(MetaTag.HealthMod.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) healthMultiplier));
			final double NewHealthMod = startMaxHealth * level * healthMultiplier;
			if (livingEntity.hasMetadata(MetaTag.BaseAdditionalHealth.toString()))
				livingEntity.removeMetadata(MetaTag.BaseAdditionalHealth.toString(), (Plugin) Main.RPGMobs);
			livingEntity.setMetadata(MetaTag.BaseAdditionalHealth.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) NewHealthMod));
			final AttributeModifier HealthMod = new AttributeModifier("RPGMobsHealthMod", NewHealthMod,
					AttributeModifier.Operation.ADD_NUMBER);
			if (livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers() != null) {
				for (AttributeModifier modifier : livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
						.getModifiers()) {
					livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(modifier);
				}
			}
			livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).addModifier(HealthMod);
			livingEntity.setHealth(livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
		}

		String startName = livingEntity.getCustomName();
		if (startName == null || startName.toLowerCase().equals("null")) {
			if (node.getMobNameLanguage() != null) {
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
		if (!livingEntity.hasMetadata(MetaTag.CustomName.toString()))
			livingEntity.setMetadata(MetaTag.CustomName.toString(),
					(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) startName));

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

	public void LoadMobsData() {
		for (final World world : Bukkit.getWorlds()) {
			for (final LivingEntity ent : world.getLivingEntities()) {
				LoadTheMetaData(ent);
			}
		}
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMobSpawn(final CreatureSpawnEvent event) {

		LoadMobMetaData(event.getEntity(), event.getSpawnReason());

	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMobTame(final EntityTameEvent event) {
		final LivingEntity tamedEntity = event.getEntity();
		EntityType entityType = tamedEntity.getType();
		Location location = tamedEntity.getLocation();
		if (!tamedEntity.hasMetadata(MetaTag.Level.toString())
				|| !tamedEntity.hasMetadata(MetaTag.BaseAdditionalHealth.toString())) {
			ResetCommand.LoadTheMetaData(tamedEntity);
		} else {
			final SpawnNode node = cfgModule.getConfigModule().getSpawnNode(location);
			if (node == null) {
				return;
			}
			if (node.isHealthModified() && node.canLevel(entityType) && !node.isBlocked(entityType)) {
				Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Main.RPGMobs, new Runnable() {
					@Override
					public void run() {
						if (Main.RPGMobs.getExperienceScalingModuleInstance().isSkillApiHandled()
								&& (event.getOwner() != null)
								&& (((Player) event.getOwner()).hasPermission("skillapi.exp"))) {
							PlayerData playerData = SkillAPI.getPlayerData((OfflinePlayer) event.getOwner());
							int level = playerData.hasClass() ? playerData.getMainClass().getLevel() : 0;
							if (level > 0) {
								if (tamedEntity.hasMetadata(MetaTag.Level.toString()))
									tamedEntity.removeMetadata(MetaTag.Level.toString(), (Plugin) Main.RPGMobs);
								tamedEntity.setMetadata(MetaTag.Level.toString(),
										(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) level));
							}
						}
						if (tamedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getModifiers() != null) {
							for (AttributeModifier modifier : tamedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH)
									.getModifiers()) {
								tamedEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).removeModifier(modifier);
							}
						}
						RefreshCommand.RefreshMetaToLevel(tamedEntity);
					}
				}, 20L);
			}
		}
	}
}
