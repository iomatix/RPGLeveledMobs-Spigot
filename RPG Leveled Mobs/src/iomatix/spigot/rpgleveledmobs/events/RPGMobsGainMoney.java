package iomatix.spigot.rpgleveledmobs.events;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import iomatix.spigot.rpgleveledmobs.Main;
import net.milkbowl.vault.economy.Economy;
public class RPGMobsGainMoney extends Event implements Cancellable{


    private Double Money;
    private Player Who;
    private static final HandlerList HANDLERS_LIST = new HandlerList();
    private boolean isCancelled;
    private Economy economy;

    public boolean isValutON() {
    return Main.RPGMobs.getMoneyScalingModuleInstance().isMoneyModuleOnline();
    }
    
    public RPGMobsGainMoney(double money,Player who, Economy economyHandler)
    {
    	this.Money = money;
    	this.Who = who;
    	this.economy = economyHandler;
    	this.isCancelled = false;	
    }
    
    @Override
    public boolean isCancelled() {
        return isCancelled;
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
    public Double getMoney() {
        return this.Money;
    }
    public void transaction() {
    	economy.depositPlayer((OfflinePlayer) this.Who, this.Money); //to event
    }

}
