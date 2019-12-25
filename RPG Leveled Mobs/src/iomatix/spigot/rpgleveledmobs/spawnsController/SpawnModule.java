package iomatix.spigot.rpgleveledmobs.spawnsController;

import java.util.List;
import java.lang.Math;
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
import iomatix.spigot.rpgleveledmobs.tools.BiasedRandom;
import iomatix.spigot.rpgleveledmobs.tools.Language;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;
import iomatix.spigot.rpgleveledmobs.mobscaling.ExperienceScalingModule;

import iomatix.spigot.rpgleveledmobs.tools.MobData;

public class SpawnModule implements Listener {

	public SpawnModule() {
		Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMobSpawn(final CreatureSpawnEvent event) {
		MobData.LoadMobMetaData(event.getEntity(), event.getSpawnReason());
	}

	@EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
	public void onMobTame(final EntityTameEvent event) {
		final LivingEntity tamedEntity = event.getEntity();
		EntityType entityType = tamedEntity.getType();
		Location location = tamedEntity.getLocation();
		if (!tamedEntity.hasMetadata(MetaTag.Level.toString())
				|| !tamedEntity.hasMetadata(MetaTag.BaseAdditionalHealth.toString())) {
			MobData.LoadTheMetaData(tamedEntity);
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
								&& SkillAPI.getPlayerData((OfflinePlayer) event.getOwner()) != null) {
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
