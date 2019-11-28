package iomatix.spigot.rpgleveledmobs.mobscaling;

import java.util.LinkedList;

import org.bukkit.entity.Player;
import org.bukkit.entity.Tameable;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.Bukkit;

import com.sucy.skill.api.enums.ExpSource;
import com.sucy.skill.api.event.PlayerExperienceGainEvent;
import com.sucy.skill.SkillAPI;

import iomatix.spigot.rpgleveledmobs.events.RPGMobsGainExperience;
import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.cmds.core.RefreshCommand;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;
import net.Indyuce.mmocore.MMOCore;

public class ExperienceScalingModule {
	private boolean SkillAPIHandled;

	public ExperienceScalingModule() {
		boolean experienceHandled = false;
		SkillAPIHandled = false;
		if (Bukkit.getPluginManager().isPluginEnabled("Heroes")) {
			LogsModule.info("Found Heroes, Enabling Heroes Experience Mod.");
			new HeroesHandler();
			experienceHandled = true;
		}
		if (Bukkit.getPluginManager().isPluginEnabled("SkillAPI")) {
			LogsModule.info("Found SkillAPI, Enabling SkillAPI Experience Mod.");
			new SkillAPIHandler();
			experienceHandled = true;
			SkillAPIHandled = true;
		}
		if (Bukkit.getPluginManager().getPlugin("MMOCore") != null) {
			experienceHandled = true;
			new MMOCoreHandler();
			LogsModule.info("Found MMOCore, Enabling MMOCore Experience Mod.");
		}
		if (!experienceHandled) {
			LogsModule.info("No Leveling Plugins Found, Enabling Vanilla Experience Mod.");
			new VanillaHandler();
		}
	}

	public boolean isSkillApiHandled() {
		return SkillAPIHandled;
	}

