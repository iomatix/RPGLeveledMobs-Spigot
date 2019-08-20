package iomatix.spigot.rpgleveledmobs.userInterface.menu;

import java.util.Iterator;
import java.util.ArrayList;

import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.Material;
import org.bukkit.ChatColor;

import iomatix.spigot.rpgleveledmobs.config.ConfigKey;
import iomatix.spigot.rpgleveledmobs.userInterface.Button;
import iomatix.spigot.rpgleveledmobs.userInterface.MenuException;
import iomatix.spigot.rpgleveledmobs.userInterface.MenuInteractionEvent;
import iomatix.spigot.rpgleveledmobs.userInterface.Menu;
import iomatix.spigot.rpgleveledmobs.config.cfgModule;
import iomatix.spigot.rpgleveledmobs.config.WorldConfig;
import iomatix.spigot.rpgleveledmobs.config.SpawnNode;
import iomatix.spigot.rpgleveledmobs.config.RPGLeveledMobsConfig;
import iomatix.spigot.rpgleveledmobs.Main;

public class WorldSettingsMenu extends SettingsMenu{
	   public WorldSettingsMenu(final RPGLeveledMobsConfig config) {
	        this.config = config;
	        this.name = ChatColor.BLUE + this.formatWorldName(((WorldConfig)config).getWorld().getName()) + " " + ChatColor.YELLOW + "Settings";
	        this.statsMenu = new WorldStatsMenu(this);
	        this.levelingMenu = new WorldLevelingMenu(this);
	        this.spawningMenu = new WorldSpawningMenu(this);
	        this.namingMenu = new WorldNamingMenu(this);
	        if (Main.isMobArenaLoaded()) {
	            this.mobArenaMenu = new WorldMobArenaMenu(this);
	        }
	        this.createMenu();
	        for (int i = this.menuMap.size() - 1; i >= 0; --i) {
	            if (this.menuMap.get(i) != null) {
	                this.menuMap.put(i + 1, this.menuMap.get(i));
	            }
	        }
	        final Button enable = new Button();
	        enable.setName(ChatColor.GREEN + "World Enabled");
	        if (((WorldConfig)config).isEnabled()) {
	            enable.setIcon(Material.GREEN_WOOL);
	            enable.addLoreLine(ChatColor.WHITE + "Enabled: " + ChatColor.GREEN + "True");
	        }
	        else {
	            enable.setIcon(Material.RED_WOOL);
	            enable.addLoreLine(ChatColor.WHITE + "Enabled: " + ChatColor.RED + "False");
	        }
	        enable.setOnPressedListener(new Button.onButtonPressedListener() {
	            @Override
	            public void onButtonPressed(final MenuInteractionEvent event) {
	                final WorldConfig wc = (WorldConfig)config;
	                wc.setEnabled(!wc.isEnabled());
	                enable.removeLore();
	                if (((WorldConfig)config).isEnabled()) {
	                    enable.setIcon(Material.GREEN_WOOL);
	                    enable.addLoreLine(ChatColor.WHITE + "Enabled: " + ChatColor.GREEN + "True");
	                }
	                else {
	                    enable.setIcon(Material.RED_WOOL);
	                    enable.addLoreLine(ChatColor.WHITE + "Enabled: " + ChatColor.RED + "False");
	                }
	                enable.addLoreLine("");
	                enable.addLoreLine(ChatColor.GRAY + "Click to Toggle.");
	                WorldSettingsMenu.this.ShowMenu(event.getInteractor());
	            }
	        });
	        enable.addLoreLine("");
	        enable.addLoreLine(ChatColor.GRAY + "Click to Toggle.");
	        this.menuMap.put(0, enable);
	        final Button nodes = new Button();
	        nodes.setIcon(Material.COMPASS);
	        nodes.setName(ChatColor.GREEN + "Spawn Nodes");
	        final Menu thisMenu = this;
	        nodes.setOnPressedListener(new Button.onButtonPressedListener() {
	            @Override
	            public void onButtonPressed(final MenuInteractionEvent event) {
	                new WorldNodeMenu(thisMenu).ShowMenu(event.getInteractor());
	            }
	        });
	        this.menuMap.put(this.menuMap.size() - 1, nodes);
	    }
	    
