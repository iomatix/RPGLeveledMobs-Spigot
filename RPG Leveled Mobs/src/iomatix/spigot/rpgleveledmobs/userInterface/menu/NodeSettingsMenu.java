package iomatix.spigot.rpgleveledmobs.userInterface.menu;

import org.bukkit.Material;

import org.bukkit.ChatColor;

import iomatix.spigot.rpgleveledmobs.config.ConfigKey;
import iomatix.spigot.rpgleveledmobs.userInterface.Button;
import iomatix.spigot.rpgleveledmobs.userInterface.MenuInteractionEvent;
import iomatix.spigot.rpgleveledmobs.userInterface.Menu;
import iomatix.spigot.rpgleveledmobs.config.SpawnNode;
import iomatix.spigot.rpgleveledmobs.Main;

public class NodeSettingsMenu extends WorldSettingsMenu {

    final Menu prev;
    
    public NodeSettingsMenu(final SpawnNode node, final Menu prev) {
        this.config = node;
        this.prev = prev;
        this.name = ChatColor.BLUE + node.getName() + ChatColor.WHITE + " - " + ChatColor.YELLOW + "Settings";
        this.statsMenu = new WorldStatsMenu(this);
        this.levelingMenu = new WorldLevelingMenu(this);
        this.spawningMenu = new WorldSpawningMenu(this);
        this.namingMenu = new WorldNamingMenu(this);
        if (Main.isMobArenaLoaded()) {
            this.mobArenaMenu = new WorldMobArenaMenu(this);
        }
        this.createMenu();
        final Button deleteButton = new Button();
        deleteButton.setIcon(Material.RED_WOOL, (short)14);
        deleteButton.setName(ChatColor.RED + "Delete Node");
        deleteButton.setOnPressedListener(new Button.onButtonPressedListener() {
            @Override
            public void onButtonPressed(final MenuInteractionEvent event) {
                node.getWorldConfig().removeNode(node);
                ((WorldNodeMenu.NodeListMenu)prev).removeNode(node);
                prev.ShowMenu(event.getInteractor());
            }
        });
        this.menuMap.put(this.menuMap.size() - 1, deleteButton);
        this.menuMap.get(8).setOnPressedListener(new Button.onButtonPressedListener() {
            @Override
            public void onButtonPressed(final MenuInteractionEvent event) {
                prev.ShowMenu(event.getInteractor());
            }
        });
    }
    
    @Override
    protected void ButtonInheritMod(final Menu menu, final Button button, final ConfigKey key, final int pos) {
        super.ButtonInheritMod(menu, button, key, pos);
        if (this.config.isValueInherited(key)) {
            button.setName(button.getName().substring(0, button.getName().indexOf(40)) + "(" + ChatColor.YELLOW + "World" + ChatColor.WHITE + ")");
        }
    }
}
