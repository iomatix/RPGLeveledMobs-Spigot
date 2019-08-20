package iomatix.spigot.rpgleveledmobs.config;

import java.util.Iterator;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.entity.EntityType;
import java.util.ArrayList;

import iomatix.spigot.rpgleveledmobs.logging.LogsModule;
import iomatix.spigot.rpgleveledmobs.Main;
import iomatix.spigot.rpgleveledmobs.tools.Language;

public class GlobalConfig extends RPGLeveledMobsConfig{
	    private boolean useSuffix;
	    private boolean usePrefix;
	    private String suffixFormat;
	    private String prefixFormat;
	    private Language language;
	    private double distancePerLevel;
	    private int maxLevel;
	    private int minLevel;
	    private int startLevel;
	    private boolean alwaysShowMobName;
	    private boolean damageModified;
	    private boolean defenseModified;
	    private boolean experienceModified;
	    private boolean healthModified;
	    private double healthMultiplier;
	    private double damageMultiplier;
	    private double defenseMultiplier;
	    private double experienceModifier;
	    private boolean leveledMobArea;
	    private boolean mobArenaWaveLeveling;
	    private double mobArenaMultiplier;
	    private double wavesPerLevel;
	    private ArrayList<EntityType> leveledMobs;
	    private ArrayList<EntityType> blockedMobs;
	    private boolean leveledSpawners;
	    
	    public GlobalConfig() {
	        this.config = new configHandler(Main.RPGMobs, "config");
	        this.setDefaults();
	        this.loadConfig();
	    }
	    
