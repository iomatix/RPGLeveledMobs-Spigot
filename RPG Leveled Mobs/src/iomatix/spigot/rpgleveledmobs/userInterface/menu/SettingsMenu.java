package iomatix.spigot.rpgleveledmobs.userInterface.menu;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.lang.reflect.Method;

import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.Bukkit;

import org.bukkit.event.Listener;
import org.bukkit.entity.EntityType;
import org.bukkit.Material;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import iomatix.spigot.rpgleveledmobs.tools.Language;
import iomatix.spigot.rpgleveledmobs.userInterface.WrongMobButton;
import iomatix.spigot.rpgleveledmobs.userInterface.menu.SettingsMenu.BlockedMenu;
import iomatix.spigot.rpgleveledmobs.userInterface.MobButton;
import iomatix.spigot.rpgleveledmobs.userInterface.Button;
import iomatix.spigot.rpgleveledmobs.userInterface.MenuException;
import iomatix.spigot.rpgleveledmobs.userInterface.MenuInteractionEvent;
import iomatix.spigot.rpgleveledmobs.userInterface.MenuHandler;
import iomatix.spigot.rpgleveledmobs.userInterface.Menu;
import iomatix.spigot.rpgleveledmobs.config.cfgModule;
import iomatix.spigot.rpgleveledmobs.config.WorldConfig;
import iomatix.spigot.rpgleveledmobs.config.RPGLeveledMobsConfig;
import iomatix.spigot.rpgleveledmobs.Main;

public class SettingsMenu extends Menu{
	   protected StatsMenu statsMenu;
	    protected LevelingMenu levelingMenu;
	    protected SpawningMenu spawningMenu;
	    protected NamingMenu namingMenu;
	    protected MobArenaMenu mobArenaMenu;
	    protected RPGLeveledMobsConfig config;
	    private static final HashSet<Player> listeners;
	    
	    public SettingsMenu(final RPGLeveledMobsConfig config) {
	        this.config = config;
	        this.name = ChatColor.BLUE + "Global " + ChatColor.YELLOW + "Settings";
	        this.statsMenu = new StatsMenu(this);
	        this.levelingMenu = new LevelingMenu(this);
	        this.spawningMenu = new SpawningMenu(this);
	        this.namingMenu = new NamingMenu(this);
	        if (Main.isMobArenaLoaded()) {
	            this.mobArenaMenu = new MobArenaMenu(this);
	        }
	        this.createMenu();
	    }
	    
	    public SettingsMenu() {
	    }
	    
	    protected void createMenu() {
	        final Button statsMenuButton = new Button();
	        statsMenuButton.setIcon(Material.BEACON);
	        statsMenuButton.setName(ChatColor.GREEN + "Stat Settings");
	        statsMenuButton.setOnPressedListener(new Button.onButtonPressedListener() {
	            @Override
	            public void onButtonPressed(final MenuInteractionEvent event) {
	                SettingsMenu.this.statsMenu.ShowMenu(event.getInteractor());
	            }
	        });
	        final Button levelingMenuButton = new Button();
	        levelingMenuButton.setIcon(Material.GOLDEN_SWORD);
	        levelingMenuButton.setName(ChatColor.GREEN + "Leveling Settings");
	        levelingMenuButton.setOnPressedListener(new Button.onButtonPressedListener() {
	            @Override
	            public void onButtonPressed(final MenuInteractionEvent event) {
	                SettingsMenu.this.levelingMenu.ShowMenu(event.getInteractor());
	            }
	        });
	        final Button spawningMenuButton = new Button();
	        spawningMenuButton.setIcon(Material.SPAWNER);
	        spawningMenuButton.setName(ChatColor.GREEN + "Spawning Settings");
	        spawningMenuButton.setOnPressedListener(new Button.onButtonPressedListener() {
	            @Override
	            public void onButtonPressed(final MenuInteractionEvent event) {
	                SettingsMenu.this.spawningMenu.ShowMenu(event.getInteractor());
	            }
	        });
	        final Button namingMenuButton = new Button();
	        namingMenuButton.setIcon(Material.NAME_TAG);
	        namingMenuButton.setName(ChatColor.GREEN + "Naming Settings");
	        namingMenuButton.setOnPressedListener(new Button.onButtonPressedListener() {
	            @Override
	            public void onButtonPressed(final MenuInteractionEvent event) {
	                SettingsMenu.this.namingMenu.ShowMenu(event.getInteractor());
	            }
	        });
	        final Button mobArenaMenuButton = new Button();
	        mobArenaMenuButton.setIcon(Material.SKELETON_SKULL);
	        mobArenaMenuButton.setName(ChatColor.GREEN + "MobArena Settings");
	        mobArenaMenuButton.setOnPressedListener(new Button.onButtonPressedListener() {
	            @Override
	            public void onButtonPressed(final MenuInteractionEvent event) {
	                SettingsMenu.this.mobArenaMenu.ShowMenu(event.getInteractor());
	            }
	        });
	        final Button backButton = new Button();
	        backButton.setIcon(Material.NETHER_STAR);
	        backButton.setName(ChatColor.RED + "\u25c0 Previous Menu");
	        backButton.setOnPressedListener(new Button.onButtonPressedListener() {
	            @Override
	            public void onButtonPressed(final MenuInteractionEvent event) {
	                new BaseSettingMenu(Main.RPGMobs.getConfigModule()).ShowMenu(event.getInteractor());
	            }
	        });
	        this.menuMap.put(0, statsMenuButton);
	        this.menuMap.put(1, levelingMenuButton);
	        this.menuMap.put(2, spawningMenuButton);
	        this.menuMap.put(3, namingMenuButton);
	        if (Main.isMobArenaLoaded()) {
	            this.menuMap.put(4, mobArenaMenuButton);
	        }
	        this.menuMap.put(8, backButton);
	    }
	    
	    static {
	        listeners = new HashSet<Player>();
	    }
	    
	    protected class StatsMenu extends Menu
	    {
	        protected final Menu prev;
	        
	        public StatsMenu(final SettingsMenu prev) {
	            this.name = prev.getName() + ChatColor.WHITE + " - " + ChatColor.BLUE + "Scaling Menu";
	            this.prev = prev;
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            this.generateMenu();
	            super.ShowMenu(player);
	        }
	        