	    public WorldSettingsMenu() {
	    }
	    
	    public String formatWorldName(final String name) {
	        String formattedName = "";
	        final int i = 0;
	        for (final String part : name.split("_")) {
	            formattedName = formattedName + part.substring(0, 1).toUpperCase() + part.substring(1) + " ";
	        }
	        return formattedName.trim();
	    }
	    
	    protected void ButtonInheritMod(final Menu menu, final Button button, final ConfigKey key, final int pos) {
	        if (this.config.isValueInherited(key)) {
	            button.setName(button.getName() + " " + ChatColor.WHITE + "(" + ChatColor.YELLOW + "Global" + ChatColor.WHITE + ")");
	        }
	        else {
	            button.removeLastLoreLine();
	            if (ConfigKey.defaultMap.get(key) instanceof Boolean) {
	                button.addLoreLine(ChatColor.GRAY + "Click to Toggle. Right Click to use Global.");
	            }
	            else {
	                button.addLoreLine(ChatColor.GRAY + "Click to Change. Right Click to use Global.");
	            }
	            final Button.onButtonPressedListener oldButtonListener = button.getOnButtonPressedListener();
	            button.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (event.getClickType() == ClickType.RIGHT) {
	                        WorldSettingsMenu.this.config.useInheritedValue(key);
	                        menu.ShowMenu(event.getInteractor());
	                    }
	                    else {
	                        oldButtonListener.onButtonPressed(event);
	                    }
	                }
	            });
	            menu.menuMap.put(pos, button);
	        }
	    }
	    
	    protected class WorldStatsMenu extends StatsMenu
	    {
	        public WorldStatsMenu(final SettingsMenu prev) {
	            super(prev);
	            this.name = prev.getName() + ChatColor.WHITE + " - " + ChatColor.BLUE + "Stats Menu";
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            super.ShowMenu(player);
	        }
	        
	        public void generateMenu() {
	            super.generateMenu();
	            final Button expMod = this.menuMap.get(10);
	            WorldSettingsMenu.this.ButtonInheritMod(this, expMod, ConfigKey.EXPERIENCE_MOD_ENABLED, 10);
	            final Button expMult = this.menuMap.get(19);
	            WorldSettingsMenu.this.ButtonInheritMod(this, expMult, ConfigKey.EXPERIENCE_MODIFIER, 19);
	            final Button healthMod = this.menuMap.get(12);
	            WorldSettingsMenu.this.ButtonInheritMod(this, healthMod, ConfigKey.HEALTH_MOD_ENABLED, 13);
	            final Button healthMult = this.menuMap.get(21);
	            WorldSettingsMenu.this.ButtonInheritMod(this, healthMult, ConfigKey.HEALTH_PER_LEVEL, 22);
	            final Button defenseMod = this.menuMap.get(14);
	            WorldSettingsMenu.this.ButtonInheritMod(this, defenseMod, ConfigKey.DEFENSE_MOD_ENABLE, 13);
	            final Button defenseMult = this.menuMap.get(23);
	            WorldSettingsMenu.this.ButtonInheritMod(this, defenseMult, ConfigKey.DEFENSE_PER_LEVEL, 22);
	            final Button damageMod = this.menuMap.get(16);
	            WorldSettingsMenu.this.ButtonInheritMod(this, damageMod, ConfigKey.DAMAGE_MOD_ENABLE, 16);
	            final Button damageMult = this.menuMap.get(25);
	            WorldSettingsMenu.this.ButtonInheritMod(this, damageMult, ConfigKey.DAMAGE_PER_LEVEL, 25);
	        }
	    }
	    
	    protected class WorldLevelingMenu extends LevelingMenu
	    {
	        public WorldLevelingMenu(final SettingsMenu prev) {
	            super(prev);
	            this.name = prev.getName() + ChatColor.WHITE + " - " + ChatColor.BLUE + "Stats Menu";
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            super.ShowMenu(player);
	        }
	        
	        @Override
	        public void generateMenu() {
	            super.generateMenu();
	            final Button minLevel = this.menuMap.get(0);
	            WorldSettingsMenu.this.ButtonInheritMod(this, minLevel, ConfigKey.MIN_LEVEL, 0);
	            final Button maxLevel = this.menuMap.get(1);
	            WorldSettingsMenu.this.ButtonInheritMod(this, maxLevel, ConfigKey.MAX_LEVEL, 1);
	            final Button startLevel = this.menuMap.get(2);
	            WorldSettingsMenu.this.ButtonInheritMod(this, startLevel, ConfigKey.START_LEVEL, 2);
	            final Button distancePerLevel = this.menuMap.get(3);
	            WorldSettingsMenu.this.ButtonInheritMod(this, distancePerLevel, ConfigKey.DISTANCE_PER_LEVEL, 3);
	        }
	    }
	    
	    protected class WorldNamingMenu extends NamingMenu
	    {
	        public WorldNamingMenu(final SettingsMenu prev) {
	            super(prev);
	            this.name = prev.getName() + ChatColor.WHITE + " - " + ChatColor.BLUE + "Leveling Menu";
	        }
	        
	        @Override
	        public void generateMenu() {
	            super.generateMenu();
	            final Button showName = this.menuMap.get(0);
	            WorldSettingsMenu.this.ButtonInheritMod(this, showName, ConfigKey.ALWAYS_SHOW_MOB_NAME, 0);
	            final Button prefixEnabled = this.menuMap.get(12);
	            WorldSettingsMenu.this.ButtonInheritMod(this, prefixEnabled, ConfigKey.USE_PREFIX, 12);
	            final Button prefixFormat = this.menuMap.get(21);
	            WorldSettingsMenu.this.ButtonInheritMod(this, prefixFormat, ConfigKey.PREFIX_FORMAT, 21);
	            final Button suffixEnabled = this.menuMap.get(14);
	            WorldSettingsMenu.this.ButtonInheritMod(this, suffixEnabled, ConfigKey.USE_SUFFIX, 14);
	            final Button suffixFormat = this.menuMap.get(23);
	            WorldSettingsMenu.this.ButtonInheritMod(this, suffixFormat, ConfigKey.SUFFIX_FORMAT, 23);
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            super.ShowMenu(player);
	        }
	    }
	    
	    protected class WorldMobArenaMenu extends MobArenaMenu
	    {
	        public WorldMobArenaMenu(final SettingsMenu prev) {
	            super(prev);
	            this.name = prev.getName() + ChatColor.WHITE + " - " + ChatColor.BLUE + "MobArena Menu";
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            super.ShowMenu(player);
	        }
	        
	        @Override
	        public void generateMenu() {
	            super.generateMenu();
	            final Button mobArenaEnabled = this.menuMap.get(0);
	            WorldSettingsMenu.this.ButtonInheritMod(this, mobArenaEnabled, ConfigKey.MOB_ARENA_ENABLED, 0);
	            final Button mobArenaMult = this.menuMap.get(1);
	            WorldSettingsMenu.this.ButtonInheritMod(this, mobArenaMult, ConfigKey.MOB_ARENA_MULTIPLIER, 1);
	            final Button mobArenaWaveEnabled = this.menuMap.get(2);
	            WorldSettingsMenu.this.ButtonInheritMod(this, mobArenaWaveEnabled, ConfigKey.MOB_ARENA_WAVE_LEVELING, 2);
	            final Button mobArenaWavePerLevel = this.menuMap.get(3);
	            WorldSettingsMenu.this.ButtonInheritMod(this, mobArenaWavePerLevel, ConfigKey.MOB_ARENA_WAVES_PER_LEVEL, 3);
	        }
	    }
	    
	    protected class WorldSpawningMenu extends SpawningMenu
	    {
	        public WorldSpawningMenu(final SettingsMenu prev) {
	            super(prev);
	            this.name = prev.getName() + ChatColor.WHITE + " - " + ChatColor.BLUE + "Spawning Menu";
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            super.ShowMenu(player);
	        }
	        
	        @Override
	        public void generateMenu() {
	            super.generateMenu();
	            final Menu thisMenu = this;
	            if (WorldSettingsMenu.this.config.isValueInherited(ConfigKey.LEVELED_MOBS)) {
	                this.menuMap.get(0).setName(this.menuMap.get(0).getName() + ChatColor.WHITE + " (" + ChatColor.YELLOW + "Global" + ChatColor.WHITE + ")");
	                this.menuMap.get(0).addLoreLine("");
	                this.menuMap.get(0).addLoreLine(ChatColor.GRAY + "Click to Change.");
	                this.menuMap.get(0).setOnPressedListener(new Button.onButtonPressedListener() {
	                    @Override
	                    public void onButtonPressed(final MenuInteractionEvent event) {
	                        WorldSettingsMenu.this.config.setLeveledMobs(WorldSettingsMenu.this.config.getLeveledMobs());
	                        new LeveledMenu(thisMenu,0).ShowMenu(event.getInteractor());
	                    }
	                });
	            }
	            else {
	                this.menuMap.get(0).addLoreLine("");
	                this.menuMap.get(0).addLoreLine(ChatColor.GRAY + "Click to Change. Right Click to use Global");
	                this.menuMap.get(0).setOnPressedListener(new Button.onButtonPressedListener() {
	                    @Override
	                    public void onButtonPressed(final MenuInteractionEvent event) {
	                        if (event.getClickType() == ClickType.RIGHT) {
	                            WorldSettingsMenu.this.config.useInheritedValue(ConfigKey.LEVELED_MOBS);
	                            WorldSpawningMenu.this.ShowMenu(event.getInteractor());
	                        }
	                        else {
	                            new LeveledMenu(thisMenu,0).ShowMenu(event.getInteractor());
	                        }
	                    }
	                });
	            }
	            if (WorldSettingsMenu.this.config.isValueInherited(ConfigKey.BLOCKED_MOBS)) {
	                this.menuMap.get(1).setName(this.menuMap.get(1).getName() + ChatColor.WHITE + " (" + ChatColor.YELLOW + "Global" + ChatColor.WHITE + ")");
	                this.menuMap.get(1).addLoreLine("");
	                this.menuMap.get(1).addLoreLine(ChatColor.GRAY + "Click to Change.");
	                this.menuMap.get(1).setOnPressedListener(new Button.onButtonPressedListener() {
	                    @Override
	                    public void onButtonPressed(final MenuInteractionEvent event) {
	                        WorldSettingsMenu.this.config.setBlockedMobs(WorldSettingsMenu.this.config.getBlockedMobs());
	                        new BlockedMenu(thisMenu,0).ShowMenu(event.getInteractor());
	                    }
	                });
	            }
	            else {
	                this.menuMap.get(1).addLoreLine("");
	                this.menuMap.get(1).addLoreLine(ChatColor.GRAY + "Click to Change. Right Click to use Global");
	                this.menuMap.get(1).setOnPressedListener(new Button.onButtonPressedListener() {
	                    @Override
	                    public void onButtonPressed(final MenuInteractionEvent event) {
	                        if (event.getClickType() == ClickType.RIGHT) {
	                            WorldSettingsMenu.this.config.useInheritedValue(ConfigKey.BLOCKED_MOBS);
	                            WorldSpawningMenu.this.ShowMenu(event.getInteractor());
	                        }
	                        else {
	                            new BlockedMenu(thisMenu,0).ShowMenu(event.getInteractor());
	                        }
	                    }
	                });
	            }
	        }
	    }
	    
	    protected class WorldNodeMenu extends Menu
	    {
	        private final Menu prev;
	        private NodeListMenu firstMenu;
	        
	        public WorldNodeMenu(final Menu prev) {
	            this.prev = prev;
	            this.name = prev.getName() + ChatColor.WHITE + " - " + ChatColor.BLUE + "Spawn Nodes";
	            this.generateMenus();
	        }
	        
	        public void generateMenus() {
	            int i = 0;
	            int page = 1;
	            NodeListMenu listMenu = null;
	            final ArrayList<SpawnNode> nodes = ((WorldConfig)WorldSettingsMenu.this.config).getNodes();
	            final ArrayList<SpawnNode> splitList = new ArrayList<SpawnNode>();
	            while (i < nodes.size()) {
	                if (i != 0 && i % 51 == 0) {
	                    listMenu = new NodeListMenu(page++, listMenu, (ArrayList<SpawnNode>)splitList.clone());
	                    splitList.clear();
	                }
	                splitList.add(nodes.get(i++));
	            }
	            listMenu = new NodeListMenu(page++, listMenu, (ArrayList<SpawnNode>)splitList.clone());
	            this.firstMenu = listMenu;
	            while (this.firstMenu.getPrev() != null) {
	                this.firstMenu = this.firstMenu.getPrev();
	            }
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            this.firstMenu.ShowMenu(player);
	        }
	        
	        public class NodeListMenu extends Menu
	        {
	            private NodeListMenu prev;
	            private NodeListMenu next;
	            private final ArrayList<SpawnNode> nodes;
	            private final int page;
	            
	            public NodeListMenu(final int page, final NodeListMenu prev, final ArrayList<SpawnNode> nodes) {
	                this.prev = null;
	                this.next = null;
	                this.prev = prev;
	                this.nodes = nodes;
	                this.page = page;
	                if (prev != null) {
	                    prev.setNext(this);
	                }
	                this.name = ChatColor.BLUE + "Spawn Nodes " + ChatColor.WHITE + "- " + ChatColor.YELLOW + "Page " + page;
	            }
	            
	            public void generateMenu() {
	                this.menuMap.clear();
	                final Menu thisMenu = this;
	                int i = 1;
	                for (final SpawnNode node : this.nodes) {
	                    final Button nodeButton = new Button();
	                    nodeButton.setIcon(Material.SPAWNER);
	                    nodeButton.setName(ChatColor.GREEN + "Spawn Node - " + node.getName());
	                    nodeButton.addLoreLine("");
	                    nodeButton.addLoreLine(ChatColor.WHITE + "X: " + ChatColor.LIGHT_PURPLE + node.getLocation().getX() + ChatColor.WHITE + " Y: " + ChatColor.LIGHT_PURPLE + node.getLocation().getY() + ChatColor.WHITE + " Z: " + ChatColor.LIGHT_PURPLE + node.getLocation().getZ());
	                    nodeButton.setOnPressedListener(new Button.onButtonPressedListener() {
	                        @Override
	                        public void onButtonPressed(final MenuInteractionEvent event) {
	                            new NodeSettingsMenu(node, thisMenu).ShowMenu(event.getInteractor());
	                        }
	                    });
	                    this.menuMap.put(i++, nodeButton);
	                }
	                final Button createButton = new Button();
	                createButton.setName(ChatColor.GREEN + "Create New Node");
	                createButton.setIcon(Material.GREEN_WOOL);
	                createButton.addLoreLine(ChatColor.GRAY + "Click to Create Spawn Node at Current Location.");
	                createButton.setOnPressedListener(new Button.onButtonPressedListener() {
	                    @Override
	                    public void onButtonPressed(final MenuInteractionEvent event) {
	                        final Location loc = event.getInteractor().getLocation();
	                        final SpawnNode node = ((WorldConfig)WorldSettingsMenu.this.config).addSpawnNode(loc.getX(), loc.getY(), loc.getZ());
	                        new NodeSettingsMenu(node, NodeListMenu.this.addNode(node)).ShowMenu(event.getInteractor());
	                    }
	                });
	                this.menuMap.put(0, createButton);
	                if (this.prev == null) {
	                    final Button backButton = new Button();
	                    backButton.setIcon(Material.NETHER_STAR);
	                    backButton.setName(ChatColor.RED + "\u25c0 Previous Menu");
	                    backButton.setOnPressedListener(new Button.onButtonPressedListener() {
	                        @Override
	                        public void onButtonPressed(final MenuInteractionEvent event) {
	                            new WorldSettingsMenu(WorldSettingsMenu.this.config).ShowMenu(event.getInteractor());
	                        }
	                    });
	                    this.menuMap.put(52, backButton);
	                }
	                else {
	                    final Button backButton = new Button();
	                    backButton.setIcon(Material.SKELETON_SKULL);
	                    final SkullMeta meta = (SkullMeta)backButton.getItemStack().getItemMeta();
	                    meta.setOwner("MHF_ArrowLeft");
	                    backButton.getItemStack().setItemMeta((ItemMeta)meta);
	                    backButton.setName(ChatColor.WHITE + "\u25c0 Page " + (this.page - 1));
	                    backButton.setOnPressedListener(new Button.onButtonPressedListener() {
	                        @Override
	                        public void onButtonPressed(final MenuInteractionEvent event) {
	                            NodeListMenu.this.prev.ShowMenu(event.getInteractor());
	                        }
	                    });
	                    this.menuMap.put(52, backButton);
	                }
	                if (this.next == null && this.prev != null) {
	                    final Button nextButton = new Button();
	                    nextButton.setIcon(Material.NETHER_STAR);
	                    nextButton.setName(ChatColor.RED + "\u25c0\u25c0 Back To Beginning");
	                    NodeListMenu beginning;
	                    for (beginning = this.prev; beginning != null && beginning.getPrev() != null; beginning = beginning.getPrev()) {}
	                    final Menu start = beginning;
	                    nextButton.setOnPressedListener(new Button.onButtonPressedListener() {
	                        @Override
	                        public void onButtonPressed(final MenuInteractionEvent event) {
	                            start.ShowMenu(event.getInteractor());
	                        }
	                    });
	                    this.menuMap.put(53, nextButton);
	                }
	                else if (this.next != null) {
	                    final Button nextButton = new Button();
	                    nextButton.setIcon(Material.SKELETON_SKULL);
	                    final SkullMeta meta = (SkullMeta)nextButton.getItemStack().getItemMeta();
	                    meta.setOwner("MHF_ArrowRight");
	                    nextButton.getItemStack().setItemMeta((ItemMeta)meta);
	                    nextButton.setName(ChatColor.WHITE + "Page " + (this.page + 1) + " \u25b6");
	                    nextButton.setOnPressedListener(new Button.onButtonPressedListener() {
	                        @Override
	                        public void onButtonPressed(final MenuInteractionEvent event) {
	                            NodeListMenu.this.next.ShowMenu(event.getInteractor());
	                        }
	                    });
	                    this.menuMap.put(53, nextButton);
	                }
	                else {
	                    this.menuMap.put(53, this.menuMap.remove(52));
	                }
	            }
	            
	            public void setNext(final NodeListMenu menu) {
	                this.next = menu;
	            }
	            
	            public NodeListMenu getPrev() {
	                return this.prev;
	            }
	            
	            public NodeListMenu getNext() {
	                return this.next;
	            }
	            
	            @Override
	            public void ShowMenu(final Player player) {
	                this.generateMenu();
	                super.ShowMenu(player);
	            }
	            
	            public Menu addNode(final SpawnNode node) {
	                if (this.nodes.size() < 51) {
	                    this.nodes.add(node);
	                    return this;
	                }
	                if (this.next != null) {
	                    return this.next.addNode(node);
	                }
	                final ArrayList<SpawnNode> newNodes = new ArrayList<SpawnNode>();
	                newNodes.add(node);
	                return this.next = new NodeListMenu(this.page + 1, this, newNodes);
	            }
	            
	            public void removeNode(final SpawnNode node) {
	                this.nodes.remove(node);
	                this.generateMenu();
	            }
	        }
	    }
}