	    private void loadConfig() {
	        LogsModule.setDebugEnabled(this.config.getConfig().getBoolean("Debug", false));
	        LogsModule.setLoggingEnabled(this.config.getConfig().getBoolean("Log", false));
	        if (this.config.getConfig().contains(ConfigKey.LEVELED_MOBS.toString())) {
	            final ArrayList<EntityType> allowed = new ArrayList<EntityType>();
	            final ArrayList<String> temp = (ArrayList<String>)this.config.getConfig().get(ConfigKey.LEVELED_MOBS.toString());
	            for (final String mob : temp) {
	                allowed.add(EntityType.valueOf(mob));
	            }
	            this.leveledMobs = allowed;
	        }
	        else {
	            final ArrayList<EntityType> allowed = ConfigKey.defaultLeveled;
	            final ArrayList<String> temp = new ArrayList<String>();
	            for (final EntityType ent : allowed) {
	                temp.add(ent.toString());
	            }
	            this.config.getConfig().set(ConfigKey.LEVELED_MOBS.toString(), (Object)temp);
	        }
	        if (this.config.getConfig().contains(ConfigKey.BLOCKED_MOBS.toString())) {
	            final ArrayList<EntityType> blocked = new ArrayList<EntityType>();
	            final ArrayList<String> temp = (ArrayList<String>)this.config.getConfig().get(ConfigKey.BLOCKED_MOBS.toString(), (Object)new ArrayList());
	            for (final String mob : temp) {
	                blocked.add(EntityType.valueOf(mob));
	            }
	            this.blockedMobs = blocked;
	        }
	        else {
	            final ArrayList<EntityType> blocked = ConfigKey.getDefaultBlocked(null);
	            final ArrayList<String> temp = new ArrayList<String>();
	            for (final EntityType ent : blocked) {
	                temp.add(ent.toString());
	            }
	            this.config.getConfig().set(ConfigKey.BLOCKED_MOBS.toString(), (Object)temp);
	        }
	        if (this.config.getConfig().contains(ConfigKey.LEVELED_SPAWNERS.toString())) {
	            this.leveledSpawners = this.config.getConfig().getBoolean(ConfigKey.LEVELED_SPAWNERS.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.MIN_LEVEL.toString())) {
	            this.minLevel = this.config.getConfig().getInt(ConfigKey.MIN_LEVEL.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.MAX_LEVEL.toString())) {
	            this.maxLevel = this.config.getConfig().getInt(ConfigKey.MAX_LEVEL.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.START_LEVEL.toString())) {
	            this.startLevel = this.config.getConfig().getInt(ConfigKey.START_LEVEL.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.DISTANCE_PER_LEVEL.toString())) {
	            this.distancePerLevel = this.config.getConfig().getDouble(ConfigKey.DISTANCE_PER_LEVEL.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.HEALTH_MOD_ENABLED.toString())) {
	            this.healthModified = this.config.getConfig().getBoolean(ConfigKey.HEALTH_MOD_ENABLED.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.HEALTH_PER_LEVEL.toString())) {
	            this.healthMultiplier = this.config.getConfig().getDouble(ConfigKey.HEALTH_PER_LEVEL.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.DAMAGE_MOD_ENABLE.toString())) {
	            this.damageModified = this.config.getConfig().getBoolean(ConfigKey.DAMAGE_MOD_ENABLE.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.DAMAGE_PER_LEVEL.toString())) {
	            this.damageMultiplier = this.config.getConfig().getDouble(ConfigKey.DAMAGE_PER_LEVEL.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.DEFENSE_MOD_ENABLE.toString())) {
	            this.defenseModified = this.config.getConfig().getBoolean(ConfigKey.DEFENSE_MOD_ENABLE.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.DEFENSE_PER_LEVEL.toString())) {
	            this.defenseMultiplier = this.config.getConfig().getDouble(ConfigKey.DEFENSE_PER_LEVEL.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.EXPERIENCE_MOD_ENABLED.toString())) {
	            this.experienceModified = this.config.getConfig().getBoolean(ConfigKey.EXPERIENCE_MOD_ENABLED.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.EXPERIENCE_MODIFIER.toString())) {
	            this.experienceModifier = this.config.getConfig().getDouble(ConfigKey.EXPERIENCE_MODIFIER.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.USE_SUFFIX.toString())) {
	            this.useSuffix = this.config.getConfig().getBoolean(ConfigKey.USE_SUFFIX.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.USE_PREFIX.toString())) {
	            this.usePrefix = this.config.getConfig().getBoolean(ConfigKey.USE_PREFIX.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.SUFFIX_FORMAT.toString())) {
	            this.suffixFormat = this.config.getConfig().getString(ConfigKey.SUFFIX_FORMAT.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.PREFIX_FORMAT.toString())) {
	            this.prefixFormat = this.config.getConfig().getString(ConfigKey.PREFIX_FORMAT.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.NAME_LANGUAGE.toString())) {
	            this.language = Language.valueOf(this.config.getConfig().getString(ConfigKey.NAME_LANGUAGE.toString()).toUpperCase());
	        }
	        if (this.config.getConfig().contains(ConfigKey.MOB_ARENA_ENABLED.toString())) {
	            this.leveledMobArea = this.config.getConfig().getBoolean(ConfigKey.MOB_ARENA_ENABLED.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.MOB_ARENA_WAVE_LEVELING.toString())) {
	            this.mobArenaWaveLeveling = this.config.getConfig().getBoolean(ConfigKey.MOB_ARENA_WAVE_LEVELING.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.MOB_ARENA_MULTIPLIER.toString())) {
	            this.mobArenaMultiplier = this.config.getConfig().getDouble(ConfigKey.MOB_ARENA_MULTIPLIER.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.ALWAYS_SHOW_MOB_NAME.toString())) {
	            this.alwaysShowMobName = this.config.getConfig().getBoolean(ConfigKey.ALWAYS_SHOW_MOB_NAME.toString());
	        }
	        if (this.config.getConfig().contains(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL.toString())) {
	            this.wavesPerLevel = this.config.getConfig().getDouble(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL.toString());
	        }
	        this.config.saveConfig();
	    }
	    