	        protected void generateMenu() {
	            this.menuMap.clear();
	            final Button exp = new Button();
	            exp.setName(ChatColor.BLUE + "Experience Settings");
	            exp.addLoreLine(ChatColor.YELLOW + "Formula: base + (base * level * multiplier)");
	            exp.setIcon(Material.EXPERIENCE_BOTTLE);
	            this.menuMap.put(1, exp);
	            final Button expToggle = new Button();
	            expToggle.setIcon(Material.WRITABLE_BOOK);
	            expToggle.setName(ChatColor.GREEN + "Experience Multiplier Enabled");
	            expToggle.addLoreLine(" ");
	            final boolean expEnabled = SettingsMenu.this.config.isExperienceModified();
	            if (expEnabled) {
	                expToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Enabled");
	            }
	            else {
	                expToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Disabled");
	            }
	            expToggle.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setExperienceModified(!expEnabled);
	                    StatsMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            expToggle.addLoreLine(" ");
	            expToggle.addLoreLine(ChatColor.GRAY + "Click to Toggle.");
	            this.menuMap.put(10, expToggle);
	            final Button expMod = new Button();
	            expMod.setIcon(Material.WRITABLE_BOOK);
	            expMod.setName(ChatColor.GREEN + "Experience Multiplier");
	            expMod.addLoreLine(" ");
	            final double expMult = SettingsMenu.this.config.getExperienceMultiplier();
	            expMod.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + expMult);
	            expMod.addLoreLine(" ");
	            expMod.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            expMod.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        StatsMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        StatsMenu.menuHandler.closeMenu(event.getInteractor());
	                        final DoubleChangeListener doubleChangeListener = new DoubleChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setExperienceMultiplier", Double.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(19, expMod);
	            final Button health = new Button();
	            health.setName(ChatColor.BLUE + "Health Settings");
	            health.addLoreLine(ChatColor.YELLOW + "Formula: base + (base * level * multiplier)");
	            health.setIcon(Material.NETHER_WART);
	            this.menuMap.put(3, health);
	            final Button healthToggle = new Button();
	            healthToggle.setName(ChatColor.GREEN + "Health Modifier Enabled");
	            healthToggle.setIcon(Material.WRITABLE_BOOK);
	            healthToggle.addLoreLine(" ");
	            final boolean healthEnabled = SettingsMenu.this.config.isHealthModified();
	            if (healthEnabled) {
	                healthToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Enabled");
	            }
	            else {
	                healthToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Disabled");
	            }
	            healthToggle.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setHealthModified(!healthEnabled);
	                    StatsMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            healthToggle.addLoreLine(" ");
	            healthToggle.addLoreLine(ChatColor.GRAY + "Click to Toggle.");
	            this.menuMap.put(12, healthToggle);
	            final Button healthMod = new Button();
	            healthMod.setIcon(Material.WRITABLE_BOOK);
	            healthMod.setName(ChatColor.GREEN + "Health Multiplier");
	            healthMod.addLoreLine(" ");
	            final double healthMult = SettingsMenu.this.config.getHealthMultiplier();
	            healthMod.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + healthMult);
	            healthMod.addLoreLine(" ");
	            healthMod.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            healthMod.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        StatsMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        StatsMenu.menuHandler.closeMenu(event.getInteractor());
	                        final DoubleChangeListener doubleChangeListener = new DoubleChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setHealthMultiplier", Double.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(21, healthMod);
	            final Button defense = new Button();
	            defense.setName(ChatColor.BLUE + "Defense Settings");
	            defense.addLoreLine(ChatColor.YELLOW + "Formula: base + (base * level * multiplier/100)");
	            defense.setIcon(Material.IRON_CHESTPLATE);
	            this.menuMap.put(5, defense);
	            final Button defenseToggle = new Button();
	            defenseToggle.setName(ChatColor.GREEN + "Defense Modifier Enabled");
	            defenseToggle.setIcon(Material.WRITABLE_BOOK);
	            defenseToggle.addLoreLine(" ");
	            final boolean defenseEnabled = SettingsMenu.this.config.isDefenseModified();
	            if (defenseEnabled) {
	            	defenseToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Enabled");
	            }
	            else {
	            	defenseToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Disabled");
	            }
	            defenseToggle.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setDefenseModified(!defenseEnabled);
	                    StatsMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            defenseToggle.addLoreLine(" ");
	            defenseToggle.addLoreLine(ChatColor.GRAY + "Click to Toggle.");
	            this.menuMap.put(14, defenseToggle);
	            final Button defenseMod = new Button();
	            defenseMod.setIcon(Material.WRITABLE_BOOK);
	            defenseMod.setName(ChatColor.GREEN + "Defense Multiplier");
	            defenseMod.addLoreLine(" ");
	            final double defenseMult = SettingsMenu.this.config.getDefenseMultiplier();
	            defenseMod.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + defenseMult);
	            defenseMod.addLoreLine(" ");
	            defenseMod.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            defenseMod.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        StatsMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        StatsMenu.menuHandler.closeMenu(event.getInteractor());
	                        final DoubleChangeListener doubleChangeListener = new DoubleChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setDefenseMultiplier", Double.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(23, defenseMod);
	            final Button damage = new Button();
	            damage.setName(ChatColor.BLUE + "Damage Settings");
	            damage.addLoreLine(ChatColor.YELLOW + "Formula: base + (base * level * multiplier)");
	            damage.setIcon(Material.IRON_SWORD);
	            this.menuMap.put(7, damage);
	            final Button damageToggle = new Button();
	            damageToggle.setName(ChatColor.GREEN + "Damage Modifier Enabled");
	            damageToggle.setIcon(Material.WRITABLE_BOOK);
	            damageToggle.addLoreLine(" ");
	            final boolean damageEnabled = SettingsMenu.this.config.isDamageModified();
	            if (damageEnabled) {
	                damageToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Enabled");
	            }
	            else {
	                damageToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Disabled");
	            }
	            damageToggle.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setDamageModified(!damageEnabled);
	                    StatsMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            damageToggle.addLoreLine(" ");
	            damageToggle.addLoreLine(ChatColor.GRAY + "Click to Toggle.");
	            this.menuMap.put(16, damageToggle);
	            final Button damageMod = new Button();
	            damageMod.setIcon(Material.WRITABLE_BOOK);
	            damageMod.setName(ChatColor.GREEN + "Damage Multiplier");
	            damageMod.addLoreLine(" ");
	            final double damageMult = SettingsMenu.this.config.getDamageMultiplier();
	            damageMod.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + damageMult);
	            damageMod.addLoreLine(" ");
	            damageMod.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            damageMod.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        StatsMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        StatsMenu.menuHandler.closeMenu(event.getInteractor());
	                        final DoubleChangeListener doubleChangeListener = new DoubleChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setDamageMultiplier", Double.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(25, damageMod);
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.RED + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    StatsMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(35, previous);
	        }
	    }
	    
	    protected class LevelingMenu extends Menu
	    {
	        private final Menu prev;
	        
	        public LevelingMenu(final SettingsMenu prev) {
	            this.name = ChatColor.BLUE + "Leveling " + ChatColor.YELLOW + "Settings";
	            this.prev = prev;
	            this.generateMenu();
	        }
	        
	        public void generateMenu() {
	            this.menuMap.clear();
	            final Button minLevel = new Button();
	            minLevel.setIcon(Material.WRITABLE_BOOK);
	            minLevel.setName(ChatColor.GREEN + "Minimum Level");
	            minLevel.addLoreLine(" ");
	            final int min = SettingsMenu.this.config.getMinLevel();
	            minLevel.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + "" + min);
	            minLevel.addLoreLine(" ");
	            minLevel.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            minLevel.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        LevelingMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        LevelingMenu.menuHandler.closeMenu(event.getInteractor());
	                        final IntegerChangeListener integerChangeListener = new IntegerChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setMinLevel", Integer.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(0, minLevel);
	            final Button maxLevel = new Button();
	            maxLevel.setIcon(Material.WRITABLE_BOOK);
	            maxLevel.setName(ChatColor.GREEN + "Maximum Level");
	            maxLevel.addLoreLine(" ");
	            final int max = SettingsMenu.this.config.getMaxLevel();
	            maxLevel.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + "" + max);
	            maxLevel.addLoreLine(" ");
	            maxLevel.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            maxLevel.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        LevelingMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        LevelingMenu.menuHandler.closeMenu(event.getInteractor());
	                        final IntegerChangeListener integerChangeListener = new IntegerChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setMaxLevel", Integer.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(1, maxLevel);
	            final Button startLevel = new Button();
	            startLevel.setIcon(Material.WRITABLE_BOOK);
	            startLevel.setName(ChatColor.GREEN + "Start Level");
	            startLevel.addLoreLine(" ");
	            final int start = SettingsMenu.this.config.getStartLevel();
	            startLevel.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + "" + start);
	            startLevel.addLoreLine(" ");
	            startLevel.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            startLevel.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        LevelingMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        LevelingMenu.menuHandler.closeMenu(event.getInteractor());
	                        final IntegerChangeListener integerChangeListener = new IntegerChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setStartLevel", Integer.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(2, startLevel);
	            final Button distancePerLevel = new Button();
	            distancePerLevel.setIcon(Material.WRITABLE_BOOK);
	            distancePerLevel.setName(ChatColor.GREEN + "Distance Per Level Increase");
	            distancePerLevel.addLoreLine(" ");
	            final double dist = SettingsMenu.this.config.getDistancePerLevel();
	            distancePerLevel.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + "" + dist);
	            distancePerLevel.addLoreLine(" ");
	            distancePerLevel.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            distancePerLevel.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        LevelingMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        LevelingMenu.menuHandler.closeMenu(event.getInteractor());
	                        final DoubleChangeListener doubleChangeListener = new DoubleChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setDistancePerLevel", Double.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(3, distancePerLevel);
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.RED + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    LevelingMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(8, previous);
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            this.generateMenu();
                super.ShowMenu(player);
           
	            
	        }
	    }
	    
	    protected class SpawningMenu extends Menu
	    {
	        private final Menu prev;
	        
	        public SpawningMenu(final SettingsMenu prev) {
	            this.prev = prev;
	            this.generateMenu();
	            this.name = ChatColor.BLUE + "Spawning " + ChatColor.YELLOW + "Settings";
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            this.generateMenu();
	            super.ShowMenu(player);
	        }
	        
	        public void generateMenu() {
	            this.menuMap.clear();
	            final Button allowed = new Button();
	            allowed.setIcon(Material.SPAWNER);
	            allowed.setName(ChatColor.GREEN + "Leveled Mobs");
	            allowed.addLoreLine("");
	            allowed.addLoreLine(ChatColor.YELLOW + "Which mobs are given levels when spawned");
	            final Menu thisMenu = this;
	            allowed.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    new LeveledMenu(thisMenu,0).ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(0, allowed);
	            final Button blocked = new Button();
	            blocked.setIcon(Material.IRON_BARS);
	            blocked.setName(ChatColor.GREEN + "Blocked Mobs");
	            blocked.addLoreLine("");
	            blocked.addLoreLine(ChatColor.YELLOW + "Which mobs are denied from spawning altogether");
	            blocked.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    new BlockedMenu(thisMenu,0).ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(1, blocked);
	            final Button leveledSpawners = new Button();
	            leveledSpawners.setIcon(Material.WHITE_WOOL);
	            leveledSpawners.setName(ChatColor.GREEN + "Leveled Spawners");
	            leveledSpawners.addLoreLine("");
	            leveledSpawners.addLoreLine(ChatColor.YELLOW + "Whether or not to level mobs from spawners.");
	            leveledSpawners.addLoreLine("");
	            if (SettingsMenu.this.config.isLeveledSpawners()) {
	                leveledSpawners.setIcon(Material.GREEN_WOOL);
	                leveledSpawners.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Enabled");
	            }
	            else {
	                leveledSpawners.setIcon(Material.RED_WOOL);
	                leveledSpawners.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Disabled");
	            }
	            leveledSpawners.addLoreLine("");
	            leveledSpawners.addLoreLine(ChatColor.GRAY + "Click to toggle.");
	            leveledSpawners.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setLeveledSpawners(!SettingsMenu.this.config.isLeveledSpawners());
	                    SpawningMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(2, leveledSpawners);
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.RED + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SpawningMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(8, previous);
	        }
	    }
	    
	    protected class LeveledMenu extends Menu
	    {
	        final Menu prev;
	        int page = 0;
	        public LeveledMenu(final Menu prev,int page) {
	            this.prev = prev;
	            this.page = page;
	            this.generateMenu(page);
	            this.name = ChatColor.GOLD + "Leveled Mobs "+ChatColor.GRAY+"page ["+(page+1)+"]" ;
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            this.generateMenu(page);
	            super.ShowMenu(player);
	        }
	        
	        public void generateMenu(int page) {
	                this.generateNewMenu(page);
	        }
	        
	        public void handle11BadMobs() {
	            final WrongMobButton Vindicator = new WrongMobButton("Vindicator", "1.11");
	            this.menuMap.put(39, Vindicator);
	            final WrongMobButton Vex = new WrongMobButton("Vex", "1.11");
	            this.menuMap.put(37, Vex);
	            final WrongMobButton Llama = new WrongMobButton("Llama", "1.11");
	            this.menuMap.put(19, Llama);
	            final WrongMobButton Evoker = new WrongMobButton("Evoker", "1.11");
	            this.menuMap.put(11, Evoker);
	            final WrongMobButton ZombieHorse = new WrongMobButton("Zombie Horse", "1.11");
	            this.menuMap.put(45, ZombieHorse);
	            final WrongMobButton Mule = new WrongMobButton("Mule", "1.11");
	            this.menuMap.put(20, Mule);
	            final WrongMobButton Donkey = new WrongMobButton("Donkey", "1.11");
	            this.menuMap.put(6, Donkey);
	            final WrongMobButton SkeletonHorse = new WrongMobButton("Skeleton Horse", "1.11");
	            this.menuMap.put(31, SkeletonHorse);
	            final WrongMobButton ElderGuardian = new WrongMobButton("Elder Guardian", "1.11");
	            this.menuMap.put(7, ElderGuardian);
	            final WrongMobButton WitherSkeleton = new WrongMobButton("Wither Skeleton", "1.11");
	            this.menuMap.put(42, WitherSkeleton);
	            final WrongMobButton ZombieVillager = new WrongMobButton("Zombie Villager", "1.11");
	            this.menuMap.put(46, ZombieVillager);
	        }
	        
	        public void handle10BadMobs() {
	            final WrongMobButton PolarBear = new WrongMobButton("Polar Bear", "1.10");
	            this.menuMap.put(25, PolarBear);
	            final WrongMobButton Stray = new WrongMobButton("Stray", "1.10");
	            this.menuMap.put(36, Stray);
	            final WrongMobButton Husk = new WrongMobButton("Husk", "1.10");
	            this.menuMap.put(16, Husk);
	        }
	        
	        public void handle9BadMobs() {
	            final WrongMobButton Shulker = new WrongMobButton("Shulker", "1.9");
	            this.menuMap.put(28, Shulker);
	        }
	        
	        public void generateNewMenu(int page) {
	        	if(page == 0) {
	            final MobButton Bat = new MobButton("Bat", EntityType.BAT, SettingsMenu.this.config, this);
	            this.menuMap.put(0, Bat);
	            final MobButton Blaze = new MobButton("Blaze", EntityType.BLAZE, SettingsMenu.this.config, this);
	            this.menuMap.put(1, Blaze);
	            final Button CaveSpider = new MobButton("Cave Spider", EntityType.CAVE_SPIDER, SettingsMenu.this.config, this);
	            this.menuMap.put(2, CaveSpider);
	            final Button Chicken = new MobButton("Chicken", EntityType.CHICKEN, SettingsMenu.this.config, this);
	            this.menuMap.put(3, Chicken);
	            final Button Cow = new MobButton("Cow", EntityType.COW, SettingsMenu.this.config, this);
	            this.menuMap.put(4, Cow);
	            final Button Creeper = new MobButton("Creeper", EntityType.CREEPER, SettingsMenu.this.config, this);
	            this.menuMap.put(5, Creeper);
	            final MobButton EnderDragon = new MobButton("Ender Dragon", EntityType.ENDER_DRAGON, SettingsMenu.this.config, this);
	            EnderDragon.setIcon(Material.DRAGON_EGG);
	            this.menuMap.put(8, EnderDragon);
	            final MobButton Enderman = new MobButton("Enderman", EntityType.ENDERMAN, SettingsMenu.this.config, this);
	            this.menuMap.put(9, Enderman);
	            final MobButton Endermite = new MobButton("Endermite", EntityType.ENDERMITE, SettingsMenu.this.config, this);
	            this.menuMap.put(10, Endermite);
	            final MobButton Ghast = new MobButton("Ghast", EntityType.GHAST, SettingsMenu.this.config, this);
	            this.menuMap.put(12, Ghast);
	            final MobButton Giant = new MobButton("Giant", EntityType.GIANT, SettingsMenu.this.config, this);
	            this.menuMap.put(13, Giant);
	            final MobButton Guardian = new MobButton("Guardian", EntityType.GUARDIAN, SettingsMenu.this.config, this);
	            this.menuMap.put(14, Guardian);
	            final MobButton Horse = new MobButton("Horse", EntityType.HORSE, SettingsMenu.this.config, this);
	            this.menuMap.put(15, Horse);
	            final MobButton IronGolem = new MobButton("Iron Golem", EntityType.IRON_GOLEM, SettingsMenu.this.config, this);
	            this.menuMap.put(17, IronGolem);
	            final MobButton LavaSlime = new MobButton("Lava Slime", EntityType.MAGMA_CUBE, SettingsMenu.this.config, this);
	            this.menuMap.put(18, LavaSlime);
	            final MobButton MushroomCow = new MobButton("Mushroom Cow", EntityType.MUSHROOM_COW, SettingsMenu.this.config, this);
	            this.menuMap.put(21, MushroomCow);
	            final MobButton Ocelot = new MobButton("Ocelot", EntityType.OCELOT, SettingsMenu.this.config, this);
	            this.menuMap.put(22, Ocelot);
	            final MobButton Pig = new MobButton("Pig", EntityType.PIG, SettingsMenu.this.config, this);
	            this.menuMap.put(23, Pig);
	            final MobButton PigZombie = new MobButton("Pig Zombie", EntityType.PIG_ZOMBIE, SettingsMenu.this.config, this);
	            this.menuMap.put(24, PigZombie);
	            final MobButton Rabbit = new MobButton("Rabbit", EntityType.RABBIT, SettingsMenu.this.config, this);
	            this.menuMap.put(26, Rabbit);
	            final MobButton Sheep = new MobButton("Sheep", EntityType.SHEEP, SettingsMenu.this.config, this);
	            this.menuMap.put(27, Sheep);
	            final MobButton SilverFish = new MobButton("Silverfish", EntityType.SILVERFISH, SettingsMenu.this.config, this);
	            this.menuMap.put(29, SilverFish);
	            final MobButton Skeleton = new MobButton("Skeleton", EntityType.SKELETON, SettingsMenu.this.config, this);
	            this.menuMap.put(30, Skeleton);
	            final MobButton Slime = new MobButton("Slime", EntityType.SLIME, SettingsMenu.this.config, this);
	            this.menuMap.put(32, Slime);
	            final MobButton Snowman = new MobButton("Snowman", EntityType.SNOWMAN, SettingsMenu.this.config, this);
	            this.menuMap.put(33, Snowman);
	            final MobButton Spider = new MobButton("Spider", EntityType.SPIDER, SettingsMenu.this.config, this);
	            this.menuMap.put(34, Spider);
	            final MobButton Squid = new MobButton("Squid", EntityType.SQUID, SettingsMenu.this.config, this);
	            this.menuMap.put(35, Squid);
	            final MobButton Villager = new MobButton("Villager", EntityType.VILLAGER, SettingsMenu.this.config, this);
	            this.menuMap.put(38, Villager);
	            final MobButton Witch = new MobButton("Witch", EntityType.WITCH, SettingsMenu.this.config, this);
	            this.menuMap.put(40, Witch);
	            final MobButton Wither = new MobButton("Wither", EntityType.WITHER, SettingsMenu.this.config, this);
	            this.menuMap.put(41, Wither);
	            final MobButton Wolf = new MobButton("Wolf", EntityType.WOLF, SettingsMenu.this.config, this);
	            this.menuMap.put(43, Wolf);
	            final MobButton Zombie = new MobButton("Zombie", EntityType.ZOMBIE, SettingsMenu.this.config, this);
	            this.menuMap.put(44, Zombie);
	            try {
	                final MobButton Shulker = new MobButton("Shulker", EntityType.SHULKER, SettingsMenu.this.config, this);
	                this.menuMap.put(28, Shulker);
	            }
	            catch (NoSuchFieldError e) {
	                this.handle9BadMobs();
	            }
	            try {
	                final MobButton PolarBear = new MobButton("Polar Bear", EntityType.POLAR_BEAR, SettingsMenu.this.config, this);
	                this.menuMap.put(25, PolarBear);
	                final MobButton Stray = new MobButton("Stray", EntityType.STRAY, SettingsMenu.this.config, this);
	                this.menuMap.put(36, Stray);
	                final MobButton Husk = new MobButton("Husk", EntityType.HUSK, SettingsMenu.this.config, this);
	                this.menuMap.put(16, Husk);
	            }
	            catch (NoSuchFieldError e) {
	                this.handle10BadMobs();
	            }
	            try {
	                final MobButton Vindicator = new MobButton("Vindicator", EntityType.VINDICATOR, SettingsMenu.this.config, this);
	                this.menuMap.put(39, Vindicator);
	                final MobButton Vex = new MobButton("Vex", EntityType.VEX, SettingsMenu.this.config, this);
	                this.menuMap.put(37, Vex);
	                final MobButton Llama = new MobButton("Llama", EntityType.LLAMA, SettingsMenu.this.config, this);
	                this.menuMap.put(19, Llama);
	                final MobButton Evoker = new MobButton("Evoker", EntityType.EVOKER, SettingsMenu.this.config, this);
	                this.menuMap.put(11, Evoker);
	                final MobButton ZombieHorse = new MobButton("Zombie Horse", EntityType.ZOMBIE_HORSE, SettingsMenu.this.config, this);
	                this.menuMap.put(45, ZombieHorse);
	                final MobButton Mule = new MobButton("Mule", EntityType.MULE, SettingsMenu.this.config, this);
	                this.menuMap.put(20, Mule);
	                final MobButton Donkey = new MobButton("Donkey", EntityType.DONKEY, SettingsMenu.this.config, this);
	                this.menuMap.put(6, Donkey);
	                final MobButton SkeletonHorse = new MobButton("Skeleton Horse", EntityType.SKELETON_HORSE, SettingsMenu.this.config, this);
	                this.menuMap.put(31, SkeletonHorse);
	                final MobButton ElderGuardian = new MobButton("Elder Guardian", EntityType.ELDER_GUARDIAN, SettingsMenu.this.config, this);
	                this.menuMap.put(7, ElderGuardian);
	                final MobButton WitherSkeleton = new MobButton("Wither Skeleton", EntityType.WITHER_SKELETON, SettingsMenu.this.config, this);
	                this.menuMap.put(42, WitherSkeleton);
	                final MobButton ZombieVillager = new MobButton("Zombie Villager", EntityType.ZOMBIE_VILLAGER, SettingsMenu.this.config, this);
	                this.menuMap.put(46, ZombieVillager);
	            }
	            catch (NoSuchFieldError e) {
	                this.handle11BadMobs();
	            }
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.RED + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    LeveledMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(52, previous);
	            final Button nextpage = new Button();
	            nextpage.setIcon(Material.NETHER_STAR);
	            nextpage.setName(ChatColor.GREEN + "Next Page \u25b6");
	            nextpage.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    new LeveledMenu(LeveledMenu.this,1).ShowMenu(event.getInteractor());
	                }
	            });    
	            this.menuMap.put(53, nextpage);
	        	}else if(page == 1) {
	            try {
	                final MobButton Parrot = new MobButton("Parrot", EntityType.PARROT, SettingsMenu.this.config, this, true);
	                this.menuMap.put(0, Parrot);
	                final MobButton Dolphin = new MobButton("Dolphin", EntityType.DOLPHIN, SettingsMenu.this.config, this, true);
	                this.menuMap.put(1, Dolphin);
	                final MobButton Drowned = new MobButton("Drowned", EntityType.DROWNED, SettingsMenu.this.config, this, true);
	                this.menuMap.put(2, Drowned);
	                final MobButton Phantom = new MobButton("Phantom", EntityType.PHANTOM, SettingsMenu.this.config, this, true);
	                this.menuMap.put(3, Phantom);
	                final MobButton Cod = new MobButton("Cod", EntityType.COD, SettingsMenu.this.config, this, true);
	                this.menuMap.put(4, Cod);
	                final MobButton Salmon = new MobButton("Salmon", EntityType.SALMON, SettingsMenu.this.config, this, true);
	                this.menuMap.put(5, Salmon);
	                final MobButton Pufferfish = new MobButton("Pufferfish", EntityType.PUFFERFISH, SettingsMenu.this.config, this, true);
	                this.menuMap.put(6, Pufferfish);
	                final MobButton Tropicalfish = new MobButton("Tropical Fish", EntityType.TROPICAL_FISH, SettingsMenu.this.config, this, true);
	                this.menuMap.put(7, Tropicalfish);
	                final MobButton Turtle = new MobButton("Turtle", EntityType.TURTLE, SettingsMenu.this.config, this, true);
	                this.menuMap.put(8, Turtle);
	            }
	            catch (NoSuchFieldError e) {
	            	final WrongMobButton Parrot2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(0, Parrot2);
	                final WrongMobButton Dolphin2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(1, Dolphin2);
	                final WrongMobButton Drowned2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(2, Drowned2);
	                final WrongMobButton Phantom2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(3, Phantom2);
	                final WrongMobButton Cod2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(4, Cod2);
	                final WrongMobButton Salmon2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(5, Salmon2);
	                final WrongMobButton Pufferfish2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(6, Pufferfish2);
	                final WrongMobButton Tropicalfish2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(7, Tropicalfish2);
	                final WrongMobButton Turtle2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(8, Turtle2);
	                
	            }
	            try {
	                final MobButton Cat = new MobButton("Cat", EntityType.CAT, SettingsMenu.this.config, this, true);
	                this.menuMap.put(9, Cat);
	                final MobButton Fox = new MobButton("Fox", EntityType.FOX, SettingsMenu.this.config, this, true);
	                this.menuMap.put(10, Fox);
	                final MobButton Panda = new MobButton("Panda", EntityType.PANDA, SettingsMenu.this.config, this, true);
	                this.menuMap.put(11, Panda);
	                final MobButton Pillager = new MobButton("Pillager", EntityType.PILLAGER, SettingsMenu.this.config, this, true);
	                this.menuMap.put(12, Pillager);
	                final MobButton Ravager = new MobButton("Ravager", EntityType.RAVAGER, SettingsMenu.this.config, this, true);
	                this.menuMap.put(13, Ravager);
	                final MobButton Trader_Llama = new MobButton("Trader's Llama", EntityType.TRADER_LLAMA, SettingsMenu.this.config, this, true);
	                this.menuMap.put(14, Trader_Llama);
	                final MobButton Wandering_Trader = new MobButton("Wandering Trader", EntityType.WANDERING_TRADER, SettingsMenu.this.config, this, true);
	                this.menuMap.put(15, Wandering_Trader);
	            }
	            catch (NoSuchFieldError e) {
	                final WrongMobButton Cat2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(9, Cat2);
	                final WrongMobButton Fox2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(10, Fox2);
	                final WrongMobButton Panda2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(11, Panda2);
	                final WrongMobButton Pillager2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(12, Pillager2);
	                final WrongMobButton Ravager2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(13, Ravager2);
	                final WrongMobButton Trader_Llama2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(14, Trader_Llama2);
	                final WrongMobButton Wandering_Trader2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(15, Wandering_Trader2);     
	            }
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.RED + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    LeveledMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(52, previous);
	            final Button nextpage = new Button();
	            nextpage.setIcon(Material.NETHER_STAR);
	            nextpage.setName(ChatColor.GRAY + "Next Page \u25b6");
	            nextpage.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    //new LeveledMenu(LeveledMenu.this,2).ShowMenu(event.getInteractor());
	                }
	            });  
	            this.menuMap.put(53, nextpage);
	        	}
	        }
	    }
	    
	    protected class BlockedMenu extends Menu
	    {
	        private final Menu prev;
	        int page = 0;
	        public BlockedMenu(final Menu prev, int page) {
	            this.prev = prev;
	            this.page = page;
	            this.name = ChatColor.RED + "Blocked Mobs "+ChatColor.GRAY+"page ["+(page+1)+"]" ;
	            this.generateMenu(page);
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            this.generateMenu(page);
	            super.ShowMenu(player);
	        }
	        
	        public void generateMenu(int page) {
	                this.generateNewMenu(page);
	        }        
	        public void generateNewMenu(int page) {
	        	if (page == 0) {
	            final MobButton Bat = new MobButton("Bat", EntityType.BAT, SettingsMenu.this.config, this, true);
	            this.menuMap.put(0, Bat);
	            final MobButton Blaze = new MobButton("Blaze", EntityType.BLAZE, SettingsMenu.this.config, this, true);
	            this.menuMap.put(1, Blaze);
	            final Button CaveSpider = new MobButton("Cave Spider", EntityType.CAVE_SPIDER, SettingsMenu.this.config, this, true);
	            this.menuMap.put(2, CaveSpider);
	            final Button Chicken = new MobButton("Chicken", EntityType.CHICKEN, SettingsMenu.this.config, this, true);
	            this.menuMap.put(3, Chicken);
	            final Button Cow = new MobButton("Cow", EntityType.COW, SettingsMenu.this.config, this, true);
	            this.menuMap.put(4, Cow);
	            final Button Creeper = new MobButton("Creeper", EntityType.CREEPER, SettingsMenu.this.config, this, true);
	            this.menuMap.put(5, Creeper);
	            final MobButton EnderDragon = new MobButton("Ender Dragon", EntityType.ENDER_DRAGON, SettingsMenu.this.config, this, true);
	            EnderDragon.setIcon(Material.DRAGON_EGG);
	            this.menuMap.put(8, EnderDragon);
	            final MobButton Enderman = new MobButton("Enderman", EntityType.ENDERMAN, SettingsMenu.this.config, this, true);
	            this.menuMap.put(9, Enderman);
	            final MobButton Endermite = new MobButton("Endermite", EntityType.ENDERMITE, SettingsMenu.this.config, this, true);
	            this.menuMap.put(10, Endermite);
	            final MobButton Ghast = new MobButton("Ghast", EntityType.GHAST, SettingsMenu.this.config, this, true);
	            this.menuMap.put(12, Ghast);
	            final MobButton Giant = new MobButton("Giant", EntityType.GIANT, SettingsMenu.this.config, this, true);
	            this.menuMap.put(13, Giant);
	            final MobButton Guardian = new MobButton("Guardian", EntityType.GUARDIAN, SettingsMenu.this.config, this, true);
	            this.menuMap.put(14, Guardian);
	            final MobButton Horse = new MobButton("Horse", EntityType.HORSE, SettingsMenu.this.config, this, true);
	            this.menuMap.put(15, Horse);
	            final MobButton IronGolem = new MobButton("Iron Golem", EntityType.IRON_GOLEM, SettingsMenu.this.config, this, true);
	            this.menuMap.put(17, IronGolem);
	            final MobButton LavaSlime = new MobButton("Lava Slime", EntityType.MAGMA_CUBE, SettingsMenu.this.config, this, true);
	            this.menuMap.put(18, LavaSlime);
	            final MobButton MushroomCow = new MobButton("Mushroom Cow", EntityType.MUSHROOM_COW, SettingsMenu.this.config, this, true);
	            this.menuMap.put(21, MushroomCow);
	            final MobButton Ocelot = new MobButton("Ocelot", EntityType.OCELOT, SettingsMenu.this.config, this, true);
	            this.menuMap.put(22, Ocelot);
	            final MobButton Pig = new MobButton("Pig", EntityType.PIG, SettingsMenu.this.config, this, true);
	            this.menuMap.put(23, Pig);
	            final MobButton PigZombie = new MobButton("Pig Zombie", EntityType.PIG_ZOMBIE, SettingsMenu.this.config, this, true);
	            this.menuMap.put(24, PigZombie);
	            final MobButton Rabbit = new MobButton("Rabbit", EntityType.RABBIT, SettingsMenu.this.config, this, true);
	            this.menuMap.put(26, Rabbit);
	            final MobButton Sheep = new MobButton("Sheep", EntityType.SHEEP, SettingsMenu.this.config, this, true);
	            this.menuMap.put(27, Sheep);
	            final MobButton SilverFish = new MobButton("Silverfish", EntityType.SILVERFISH, SettingsMenu.this.config, this, true);
	            this.menuMap.put(29, SilverFish);
	            final MobButton Skeleton = new MobButton("Skeleton", EntityType.SKELETON, SettingsMenu.this.config, this, true);
	            this.menuMap.put(30, Skeleton);
	            final MobButton Slime = new MobButton("Slime", EntityType.SLIME, SettingsMenu.this.config, this, true);
	            this.menuMap.put(32, Slime);
	            final MobButton Snowman = new MobButton("Snowman", EntityType.SNOWMAN, SettingsMenu.this.config, this, true);
	            this.menuMap.put(33, Snowman);
	            final MobButton Spider = new MobButton("Spider", EntityType.SPIDER, SettingsMenu.this.config, this, true);
	            this.menuMap.put(34, Spider);
	            final MobButton Squid = new MobButton("Squid", EntityType.SQUID, SettingsMenu.this.config, this, true);
	            this.menuMap.put(35, Squid);
	            final MobButton Villager = new MobButton("Villager", EntityType.VILLAGER, SettingsMenu.this.config, this, true);
	            this.menuMap.put(38, Villager);
	            final MobButton Witch = new MobButton("Witch", EntityType.WITCH, SettingsMenu.this.config, this, true);
	            this.menuMap.put(40, Witch);
	            final MobButton Wither = new MobButton("Wither", EntityType.WITHER, SettingsMenu.this.config, this, true);
	            this.menuMap.put(41, Wither);
	            final MobButton Wolf = new MobButton("Wolf", EntityType.WOLF, SettingsMenu.this.config, this, true);
	            this.menuMap.put(43, Wolf);
	            final MobButton Zombie = new MobButton("Zombie", EntityType.ZOMBIE, SettingsMenu.this.config, this, true);
	            this.menuMap.put(44, Zombie);
	            try {
	                final MobButton Shulker = new MobButton("Shulker", EntityType.SHULKER, SettingsMenu.this.config, this, true);
	                this.menuMap.put(28, Shulker);
	            }
	            catch (NoSuchFieldError e) {
	                final WrongMobButton Shulker2 = new WrongMobButton("Shulker", "1.9");
	                this.menuMap.put(28, Shulker2);
	            }
	            try {
	                final MobButton PolarBear = new MobButton("Polar Bear", EntityType.POLAR_BEAR, SettingsMenu.this.config, this, true);
	                this.menuMap.put(25, PolarBear);
	                final MobButton Stray = new MobButton("Stray", EntityType.STRAY, SettingsMenu.this.config, this, true);
	                this.menuMap.put(36, Stray);
	                final MobButton Husk = new MobButton("Husk", EntityType.HUSK, SettingsMenu.this.config, this, true);
	                this.menuMap.put(16, Husk);
	            }
	            catch (NoSuchFieldError e) {
	                final WrongMobButton PolarBear2 = new WrongMobButton("Polar Bear", "1.10");
	                this.menuMap.put(25, PolarBear2);
	                final WrongMobButton Stray2 = new WrongMobButton("Stray", "1.10");
	                this.menuMap.put(36, Stray2);
	                final WrongMobButton Husk2 = new WrongMobButton("Husk", "1.10");
	                this.menuMap.put(16, Husk2);
	            }
	            try {
	                final MobButton Vindicator = new MobButton("Vindicator", EntityType.VINDICATOR, SettingsMenu.this.config, this, true);
	                this.menuMap.put(39, Vindicator);
	                final MobButton Vex = new MobButton("Vex", EntityType.VEX, SettingsMenu.this.config, this, true);
	                this.menuMap.put(37, Vex);
	                final MobButton Llama = new MobButton("Llama", EntityType.LLAMA, SettingsMenu.this.config, this, true);
	                this.menuMap.put(19, Llama);
	                final MobButton Evoker = new MobButton("Evoker", EntityType.EVOKER, SettingsMenu.this.config, this, true);
	                this.menuMap.put(11, Evoker);
	                final MobButton ZombieHorse = new MobButton("Zombie Horse", EntityType.ZOMBIE_HORSE, SettingsMenu.this.config, this, true);
	                this.menuMap.put(45, ZombieHorse);
	                final MobButton Mule = new MobButton("Mule", EntityType.MULE, SettingsMenu.this.config, this, true);
	                this.menuMap.put(20, Mule);
	                final MobButton Donkey = new MobButton("Donkey", EntityType.DONKEY, SettingsMenu.this.config, this, true);
	                this.menuMap.put(6, Donkey);
	                final MobButton SkeletonHorse = new MobButton("Skeleton Horse", EntityType.SKELETON_HORSE, SettingsMenu.this.config, this, true);
	                this.menuMap.put(31, SkeletonHorse);
	                final MobButton ElderGuardian = new MobButton("Elder Guardian", EntityType.ELDER_GUARDIAN, SettingsMenu.this.config, this, true);
	                this.menuMap.put(7, ElderGuardian);
	                final MobButton WitherSkeleton = new MobButton("Wither Skeleton", EntityType.WITHER_SKELETON, SettingsMenu.this.config, this, true);
	                this.menuMap.put(42, WitherSkeleton);
	                final MobButton ZombieVillager = new MobButton("Zombie Villager", EntityType.ZOMBIE_VILLAGER, SettingsMenu.this.config, this, true);
	                this.menuMap.put(46, ZombieVillager);
	            }
	            catch (NoSuchFieldError e) {
	                final WrongMobButton Vindicator2 = new WrongMobButton("Vindicator", "1.11");
	                this.menuMap.put(39, Vindicator2);
	                final WrongMobButton Vex2 = new WrongMobButton("Vex", "1.11");
	                this.menuMap.put(37, Vex2);
	                final WrongMobButton Llama2 = new WrongMobButton("Llama", "1.11");
	                this.menuMap.put(19, Llama2);
	                final WrongMobButton Evoker2 = new WrongMobButton("Evoker", "1.11");
	                this.menuMap.put(11, Evoker2);
	                final WrongMobButton ZombieHorse2 = new WrongMobButton("Zombie Horse", "1.11");
	                this.menuMap.put(45, ZombieHorse2);
	                final WrongMobButton Mule2 = new WrongMobButton("Mule", "1.11");
	                this.menuMap.put(20, Mule2);
	                final WrongMobButton Donkey2 = new WrongMobButton("Donkey", "1.11");
	                this.menuMap.put(6, Donkey2);
	                final WrongMobButton SkeletonHorse2 = new WrongMobButton("Skeleton Horse", "1.11");
	                this.menuMap.put(31, SkeletonHorse2);
	                final WrongMobButton ElderGuardian2 = new WrongMobButton("Elder Guardian", "1.11");
	                this.menuMap.put(7, ElderGuardian2);
	                final WrongMobButton WitherSkeleton2 = new WrongMobButton("Wither Skeleton", "1.11");
	                this.menuMap.put(42, WitherSkeleton2);
	                final WrongMobButton ZombieVillager2 = new WrongMobButton("Zombie Villager", "1.11");
	                this.menuMap.put(46, ZombieVillager2);       
	            }
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.RED + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    BlockedMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(52, previous);
	            final Button nextpage = new Button();
	            nextpage.setIcon(Material.NETHER_STAR);
	            nextpage.setName(ChatColor.GREEN + "Next Page \u25b6");
	            nextpage.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                	 new BlockedMenu(BlockedMenu.this,1).ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(53, nextpage);
	        	}else if(page == 1) {
	            try {
	                final MobButton Parrot = new MobButton("Parrot", EntityType.PARROT, SettingsMenu.this.config, this, true);
	                this.menuMap.put(0, Parrot);
	                final MobButton Dolphin = new MobButton("Dolphin", EntityType.DOLPHIN, SettingsMenu.this.config, this, true);
	                this.menuMap.put(1, Dolphin);
	                final MobButton Drowned = new MobButton("Drowned", EntityType.DROWNED, SettingsMenu.this.config, this, true);
	                this.menuMap.put(2, Drowned);
	                final MobButton Phantom = new MobButton("Phantom", EntityType.PHANTOM, SettingsMenu.this.config, this, true);
	                this.menuMap.put(3, Phantom);
	                final MobButton Cod = new MobButton("Cod", EntityType.COD, SettingsMenu.this.config, this, true);
	                this.menuMap.put(4, Cod);
	                final MobButton Salmon = new MobButton("Salmon", EntityType.SALMON, SettingsMenu.this.config, this, true);
	                this.menuMap.put(5, Salmon);
	                final MobButton Pufferfish = new MobButton("Pufferfish", EntityType.PUFFERFISH, SettingsMenu.this.config, this, true);
	                this.menuMap.put(6, Pufferfish);
	                final MobButton Tropicalfish = new MobButton("Tropical Fish", EntityType.TROPICAL_FISH, SettingsMenu.this.config, this, true);
	                this.menuMap.put(7, Tropicalfish);
	                final MobButton Turtle = new MobButton("Turtle", EntityType.TURTLE, SettingsMenu.this.config, this, true);
	                this.menuMap.put(8, Turtle);
	            }
	            catch (NoSuchFieldError e) {
	            	final WrongMobButton Parrot2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(0, Parrot2);
	                final WrongMobButton Dolphin2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(1, Dolphin2);
	                final WrongMobButton Drowned2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(2, Drowned2);
	                final WrongMobButton Phantom2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(3, Phantom2);
	                final WrongMobButton Cod2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(4, Cod2);
	                final WrongMobButton Salmon2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(5, Salmon2);
	                final WrongMobButton Pufferfish2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(6, Pufferfish2);
	                final WrongMobButton Tropicalfish2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(7, Tropicalfish2);
	                final WrongMobButton Turtle2 = new WrongMobButton("Unknown Mob", "1.13");
	                this.menuMap.put(8, Turtle2);
	            }
	            try {
	                final MobButton Cat = new MobButton("Cat", EntityType.CAT, SettingsMenu.this.config, this, true);
	                this.menuMap.put(9, Cat);
	                final MobButton Fox = new MobButton("Fox", EntityType.FOX, SettingsMenu.this.config, this, true);
	                this.menuMap.put(10, Fox);
	                final MobButton Panda = new MobButton("Panda", EntityType.PANDA, SettingsMenu.this.config, this, true);
	                this.menuMap.put(11, Panda);
	                final MobButton Pillager = new MobButton("Pillager", EntityType.PILLAGER, SettingsMenu.this.config, this, true);
	                this.menuMap.put(12, Pillager);
	                final MobButton Ravager = new MobButton("Ravager", EntityType.RAVAGER, SettingsMenu.this.config, this, true);
	                this.menuMap.put(13, Ravager);
	                final MobButton Trader_Llama = new MobButton("Trader's Llama", EntityType.TRADER_LLAMA, SettingsMenu.this.config, this, true);
	                this.menuMap.put(14, Trader_Llama);
	                final MobButton Wandering_Trader = new MobButton("Wandering Trader", EntityType.WANDERING_TRADER, SettingsMenu.this.config, this, true);
	                this.menuMap.put(15, Wandering_Trader);
	            }
	            catch (NoSuchFieldError e) {
	                final WrongMobButton Cat2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(8, Cat2);
	                final WrongMobButton Fox2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(10, Fox2);
	                final WrongMobButton Panda2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(11, Panda2);
	                final WrongMobButton Pillager2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(12, Pillager2);
	                final WrongMobButton Ravager2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(13, Ravager2);
	                final WrongMobButton Trader_Llama2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(14, Trader_Llama2);
	                final WrongMobButton Wandering_Trader2 = new WrongMobButton("Unknown Mob", "1.14");
	                this.menuMap.put(15, Wandering_Trader2);     
	            }
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.GREEN + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                	BlockedMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(52, previous);
	            final Button nextpage = new Button();
	            nextpage.setIcon(Material.NETHER_STAR);
	            nextpage.setName(ChatColor.GRAY + "Next Page \u25b6");
	            nextpage.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) { 
	                //    new BlockedMenu(BlockedMenu.this,2).ShowMenu(event.getInteractor()); 3rd page is off 
	                }
	            });
	            this.menuMap.put(53, nextpage);
	            
	        	}
	        }
	    }
	    
	    protected class NamingMenu extends Menu
	    {
	        private final Menu prev;
	        
	        public NamingMenu(final SettingsMenu prev) {
	            this.name = ChatColor.BLUE + "Naming " + ChatColor.YELLOW + "Settings";
	            this.generateMenu();
	            this.prev = prev;
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            this.generateMenu();
	            super.ShowMenu(player);
	        }
	        
	        public void generateMenu() {
	            final Button showNames = new Button();
	            showNames.setName(ChatColor.BLUE + "Always Show Mob Name");
	            showNames.addLoreLine(ChatColor.YELLOW + "Displays mob names even when not looking directly at mob.");
	            showNames.addLoreLine("");
	            final boolean enabled = SettingsMenu.this.config.isAlwaysShowMobName();
	            if (enabled) {
	                showNames.setIcon(Material.GLASS);
	                showNames.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Shown");
	            }
	            else {
	                showNames.setIcon(Material.RED_STAINED_GLASS);
	                showNames.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Hidden");
	            }
	            showNames.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setAlwaysShowMobName(!enabled);
	                    NamingMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            showNames.addLoreLine("");
	            showNames.addLoreLine(ChatColor.GRAY + "Click to Toggle.");
	            this.menuMap.put(0, showNames);
	            final Button language = new Button();
	            language.setName(ChatColor.BLUE + "Language Settings");
	            language.setIcon(Material.WRITABLE_BOOK);
	            language.addLoreLine("");
	            language.addLoreLine(ChatColor.YELLOW + "Select which language to use for mob names");
	            final NamingMenu ref = this;
	            language.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    new LanguageMenu(ref).ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(9, language);
	            final Button prefixLabel = new Button();
	            prefixLabel.setName(ChatColor.BLUE + "Prefix Settings");
	            prefixLabel.setIcon(Material.NAME_TAG);
	            prefixLabel.hasGlowEffect(true);
	            this.menuMap.put(3, prefixLabel);
	            final Button prefixToggle = new Button();
	            prefixToggle.setIcon(Material.WRITABLE_BOOK);
	            prefixToggle.setName(ChatColor.GREEN + "Prefix Enabled");
	            prefixToggle.addLoreLine(" ");
	            final boolean prefix = SettingsMenu.this.config.isPrefixEnabled();
	            if (prefix) {
	                prefixToggle.setIcon(Material.GREEN_WOOL);
	                prefixToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Enabled");
	            }
	            else {
	                prefixToggle.setIcon(Material.RED_WOOL);
	                prefixToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Disabled");
	            }
	            prefixToggle.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setPrefixEnabled(!prefix);
	                    NamingMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            prefixToggle.addLoreLine("");
	            prefixToggle.addLoreLine(ChatColor.GRAY + "Click to Toggle.");
	            this.menuMap.put(12, prefixToggle);
	            final Button prefixFormat = new Button();
	            prefixFormat.setIcon(Material.WRITABLE_BOOK);
	            prefixFormat.setName(ChatColor.GREEN + "Prefix Format");
	            prefixFormat.addLoreLine(" ");
	            final String prefixString = SettingsMenu.this.config.getPrefixFormat();
	            prefixFormat.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.YELLOW + prefixString);
	            prefixFormat.addLoreLine("");
	            prefixFormat.addLoreLine(ChatColor.GRAY + "Click to Change.");
	            prefixFormat.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        NamingMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        NamingMenu.menuHandler.closeMenu(event.getInteractor());
	                        final StringChangeListener stringChangeListener = new StringChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setPrefixFormat", String.class));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value, use # for the mob's level.");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(21, prefixFormat);
	            final Button suffixLabel = new Button();
	            suffixLabel.setName(ChatColor.BLUE + "Suffix Settings");
	            suffixLabel.setIcon(Material.NAME_TAG);
	            suffixLabel.hasGlowEffect(true);
	            this.menuMap.put(5, suffixLabel);
	            final Button suffixToggle = new Button();
	            suffixToggle.setIcon(Material.WRITABLE_BOOK);
	            suffixToggle.setName(ChatColor.GREEN + "Suffix Enabled");
	            suffixToggle.addLoreLine(" ");
	            final boolean suffix = SettingsMenu.this.config.isSuffixEnabled();
	            if (suffix) {
	                suffixToggle.setIcon(Material.GREEN_WOOL);
	                suffixToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Enabled");
	            }
	            else {
	                suffixToggle.setIcon(Material.RED_WOOL);
	                suffixToggle.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Disabled");
	            }
	            suffixToggle.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setSuffixEnabled(!suffix);
	                    NamingMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            suffixToggle.addLoreLine("");
	            suffixToggle.addLoreLine(ChatColor.GRAY + "Click to Toggle.");
	            this.menuMap.put(14, suffixToggle);
	            final Button suffixFormat = new Button();
	            suffixFormat.setIcon(Material.WRITABLE_BOOK);
	            suffixFormat.setName(ChatColor.GREEN + "Suffix Format");
	            suffixFormat.addLoreLine(" ");
	            final String suffixString = SettingsMenu.this.config.getSuffixFormat();
	            suffixFormat.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.YELLOW + suffixString);
	            suffixFormat.addLoreLine("");
	            suffixFormat.addLoreLine(ChatColor.GRAY + "Click to Change.");
	            suffixFormat.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        NamingMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        NamingMenu.menuHandler.closeMenu(event.getInteractor());
	                        final StringChangeListener stringChangeListener = new StringChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setSuffixFormat", String.class));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value, use # for the mob's level.");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(23, suffixFormat);
	            final Button demo = new Button();
	            demo.setIcon(Material.SKELETON_SKULL);
	            demo.setName(ChatColor.GREEN + "Example Mob Name");
	            String name = " Zombie ";
	            if (prefix) {
	                name = ChatColor.translateAlternateColorCodes('&', prefixString.replace("#", "15") + name);
	            }
	            if (suffix) {
	                name = ChatColor.translateAlternateColorCodes('&', name + suffixString.replace("#", "15"));
	            }
	            name = name.trim();
	            demo.addLoreLine(" ");
	            demo.addLoreLine(ChatColor.WHITE + name);
	            this.menuMap.put(31, demo);
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.RED + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    NamingMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(35, previous);
	        }
	    }
	    
	    protected class MobArenaMenu extends Menu
	    {
	        private final Menu prev;
	        
	        public MobArenaMenu(final SettingsMenu prev) {
	            this.name = ChatColor.BLUE + "MobArena" + ChatColor.YELLOW + " Settings";
	            this.prev = prev;
	            this.generateMenu();
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            this.generateMenu();
	            super.ShowMenu(player);
	        }
	        
	        public void generateMenu() {
	            final Button enabled = new Button();
	            enabled.setIcon(Material.WRITABLE_BOOK);
	            enabled.setName(ChatColor.GREEN + "MobArena Leveling Enabled");
	            enabled.addLoreLine(" ");
	            final boolean arenaEnabled = SettingsMenu.this.config.isMobArenaLeveled();
	            if (arenaEnabled) {
	                enabled.setIcon(Material.GREEN_WOOL);
	                enabled.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Enabled");
	            }
	            else {
	                enabled.setIcon(Material.RED_WOOL);
	                enabled.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Disabled");
	            }
	            enabled.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setMobArenaLeveled(!arenaEnabled);
	                    MobArenaMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            enabled.addLoreLine(" ");
	            enabled.addLoreLine(ChatColor.GRAY + "Click to Toggle");
	            this.menuMap.put(0, enabled);
	            final Button multiplier = new Button();
	            multiplier.setIcon(Material.WRITABLE_BOOK);
	            multiplier.setName(ChatColor.GREEN + "MobArena Experience Multiplier");
	            multiplier.addLoreLine(" ");
	            final double mult = SettingsMenu.this.config.getMobArenaMultiplier();
	            multiplier.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + mult);
	            multiplier.addLoreLine(" ");
	            multiplier.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            multiplier.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        MobArenaMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        MobArenaMenu.menuHandler.closeMenu(event.getInteractor());
	                        final DoubleChangeListener doubleChangeListener = new DoubleChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setMobArenaMultiplier", Double.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(1, multiplier);
	            final Button waveLeveling = new Button();
	            waveLeveling.setIcon(Material.WRITABLE_BOOK);
	            waveLeveling.setName(ChatColor.GREEN + "MobArena Wave Leveling Enabled");
	            waveLeveling.addLoreLine(" ");
	            final boolean waveEnabled = SettingsMenu.this.config.isMobArenaWaveLeveled();
	            if (waveEnabled) {
	                waveLeveling.setIcon(Material.GREEN_WOOL);
	                waveLeveling.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.GREEN + "Enabled");
	            }
	            else {
	                waveLeveling.setIcon(Material.RED_WOOL);
	                waveLeveling.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.RED + "Disabled");
	            }
	            waveLeveling.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    SettingsMenu.this.config.setMobArenaWaveLeveled(!waveEnabled);
	                    MobArenaMenu.this.ShowMenu(event.getInteractor());
	                }
	            });
	            waveLeveling.addLoreLine(" ");
	            waveLeveling.addLoreLine(ChatColor.GRAY + "Click to Toggle");
	            this.menuMap.put(2, waveLeveling);
	            final Button wavesPerLevel = new Button();
	            wavesPerLevel.setIcon(Material.WRITABLE_BOOK);
	            wavesPerLevel.setName(ChatColor.GREEN + "MobArena Waves Per Level");
	            wavesPerLevel.addLoreLine(" ");
	            final double waves = SettingsMenu.this.config.getMobArenaWavesPerLevel();
	            wavesPerLevel.addLoreLine(ChatColor.WHITE + "Value: " + ChatColor.LIGHT_PURPLE + waves);
	            wavesPerLevel.addLoreLine(" ");
	            wavesPerLevel.addLoreLine(ChatColor.GRAY + "Click to Change Value.");
	            wavesPerLevel.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    if (SettingsMenu.listeners.contains(event.getInteractor())) {
	                        MobArenaMenu.menuHandler.closeMenu(event.getInteractor());
	                    }
	                    try {
	                        MobArenaMenu.menuHandler.closeMenu(event.getInteractor());
	                        final DoubleChangeListener doubleChangeListener = new DoubleChangeListener(event.getInteractor(), event.getMenu(), SettingsMenu.this.config.getClass().getMethod("setMobArenaWavesPerLevel", Double.TYPE));
	                        event.getInteractor().sendMessage(ChatColor.YELLOW + "Please enter a new value: ");
	                    }
	                    catch (NoSuchMethodException e) {
	                        e.printStackTrace();
	                    }
	                }
	            });
	            this.menuMap.put(3, wavesPerLevel);
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.RED + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    MobArenaMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(8, previous);
	        }
	    }
	    
	    protected class LanguageMenu extends Menu
	    {
	        private NamingMenu prev;
	        
	        public LanguageMenu(final NamingMenu prev) {
	            this.name = ChatColor.BLUE + "Language " + ChatColor.YELLOW + "Settings";
	            this.generateMenu();
	            this.prev = prev;
	        }
	        
	        @Override
	        public void ShowMenu(final Player player) {
	            this.generateMenu();
	            super.ShowMenu(player);
	        }
	        
	        public void generateMenu() {
	            int i = 0;
	            final Language current = SettingsMenu.this.config.getMobNameLanguage();
	            for (final Language lang : Language.values()) {
	                final Button bLang = new Button();
	                if (lang == current) {
	                    bLang.setIcon(Material.GREEN_WOOL);
	                }
	                else {
	                    bLang.setIcon(Material.WHITE_WOOL);
	                }
	                bLang.setName(ChatColor.GREEN + lang.toString());
	                bLang.addLoreLine("");
	                bLang.addLoreLine(ChatColor.GRAY + "Click to use this language.");
	                bLang.setOnPressedListener(new Button.onButtonPressedListener() {
	                    @Override
	                    public void onButtonPressed(final MenuInteractionEvent event) {
	                        SettingsMenu.this.config.setMobNameLanguage(lang);
	                        LanguageMenu.this.ShowMenu(event.getInteractor());
	                    }
	                });
	                this.menuMap.put(i++, bLang);
	            }
	            final Button previous = new Button();
	            previous.setIcon(Material.NETHER_STAR);
	            previous.setName(ChatColor.RED + "\u25c0 Previous Menu");
	            previous.setOnPressedListener(new Button.onButtonPressedListener() {
	                @Override
	                public void onButtonPressed(final MenuInteractionEvent event) {
	                    LanguageMenu.this.prev.ShowMenu(event.getInteractor());
	                }
	            });
	            this.menuMap.put(35, previous);
	        }
	    }
	    
	    protected class DoubleChangeListener implements Listener
	    {
	        final Player player;
	        final Menu menu;
	        final Method method;
	        
	        public DoubleChangeListener(final Player player, final Menu menu, final Method method) {
	            this.player = player;
	            this.menu = menu;
	            this.method = method;
	            Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.RPGMobs);
	            final DoubleChangeListener instance = this;
	            if (SettingsMenu.listeners.contains(player)) {
	                SettingsMenu.menuHandler.closeMenu(player);
	            }
	            SettingsMenu.listeners.add(player);
	            Bukkit.getScheduler().runTaskLater((Plugin)Main.RPGMobs, (Runnable)new Runnable() {
	                @Override
	                public void run() {
	                    if (SettingsMenu.listeners.contains(player)) {
	                        player.sendMessage(ChatColor.RED + "Value Change Timed Out");
	                    }
	                    SettingsMenu.listeners.remove(player);
	                    HandlerList.unregisterAll((Listener)instance);
	                }
	            }, 600L);
	        }
	        
	        @EventHandler
	        public void onChat(final AsyncPlayerChatEvent event) {
	        	Player player = event.getPlayer();
	            if (player == this.player) {
	                try {
	                    final double newValue = Double.parseDouble(event.getMessage());
	                    this.method.invoke(SettingsMenu.this.config, newValue);
	                }
	                catch (NumberFormatException exception) {
	                    this.player.sendMessage(ChatColor.RED + "Unable to change value, please enter an integer");
	                }
	                catch (InvocationTargetException e) {
	                    e.printStackTrace();
	                }
	                catch (IllegalAccessException e2) {
	                    e2.printStackTrace();
	                }	              
	                this.menu.ShowMenu(player);
	                HandlerList.unregisterAll((Listener)this);
	                SettingsMenu.listeners.remove(this.player);  
	                event.setCancelled(true);
	                
	            }
	        }
	    }
	    
	    protected class IntegerChangeListener implements Listener
	    {
	        final Player player;
	        final Menu menu;
	        final Method method;
	        
	        public IntegerChangeListener(final Player player, final Menu menu, final Method method) {
	            this.player = player;
	            this.menu = menu;
	            this.method = method;
	            Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.RPGMobs);
	            final IntegerChangeListener instance = this;
	            if (SettingsMenu.listeners.contains(player)) {
	                SettingsMenu.menuHandler.closeMenu(player);
	            }
	            Bukkit.getScheduler().runTaskLater((Plugin)Main.RPGMobs, (Runnable)new Runnable() {
	                @Override
	                public void run() {
	                    if (SettingsMenu.listeners.contains(player)) {
	                        player.sendMessage(ChatColor.RED + "Value Change Timed Out");
	                    }
	                    SettingsMenu.listeners.remove(player);
	                    HandlerList.unregisterAll((Listener)instance);
	                }
	            }, 600L);
	        }
	        
	        @EventHandler
	        public void onChat(final AsyncPlayerChatEvent event) {
	            if (event.getPlayer() == this.player) {
	                try {
	                    final int newValue = Integer.parseInt(event.getMessage());
	                    this.method.invoke(SettingsMenu.this.config, newValue);
	                }
	                catch (NumberFormatException exception) {
	                    this.player.sendMessage(ChatColor.RED + "Unable to change value, please enter an integer");
	                }
	                catch (InvocationTargetException e) {
	                    e.printStackTrace();
	                }
	                catch (IllegalAccessException e2) {
	                    e2.printStackTrace();
	                }
	                this.menu.ShowMenu(event.getPlayer());
	                HandlerList.unregisterAll((Listener)this);
	                SettingsMenu.listeners.remove(this.player);
	                event.setCancelled(true);
	            }
	        }
	    }
	    
	    protected class StringChangeListener implements Listener
	    {
	        final Player player;
	        final Menu menu;
	        final Method method;
	        
	        public StringChangeListener(final Player player, final Menu menu, final Method method) {
	            this.player = player;
	            this.menu = menu;
	            this.method = method;
	            Bukkit.getPluginManager().registerEvents((Listener)this, (Plugin)Main.RPGMobs);
	            final StringChangeListener instance = this;
	            if (SettingsMenu.listeners.contains(player)) {
	                SettingsMenu.menuHandler.closeMenu(player);
	            }
	            Bukkit.getScheduler().runTaskLater((Plugin)Main.RPGMobs, (Runnable)new Runnable() {
	                @Override
	                public void run() {
	                    if (SettingsMenu.listeners.contains(player)) {
	                        player.sendMessage(ChatColor.RED + "Value Change Timed Out");
	                    }
	                    SettingsMenu.listeners.remove(player);
	                    HandlerList.unregisterAll((Listener)instance);
	                }
	            }, 600L);
	        }
	        
	        @EventHandler
	        public void onChat(final AsyncPlayerChatEvent event) {
	            if (event.getPlayer() == this.player) {
	                final String newValue = event.getMessage();
	                try {
	                    this.method.invoke(SettingsMenu.this.config, newValue);
	                }
	                catch (IllegalAccessException e) {
	                    e.printStackTrace();
	                }
	                catch (InvocationTargetException e2) {
	                    e2.printStackTrace();
	                }
	                this.menu.ShowMenu(event.getPlayer());
	                HandlerList.unregisterAll((Listener)this);
	                SettingsMenu.listeners.remove(this.player);
	                event.setCancelled(true);
	            }
	        }
	    }
}
