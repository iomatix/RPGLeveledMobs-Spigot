package iomatix.spigot.rpgleveledmobs.mobscaling;

import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.GameMode;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Bukkit;

import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import com.sucy.skill.SkillAPI;

import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;

public class ExperienceScalingModule {
	public ExperienceScalingModule() {
		boolean experienceHandled = false;
		if (Bukkit.getPluginManager().isPluginEnabled("Heroes")) {
			LogsModule.info("Found Heroes, Enabling Heroes Experience Mod.");
			new HeroesHandler();
			experienceHandled = true;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("SkillAPI")) {
			LogsModule.info("Found SkillAPI, Enabling SkillAPI Experience Mod.");
			new SkillAPIHandler();
			experienceHandled = true;
		}
		if (!experienceHandled) {
			LogsModule.info("No Leveling Plugins Found, Enabling Vanilla Experience Mod.");
			new VanillaHandler();
		}
	}

	private int handleOrbExp(final Entity ent, final int droppedXp) {
		if (this.isMobExperienceModified(ent)) {
			final double expMod = ent.getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble();
			final int level = ent.getMetadata(MetaTag.Level.toString()).get(0).asInt();
			final int moddedExp = (int) Math.floor(droppedXp + droppedXp * level * expMod);
			return moddedExp;
		}
		return droppedXp;
	}

	private boolean isMobExperienceModified(final Entity ent) {
		return ent.hasMetadata(MetaTag.RPGmob.toString()) && ent.hasMetadata(MetaTag.Level.toString())
				&& ent.hasMetadata(MetaTag.ExpMod.toString());
	}

	private class VanillaHandler implements Listener {
		public VanillaHandler() {
			Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
		}

		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		public void onEntityDeath(final EntityDeathEvent event) {
			if (ExperienceScalingModule.this.isMobExperienceModified((Entity) event.getEntity())) {
				final int defDropped = event.getDroppedExp();
				final double expMod = event.getEntity().getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble();
				final int level = event.getEntity().getMetadata(MetaTag.Level.toString()).get(0).asInt();
				final int moddedExp = (int) Math.floor(defDropped + defDropped * level * expMod);
				event.setDroppedExp(moddedExp);
			}
		}
	}

	private class HeroesHandler implements Listener {
		public HeroesHandler() {
			Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
		}
	}

	private class SkillAPIHandler implements Listener {
		public SkillAPIHandler() {
			Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onEntityDeath(final EntityDeathEvent event) {
			if (SkillAPI.getSettings().isUseOrbs()) {
				event.setDroppedExp(ExperienceScalingModule.this.handleOrbExp((Entity) event.getEntity(), event.getDroppedExp()));
			} else if (event.getEntity().hasMetadata(MetaTag.RPGmob.toString())
					&& event.getEntity().hasMetadata(MetaTag.Level.toString())
					&& event.getEntity().hasMetadata(MetaTag.ExpMod.toString())) {
				Player tempKiller = null;
				if (event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
					final EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) event.getEntity().getLastDamageCause();
		            final Entity damager = nEvent.getDamager();
		            if(damager != null && damager instanceof Tameable ) {
		            	final Tameable thePet = (Tameable) damager;
		            	if(thePet.isTamed()) {

		            		tempKiller = (Player)((Tameable)damager).getOwner();
		            	}
		            	
		            }
				}else if (event.getEntity().getKiller() != null)
				{
					tempKiller = event.getEntity().getKiller();
				}
				
				final Player killer = tempKiller;
				if (killer != null && killer.hasPermission("skillapi.exp")) {
					if (killer.getGameMode() == GameMode.CREATIVE && SkillAPI.getSettings().isBlockCreative()) {
						return;
					}
					if (killer.hasMetadata(MetaTag.RecentKill.toString())) {
						((LinkedList) killer.getMetadata(MetaTag.RecentKill.toString()).get(0).value())
								.addLast(event.getEntity().getMetadata(MetaTag.Level.toString()).get(0).asInt()
										* event.getEntity().getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble());
					} else {
						final LinkedList<Double> q = new LinkedList<Double>();
						q.addLast(event.getEntity().getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble()
								* event.getEntity().getMetadata(MetaTag.Level.toString()).get(0).asInt());
						killer.setMetadata(MetaTag.RecentKill.toString(),
								(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) q));
					}
				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		public void onPlayerGainExp(final PlayerExperienceGainEvent event) {
			final Player killer = event.getPlayerData().getPlayer();
			if (event.getSource() != ExpSource.MOB || !killer.hasMetadata(MetaTag.RecentKill.toString())) {
				return;
			}
			try {
			final double expModifier = (double) ((LinkedList) killer.getMetadata(MetaTag.RecentKill.toString()).get(0).value()).removeFirst();
			if (expModifier != 0) event.setExp((int) Math.floor(event.getExp() + event.getExp() * expModifier));
			}catch(Exception e) { return;}
		}
	}
}
