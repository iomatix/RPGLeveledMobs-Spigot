package iomatix.spigot.rpgleveledmobs.userInterface;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SpawnEggMeta;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;

import iomatix.spigot.rpgleveledmobs.config.RPGLeveledMobsConfig;

public class MobButton extends Button {
    private static final HashMap<EntityType, String> nameMap;
    
    public MobButton(final String EntityName, final EntityType type, final RPGLeveledMobsConfig config, final Menu menu) {
        this.addLoreLine("");
        if (config.canLevel(type)) {
            this.addLoreLine(ChatColor.GREEN + "Leveled");
        }
        else {
            this.addLoreLine(ChatColor.RED + "Not Leveled");
        }
        

        try { this.setIcon(Material.getMaterial(type.toString()+"_SPAWN_EGG")); }
        catch(Exception e){
                this.setIcon(Material.getMaterial("BAT_SPAWN_EGG")); 
    		}
            
            final SpawnEggMeta meta = (SpawnEggMeta)this.getItemStack().getItemMeta();
            meta.setDisplayName(ChatColor.GOLD + EntityName);
            final ArrayList<String> lore = new ArrayList<String>();
            lore.add("");
            if (config.canLevel(type)) {
                lore.add(ChatColor.GREEN + "Leveled");
            }
            else {
                lore.add(ChatColor.RED + "Not Leveled");
            }
            meta.setLore((List)lore);
   
            if (type == EntityType.GIANT) {
                this.setIcon(Material.ZOMBIE_SPAWN_EGG);
            }
            this.getItemStack().setItemMeta((ItemMeta)meta);
        
        this.setOnPressedListener(new onButtonPressedListener() {
            @Override
            public void onButtonPressed(final MenuInteractionEvent event) {
                if (config.canLevel(type)) {
                    config.removeLeveledMob(type);
                }
                else {
                    config.addLeveledMob(type);
                }
                menu.ShowMenu(event.getInteractor());
            }
        });
    }
    
    public MobButton(final String EntityName, final EntityType type, final RPGLeveledMobsConfig config, final Menu menu, final boolean blocked) {
        this.setName(ChatColor.YELLOW + EntityName);
        this.addLoreLine("");
        if (!config.isBlocked(type)) {
            this.addLoreLine(ChatColor.GREEN + "Allowed");
        }
        else {
            this.addLoreLine(ChatColor.RED + "Blocked");
        }

   
        try { this.setIcon(Material.getMaterial(type.toString()+"_SPAWN_EGG")); }
        catch(Exception e){
                this.setIcon(Material.getMaterial("BAT_SPAWN_EGG")); 
    		}
            final SpawnEggMeta meta = (SpawnEggMeta)this.getItemStack().getItemMeta();
            meta.setDisplayName(ChatColor.YELLOW + EntityName);
            final ArrayList<String> lore = new ArrayList<String>();
            lore.add("");
            if (!config.isBlocked(type)) {
                lore.add(ChatColor.GREEN + "Allowed");
            }
            else {
                lore.add(ChatColor.RED + "Not Blocked");
            }
            meta.setLore((List)lore);
            if (type == EntityType.GIANT) {
                this.setIcon(Material.ZOMBIE_SPAWN_EGG);
            }
            this.getItemStack().setItemMeta((ItemMeta)meta);
      
        this.setOnPressedListener(new onButtonPressedListener() {
            @Override
            public void onButtonPressed(final MenuInteractionEvent event) {
                if (config.isBlocked(type)) {
                    config.removeBlockedMob(type);
                }
                else {
                    config.addBlockedMob(type);
                }
                menu.ShowMenu(event.getInteractor());
            }
        });
    }
    
    static {
        nameMap = new HashMap<EntityType, String>();
    }
}