	    private void setDefaults() {
	        for (final ConfigKey key : ConfigKey.defaultMap.keySet()) {
	            if (key != ConfigKey.BLOCKED_MOBS && key != ConfigKey.LEVELED_MOBS && key != ConfigKey.NAME_LANGUAGE && !this.config.getConfig().contains(key.toString())) {
	                this.config.getConfig().set(key.toString(), ConfigKey.defaultMap.get(key));
	            }
	        }
	        if (!this.config.getConfig().contains(ConfigKey.BLOCKED_MOBS.toString())) {
	            this.config.getConfig().set(ConfigKey.BLOCKED_MOBS.toString(), (Object)this.entListToStringList(ConfigKey.getDefaultBlocked(null)));
	        }
	        if (!this.config.getConfig().contains(ConfigKey.LEVELED_MOBS.toString())) {
	            this.config.getConfig().set(ConfigKey.LEVELED_MOBS.toString(), (Object)this.entListToStringList(ConfigKey.defaultLeveled));
	        }
	        if (!this.config.getConfig().contains(ConfigKey.NAME_LANGUAGE.toString())) {
	            this.config.getConfig().set(ConfigKey.NAME_LANGUAGE.toString(), (Object) ConfigKey.defaultMap.get(ConfigKey.NAME_LANGUAGE).toString());
	        }
	        this.config.saveConfig();
	    }
	    
	    @Override
	    public boolean isSuffixEnabled() {
	        return this.useSuffix;
	    }
	    
	    @Override
	    public void setSuffixEnabled(final boolean useSuffix) {
	        this.useSuffix = useSuffix;
	        this.config.getConfig().set(ConfigKey.USE_SUFFIX.toString(), (Object)useSuffix);
	        this.config.saveConfig();
	    }
	    
	    @Override
	    public boolean isPrefixEnabled() {
	        return this.usePrefix;
	    }
	    
	    @Override
	    public void setPrefixEnabled(final boolean usePrefix) {
	        this.usePrefix = usePrefix;
	        this.config.getConfig().set(ConfigKey.USE_PREFIX.toString(), (Object)usePrefix);
	        this.config.saveConfig();
	    }
	    
	    @Override
	    public String getSuffixFormat() {
	        return this.suffixFormat;
	    }
	    
	    @Override
	    public void setSuffixFormat(final String suffixFormat) {
	        this.suffixFormat = suffixFormat;
	        this.config.getConfig().set(ConfigKey.SUFFIX_FORMAT.toString(), (Object)suffixFormat);
	        this.config.saveConfig();
	    }
	    
	    @Override
	    public String getPrefixFormat() {
	        return this.prefixFormat;
	    }
	    
	    @Override
	    public void setPrefixFormat(final String prefixFormat) {
	        this.prefixFormat = prefixFormat;
	        this.config.getConfig().set(ConfigKey.PREFIX_FORMAT.toString(), (Object)prefixFormat);
	        this.config.saveConfig();
	    }
	    
