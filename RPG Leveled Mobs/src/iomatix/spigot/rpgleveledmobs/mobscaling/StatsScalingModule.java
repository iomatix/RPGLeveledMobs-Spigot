package iomatix.spigot.rpgleveledmobs.mobscaling;

import org.bukkit.event.EventPriority;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.entity.Entity;
import org.bukkit.Bukkit;

import com.sucy.skill.api.event.PhysicalDamageEvent; 
import com.sucy.skill.api.event.SkillDamageEvent; 

import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.tools.MetaTag;
import iomatix.spigot.rpgleveledmobs.logging.LogsModule;

public class StatsScalingModule {

    public StatsScalingModule() {
        boolean scalingModified = false;
        if (Bukkit.getPluginManager().getPlugin("Heroes") != null) {
        	scalingModified = true;
            new HeroesHandler();
            LogsModule.info("Found Heroes, Enabling Heroes Scaling Mod.");
        }
        if (Bukkit.getPluginManager().getPlugin("SkillAPI") != null) {
        	scalingModified = true;
            new SkillAPIHandler();
            LogsModule.info("Found SkillAPI, Enabling SkillAPI Scaling Mod.");
        }
        if (!scalingModified) {
            new VanillaHandler();
            LogsModule.info("Enabling Vanilla Scaling Mod.");
        }
    }
    
    public void onLeveledMobShoot(final Entity ent, final Entity proj) {
        if (this.isDamageModded(ent)) {
            proj.setMetadata(MetaTag.RPGmob.toString(), (MetadataValue)ent.getMetadata(MetaTag.RPGmob.toString()).get(0));
            proj.setMetadata(MetaTag.Level.toString(), (MetadataValue)ent.getMetadata(MetaTag.Level.toString()).get(0));
            proj.setMetadata(MetaTag.DamageMod.toString(), (MetadataValue)ent.getMetadata(MetaTag.DamageMod.toString()).get(0));
        }
    }
    
    public boolean isDamageModded(final Entity ent) {
        return ent != null && ent.hasMetadata(MetaTag.RPGmob.toString()) && ent.hasMetadata(MetaTag.Level.toString()) && ent.hasMetadata(MetaTag.DamageMod.toString());
    }
    
    public boolean isDefenseModded(final Entity ent) {
        return ent != null && ent.hasMetadata(MetaTag.RPGmob.toString()) && ent.hasMetadata(MetaTag.Level.toString()) && ent.hasMetadata(MetaTag.DefenseMod.toString());
    }
    
    public int getLevel(final Entity ent) {
        return ent.getMetadata(MetaTag.Level.toString()).get(0).asInt();
    }
    
    public double getDamageMod(final Entity ent) {
        return ent.getMetadata(MetaTag.DamageMod.toString()).get(0).asDouble();
    }
    public double getDefenseMod(final Entity ent) {
        return ent.getMetadata(MetaTag.DefenseMod.toString()).get(0).asDouble();
    }
    
    
    private class VanillaHandler implements Listener
    {
        public VanillaHandler() {
            Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.RPGMobs);
        }
        
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onEntityDamage(final EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Projectile && ((Projectile)event.getDamager()).getShooter() instanceof Monster) {
            	StatsScalingModule.this.onLeveledMobShoot((Entity)((Projectile)event.getDamager()).getShooter(), event.getDamager());
            }
            if (event.getDamager().hasMetadata(MetaTag.RPGmob.toString()) && event.getDamager().hasMetadata(MetaTag.DamageMod.toString())) {
                final double damageMod = StatsScalingModule.this.getDamageMod(event.getDamager());
                final int level = StatsScalingModule.this.getLevel(event.getDamager());
                event.setDamage(event.getDamage() + event.getDamage() * damageMod * level);
            }  
            
            if(event.getEntity().hasMetadata(MetaTag.RPGmob.toString()) && event.getEntity().hasMetadata(MetaTag.DefenseMod.toString())) {
            	final double defenseMod = StatsScalingModule.this.getDefenseMod(event.getEntity());
            	final int level = StatsScalingModule.this.getLevel(event.getEntity());
            	final double output = event.getDamage() - event.getDamage() * level * defenseMod;
            	if(output < 1) event.setDamage(1);
            	else event.setDamage(output);
        }
    }
   }
    
    private class HeroesHandler implements Listener
    {
        public HeroesHandler() {
            Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.RPGMobs);
        }
        
        @EventHandler(ignoreCancelled = true, priority = EventPriority.MONITOR)
        public void onDamage(final EntityDamageByEntityEvent event) {
            if (event.getDamager() instanceof Projectile && StatsScalingModule.this.isDamageModded((Entity)((Projectile)event.getDamager()).getShooter())) {
            	StatsScalingModule.this.onLeveledMobShoot((Entity)((Projectile)event.getDamager()).getShooter(), event.getDamager());
            }
            if (StatsScalingModule.this.isDamageModded(event.getDamager())) {
                final int level = StatsScalingModule.this.getLevel(event.getDamager());
                final double damageMod = StatsScalingModule.this.getDamageMod(event.getDamager());
                final double damage = event.getDamage();
                event.setDamage(damage + damage * level * damageMod);
            }
            if (StatsScalingModule.this.isDefenseModded(event.getEntity())) {
                final int level = StatsScalingModule.this.getLevel(event.getEntity());
                final double defenseMod = StatsScalingModule.this.getDefenseMod(event.getEntity());
                final double damage = event.getDamage();
                final double output = damage - damage * level * defenseMod/100;
            	if(output < 1) event.setDamage(1);
            	else event.setDamage(output);
            }
            
        }
    }
    
    private class SkillAPIHandler implements Listener
    {
        public SkillAPIHandler() {
            Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.RPGMobs);
        }
        
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onPhysicalDamage(final PhysicalDamageEvent event) {
            if (StatsScalingModule.this.isDamageModded((Entity)event.getDamager())) {
                final int level = StatsScalingModule.this.getLevel((Entity)event.getDamager());
                final double damageMod = StatsScalingModule.this.getDamageMod((Entity)event.getDamager());
                event.setDamage(event.getDamage() + level * damageMod * event.getDamage());
            }
            if (StatsScalingModule.this.isDefenseModded((Entity)event.getTarget())) {
                final int level = StatsScalingModule.this.getLevel((Entity)event.getTarget());
                final double defenseMod = StatsScalingModule.this.getDefenseMod((Entity)event.getTarget());
                final double output = event.getDamage() - level * defenseMod * event.getDamage(); 
            	if(output < 1) event.setDamage(1);
            	else event.setDamage(output);
            } 
            
            
        }
        @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
        public void onSkillDamage(final SkillDamageEvent event) {
        	 if (StatsScalingModule.this.isDefenseModded((Entity)event.getTarget())) {
                 final int level = StatsScalingModule.this.getLevel((Entity)event.getTarget());
                 final double defenseMod = StatsScalingModule.this.getDefenseMod((Entity)event.getTarget());
                 final double output = event.getDamage() - level * defenseMod * event.getDamage(); 
             	if(output < 1) event.setDamage(1);
             	else event.setDamage(output);
             } 
        }
        }
 }