	private int handleOrbExp(final Entity ent, final int droppedXp) {
		if (this.isMobExperienceModified(ent)) {
			final double expMod = ent.getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble();
			final double expAddon = ent.getMetadata(MetaTag.ExpAddon.toString()).get(0).asDouble();
			final int level = ent.getMetadata(MetaTag.Level.toString()).get(0).asInt();
			final int moddedExp = (int) Math.floor(droppedXp + expAddon * level + droppedXp * level * expMod);
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
				final double expAddon = event.getEntity().getMetadata(MetaTag.ExpAddon.toString()).get(0).asDouble();
				final int level = event.getEntity().getMetadata(MetaTag.Level.toString()).get(0).asInt();
				final int moddedExp = (int) Math.floor(defDropped + expAddon * level + defDropped * level * expMod);
				event.setDroppedExp(moddedExp);
			}
		}
	}

	private class HeroesHandler implements Listener {
		public HeroesHandler() {
			Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
		}
	}

	private class MMOCoreHandler implements Listener {
		public MMOCoreHandler() {
			Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);
		}
		
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onEntityDeath(final EntityDeathEvent event) {
			 if (event.getEntity().hasMetadata(MetaTag.RPGmob.toString())
					&& event.getEntity().hasMetadata(MetaTag.Level.toString())
					&& (event.getEntity().hasMetadata(MetaTag.ExpMod.toString())
							&& event.getEntity().hasMetadata(MetaTag.ExpAddon.toString()))) {
				Player tempKiller = null;
				if (event.getEntity().getKiller() == null
						&& event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
					final EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) event.getEntity()
							.getLastDamageCause();
					final Entity damager = nEvent.getDamager();
					if (damager != null && damager instanceof Tameable) {
						final Tameable thePet = (Tameable) damager;
						if (thePet.isTamed()) {

							tempKiller = (Player) ((Tameable) damager).getOwner();
						}
						
					}
				} else if (event.getEntity().getKiller() != null) {
					tempKiller = event.getEntity().getKiller();
				}

				final Player killer = tempKiller;
				if (killer != null) {

					final int mobLevel = event.getEntity().getMetadata(MetaTag.Level.toString()).get(0).asInt();
					final double expFinal = mobLevel
							* event.getEntity().getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble();
					final double expAddonFinal = mobLevel
							* event.getEntity().getMetadata(MetaTag.ExpAddon.toString()).get(0).asDouble();
					if (killer.hasMetadata(MetaTag.RecentKill.toString())) {
						((LinkedList) killer.getMetadata(MetaTag.RecentKill.toString()).get(0).value())
								.addLast(expFinal);
					} else {
						final LinkedList<Double> q = new LinkedList<Double>();
						q.addLast(expFinal);
						killer.setMetadata(MetaTag.RecentKill.toString(),
								(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) q));
					}
					if (killer.hasMetadata(MetaTag.RecentKillAddon.toString())) {
						((LinkedList) killer.getMetadata(MetaTag.RecentKillAddon.toString()).get(0).value())
								.addLast(expAddonFinal);
					} else {
						final LinkedList<Double> q = new LinkedList<Double>();
						q.addLast(expAddonFinal);
						killer.setMetadata(MetaTag.RecentKillAddon.toString(),
								(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) q));
					}

				}
			}
		}
		
		@EventHandler(priority = EventPriority.HIGHEST)
		public void onPlayerGainExp(final net.Indyuce.mmocore.api.event.PlayerExperienceGainEvent event) {
			final Player killer = event.getPlayer();
			if ( !killer.hasMetadata(MetaTag.RecentKill.toString())) {
				return;
			}
			try {
				final double expModifier = (double) ((LinkedList) killer.getMetadata(MetaTag.RecentKill.toString())
						.get(0).value()).removeFirst();
				final double expAddon = (double) ((LinkedList) killer.getMetadata(MetaTag.RecentKillAddon.toString())
						.get(0).value()).removeFirst();
				if (expModifier != 0) {

					final double theExp = event.getExperience() + event.getExperience() * expModifier + expAddon;
					RPGMobsGainExperience gainExperienceEvent = new RPGMobsGainExperience(theExp, killer);
					Bukkit.getPluginManager().callEvent(gainExperienceEvent);
					if (!(gainExperienceEvent.isCancelled())) {
						event.setExperience((int) Math.floor(theExp));
					} else {
						event.setCancelled(true);
					}
				}
			} catch (Exception e) {
				return;
			}
			
		}
		
		@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
		public void onPlayerGainLevel(final net.Indyuce.mmocore.api.event.PlayerLevelUpEvent event) {
			final int newLevel = event.getNewLevel();
			final Player who = event.getPlayer();
			for (final World world : Bukkit.getWorlds()) {
				for (final Entity ent : world.getEntities()) {
					if (ent instanceof Tameable) {
						final Tameable thePet = (Tameable) ent;
						if (thePet.isTamed() && thePet.getOwner() != null ) {
							if (thePet.getOwner().getName().equals(who.getName())) {
								if (thePet.hasMetadata(MetaTag.Level.toString())) {
									thePet.removeMetadata(MetaTag.Level.toString(), (Plugin) Main.RPGMobs);
								}
								thePet.setMetadata(MetaTag.Level.toString(),
										(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
												(Object) newLevel));
								RefreshCommand.RefreshMetaToLevel((LivingEntity) thePet);
							}
						}
					}
				}
			}

		}	
		
	}

	private class SkillAPIHandler implements Listener {
		public SkillAPIHandler() {
			Bukkit.getPluginManager().registerEvents((Listener) this, (Plugin) Main.RPGMobs);

		}

		@EventHandler(priority = EventPriority.HIGHEST)
		public void onEntityDeath(final EntityDeathEvent event) {
			if (SkillAPI.getSettings().isUseOrbs()) {
				event.setDroppedExp(
						ExperienceScalingModule.this.handleOrbExp((Entity) event.getEntity(), event.getDroppedExp()));
			} else if (event.getEntity().hasMetadata(MetaTag.RPGmob.toString())
					&& event.getEntity().hasMetadata(MetaTag.Level.toString())
					&& (event.getEntity().hasMetadata(MetaTag.ExpMod.toString())
							&& event.getEntity().hasMetadata(MetaTag.ExpAddon.toString()))) {
				Player tempKiller = null;
				if (event.getEntity().getKiller() == null
						&& event.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
					final EntityDamageByEntityEvent nEvent = (EntityDamageByEntityEvent) event.getEntity()
							.getLastDamageCause();
					final Entity damager = nEvent.getDamager();
					if (damager != null && damager instanceof Tameable) {
						final Tameable thePet = (Tameable) damager;
						if (thePet.isTamed()) {

							tempKiller = (Player) ((Tameable) damager).getOwner();
						}

					}
				} else if (event.getEntity().getKiller() != null) {
					tempKiller = event.getEntity().getKiller();
				}

				final Player killer = tempKiller;
				if (killer != null && killer.hasPermission("skillapi.exp")) {
					if (killer.getGameMode() == GameMode.CREATIVE && SkillAPI.getSettings().isBlockCreative()) {
						return;
					}

					final int mobLevel = event.getEntity().getMetadata(MetaTag.Level.toString()).get(0).asInt();
					final double expFinal = mobLevel
							* event.getEntity().getMetadata(MetaTag.ExpMod.toString()).get(0).asDouble();
					final double expAddonFinal = mobLevel
							* event.getEntity().getMetadata(MetaTag.ExpAddon.toString()).get(0).asDouble();
					if (killer.hasMetadata(MetaTag.RecentKill.toString())) {
						((LinkedList) killer.getMetadata(MetaTag.RecentKill.toString()).get(0).value())
								.addLast(expFinal);
					} else {
						final LinkedList<Double> q = new LinkedList<Double>();
						q.addLast(expFinal);
						killer.setMetadata(MetaTag.RecentKill.toString(),
								(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) q));
					}
					if (killer.hasMetadata(MetaTag.RecentKillAddon.toString())) {
						((LinkedList) killer.getMetadata(MetaTag.RecentKillAddon.toString()).get(0).value())
								.addLast(expAddonFinal);
					} else {
						final LinkedList<Double> q = new LinkedList<Double>();
						q.addLast(expAddonFinal);
						killer.setMetadata(MetaTag.RecentKillAddon.toString(),
								(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs, (Object) q));
					}

				}
			}
		}

		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
		public void onPlayerGainExp(final com.sucy.skill.api.event.PlayerExperienceGainEvent event) {
			final Player killer = event.getPlayerData().getPlayer();
			if (event.getSource() != ExpSource.MOB || !killer.hasMetadata(MetaTag.RecentKill.toString())) {
				return;
			}
			try {
				final double expModifier = (double) ((LinkedList) killer.getMetadata(MetaTag.RecentKill.toString())
						.get(0).value()).removeFirst();
				final double expAddon = (double) ((LinkedList) killer.getMetadata(MetaTag.RecentKillAddon.toString())
						.get(0).value()).removeFirst();
				if (expModifier != 0) {

					final double theExp = event.getExp() + event.getExp() * expModifier + expAddon;
					RPGMobsGainExperience gainExperienceEvent = new RPGMobsGainExperience(theExp, killer);
					Bukkit.getPluginManager().callEvent(gainExperienceEvent);
					if (!(gainExperienceEvent.isCancelled())) {
						event.setExp((int) Math.floor(theExp));
					} else {
						event.setCancelled(true);
					}

				}
			} catch (Exception e) {
				return;
			}
		}

		@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
		public void onPlayerGainLevel(final com.sucy.skill.api.event.PlayerLevelUpEvent event) {
			final int newLevel = event.getLevel();
			final Player who = event.getPlayerData().getPlayer();
			for (final World world : Bukkit.getWorlds()) {
				for (final Entity ent : world.getEntities()) {
					if (ent instanceof Tameable) {
						final Tameable thePet = (Tameable) ent;
						if (thePet.isTamed() && thePet.getOwner() != null ) {
							if (thePet.getOwner().getName().equals(who.getName())) {
								if (thePet.hasMetadata(MetaTag.Level.toString())) {
									thePet.removeMetadata(MetaTag.Level.toString(), (Plugin) Main.RPGMobs);
								}
								thePet.setMetadata(MetaTag.Level.toString(),
										(MetadataValue) new FixedMetadataValue((Plugin) Main.RPGMobs,
												(Object) newLevel));
								RefreshCommand.RefreshMetaToLevel((LivingEntity) thePet);
							}
						}
					}
				}
			}

		}
	}
}