	    @Override
	    public void setDamageModified(final boolean enabled) {
	        this.damageModified = enabled;
	        this.config.getConfig().set(ConfigKey.DAMAGE_MOD_ENABLE.toString(), (Object)this.damageModified);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void setDefenseModified(final boolean enabled) {
	        this.defenseModified = enabled;
	        this.config.getConfig().set(ConfigKey.DEFENSE_MOD_ENABLE.toString(), (Object)this.defenseModified);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public boolean isDamageModified() {
	        return this.damageModified;
	    }
	    
	    @Override
	    public boolean isDefenseModified() {
	        return this.defenseModified;
	    }
	    
	    @Override
	    public boolean isHealthModified() {
	        return this.healthModified;
	    }
	    
	    @Override
	    public void setHealthModified(final boolean enabled) {
	        this.healthModified = enabled;
	        this.config.getConfig().set(ConfigKey.HEALTH_MOD_ENABLED.toString(), (Object)enabled);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void setMobArenaLeveled(final boolean enabled) {
	        this.leveledMobArea = enabled;
	        this.config.getConfig().set(ConfigKey.MOB_ARENA_ENABLED.toString(), (Object)enabled);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public boolean isMobArenaLeveled() {
	        return this.leveledMobArea;
	    }
	    
	    @Override
	    public void setMobArenaWaveLeveled(final boolean enabled) {
	        this.mobArenaWaveLeveling = enabled;
	        this.config.getConfig().set(ConfigKey.MOB_ARENA_WAVE_LEVELING.toString(), (Object)enabled);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public boolean isMobArenaWaveLeveled() {
	        return this.mobArenaWaveLeveling;
	    }
	    
	    @Override
	    public double getDistancePerLevel() {
	        return this.distancePerLevel;
	    }
	    
	    @Override
	    public void setDistancePerLevel(final double distance) {
	        this.distancePerLevel = distance;
	        this.config.getConfig().set(ConfigKey.DISTANCE_PER_LEVEL.toString(), (Object)distance);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public int getStartLevel() {
	        return this.startLevel;
	    }
	    
	    @Override
	    public void setStartLevel(final int startLevel) {
	        this.startLevel = startLevel;
	        this.config.getConfig().set(ConfigKey.START_LEVEL.toString(), (Object)startLevel);
	        this.config.saveConfig();
	    }
	    
	    @Override
	    public int getMaxLevel() {
	        return this.maxLevel;
	    }
	    
	    @Override
	    public void setMaxLevel(final int maxLevel) {
	        this.maxLevel = maxLevel;
	        this.config.getConfig().set(ConfigKey.MAX_LEVEL.toString(), (Object)maxLevel);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void setExperienceModified(final boolean enabled) {
	        this.experienceModified = enabled;
	        this.config.getConfig().set(ConfigKey.EXPERIENCE_MOD_ENABLED.toString(), (Object)enabled);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void setExperienceMultiplier(final double multiplier) {
	        this.experienceModifier = multiplier;
	        this.config.getConfig().set(ConfigKey.EXPERIENCE_MODIFIER.toString(), (Object)multiplier);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public double getExperienceMultiplier() {
	        return this.experienceModifier;
	    }
	    
	    @Override
	    public void setHealthMultiplier(final double multiplier) {
	        this.healthMultiplier = multiplier;
	        this.config.getConfig().set(ConfigKey.HEALTH_PER_LEVEL.toString(), (Object)multiplier);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public double getHealthMultiplier() {
	        return this.healthMultiplier;
	    }
	    
	    @Override
	    public void setDamageMultiplier(final double multiplier) {
	        this.damageMultiplier = multiplier;
	        this.config.getConfig().set(ConfigKey.DAMAGE_PER_LEVEL.toString(), (Object)multiplier);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public double getDamageMultiplier() {
	        return this.damageMultiplier;
	    }
	    
	    @Override
	    public void setDefenseMultiplier(final double multiplier) {
	        this.defenseMultiplier = multiplier;
	        this.config.getConfig().set(ConfigKey.DEFENSE_PER_LEVEL.toString(), (Object)multiplier);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public double getDefenseMultiplier() {
	        return this.defenseMultiplier;
	    }
	    
	    @Override
	    public double getMobArenaMultiplier() {
	        return this.mobArenaMultiplier;
	    }
	    
	    @Override
	    public void setMobArenaMultiplier(final double mult) {
	        this.mobArenaMultiplier = mult;
	        this.config.getConfig().set(ConfigKey.MOB_ARENA_MULTIPLIER.toString(), (Object)mult);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void setLeveledMobs(final ArrayList<EntityType> allowed) {
	        this.leveledMobs = (ArrayList<EntityType>)allowed.clone();
	        final ArrayList<String> temp = new ArrayList<String>();
	        for (final EntityType type : this.leveledMobs) {
	            temp.add(type.toString());
	        }
	        this.config.getConfig().set(ConfigKey.LEVELED_MOBS.toString(), (Object)temp);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void addLeveledMob(final EntityType ent) {
	        if (!this.leveledMobs.contains(ent)) {
	            this.leveledMobs.add(ent);
	        }
	        this.config.getConfig().set(ConfigKey.LEVELED_MOBS.toString(), (Object)this.entListToStringList(this.leveledMobs));
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void removeLeveledMob(final EntityType ent) {
	        if (this.leveledMobs.contains(ent)) {
	            this.leveledMobs.remove(ent);
	            this.config.getConfig().set(ConfigKey.LEVELED_MOBS.toString(), (Object)this.entListToStringList(this.leveledMobs));
	            this.config.saveConfig();
	            Main.RPGMobs.getConfigModule().globalUpdate();
	        }
	    }
	    
	    @Override
	    public void setBlockedMobs(final ArrayList<EntityType> blocked) {
	        this.blockedMobs = (ArrayList<EntityType>)blocked.clone();
	        final ArrayList<String> temp = new ArrayList<String>();
	        for (final EntityType type : this.blockedMobs) {
	            temp.add(type.toString());
	        }
	        this.config.getConfig().set(ConfigKey.BLOCKED_MOBS.toString(), (Object)temp);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void addBlockedMob(final EntityType ent) {
	        if (!this.blockedMobs.contains(ent)) {
	            this.blockedMobs.add(ent);
	        }
	        this.config.getConfig().set(ConfigKey.BLOCKED_MOBS.toString(), (Object)this.entListToStringList(this.blockedMobs));
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void removeBlockedMob(final EntityType ent) {
	        this.blockedMobs.remove(ent);
	        this.config.getConfig().set(ConfigKey.BLOCKED_MOBS.toString(), (Object)this.entListToStringList(this.blockedMobs));
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void useInheritedValue(final ConfigKey key) {
	    }
	    
	    @Override
	    public void setMinLevel(final int level) {
	        this.minLevel = level;
	        this.config.getConfig().set(ConfigKey.MIN_LEVEL.toString(), (Object)level);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public double getMobArenaWavesPerLevel() {
	        return this.wavesPerLevel;
	    }
	    
	    @Override
	    public void setMobArenaWavesPerLevel(final double waves) {
	        this.wavesPerLevel = waves;
	        this.config.getConfig().set(ConfigKey.MOB_ARENA_WAVES_PER_LEVEL.toString(), (Object)waves);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public int getMinLevel() {
	        return this.minLevel;
	    }
	    
	    @Override
	    public boolean isExperienceModified() {
	        return this.experienceModified;
	    }
	    
	    @Override
	    public boolean isBlocked(final EntityType ent) {
	        return this.blockedMobs.contains(ent);
	    }
	    
	    @Override
	    public boolean canLevel(final EntityType ent) {
	        return this.leveledMobs.contains(ent);
	    }
	    
	    @Override
	    public ArrayList<EntityType> getLeveledMobs() {
	        return this.leveledMobs;
	    }
	    
	    @Override
	    public ArrayList<EntityType> getBlockedMobs() {
	        return this.blockedMobs;
	    }
	    
	    @Override
	    public boolean isLeveledSpawners() {
	        return this.leveledSpawners;
	    }
	    
	    @Override
	    public void setLeveledSpawners(final boolean leveledSpawners) {
	        this.leveledSpawners = leveledSpawners;
	        this.config.getConfig().set(ConfigKey.LEVELED_SPAWNERS.toString(), (Object)leveledSpawners);
	    }
	    
	    @Override
	    public boolean isAlwaysShowMobName() {
	        return this.alwaysShowMobName;
	    }
	    
	    @Override
	    public void setAlwaysShowMobName(final boolean enabled) {
	        this.alwaysShowMobName = enabled;
	        this.config.getConfig().set(ConfigKey.ALWAYS_SHOW_MOB_NAME.toString(), (Object)this.alwaysShowMobName);
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public void setMobNameLanguage(final Language lang) {
	        this.language = lang;
	        this.config.getConfig().set(ConfigKey.NAME_LANGUAGE.toString(), (Object)this.language.getName());
	        this.config.saveConfig();
	        Main.RPGMobs.getConfigModule().globalUpdate();
	    }
	    
	    @Override
	    public Language getMobNameLanguage() {
	        if (this.inheritedValues.containsKey(ConfigKey.NAME_LANGUAGE)) {
	            return (Language) this.inheritedValues.get(ConfigKey.NAME_LANGUAGE);
	        }
	        return this.language;
	    }
}
