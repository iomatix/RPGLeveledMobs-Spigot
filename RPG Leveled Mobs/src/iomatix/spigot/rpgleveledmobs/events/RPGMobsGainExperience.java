package iomatix.spigot.rpgleveledmobs.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import iomatix.spigot.rpgleveledmobs.Main;

public class RPGMobsGainExperience extends Event implements Cancellable {

    private Double Experience;
    private Player Who;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    
    
    public boolean isSkillAPIModuleON() {
    return Main.RPGMobs.getExperienceScalingModuleInstance().isSkillApiHandled();
    }
    
    public RPGMobsGainExperience(double exp,Player who)
    {
    	this.Experience = exp;
    	this.Who = who;
    	this.isCancelled = false;	
    }
    
    @Override
    public boolean isCancelled() {
        return this.isCancelled;
    }
    
    @Override
    public void setCancelled(boolean cancelled) {
        this.isCancelled = cancelled;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS_LIST;
    }
    
    public static HandlerList getHandlerList() {
        return HANDLERS_LIST;
    }
    
    
    
    public Player getEntity() {
        return this.Who;
    }
    public Double getExp() {
        return this.Experience;
    }
}
