package iomatix.spigot.rpgleveledmobs.config;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.ConfigurationSection;

public class configHandler {

private String fileName;
private JavaPlugin plugin;
private File ConfigFile;
private FileConfiguration fileConfiguration;
	
public void Config(final JavaPlugin plugin, final String name){
this.plugin = plugin;
this.fileName = name +".yml";
this.ConfigFile = new File(plugin.getDataFolder(), this.fileName);
try {
final String path = this.ConfigFile.getAbsolutePath();	
if (new File(path.substring(0,path.lastIndexOf(File.pathSeparator))).mkdir()) plugin.getLogger().info("New folder for config files created.");
		}catch(Exception ex) {}
	}

public File getConfigFile() {
return this.ConfigFile;	
}


public void reloadConfig() {
    this.fileConfiguration = (FileConfiguration)YamlConfiguration.loadConfiguration(this.ConfigFile);
    final InputStream defConfigStream = this.plugin.getResource(this.fileName);
    if (defConfigStream != null) {
        final YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(this.ConfigFile);
        this.fileConfiguration.setDefaults((Configuration)defConfig);
    }
}

public FileConfiguration getConfig() {
    if (this.fileConfiguration == null) this.reloadConfig();
    
    return this.fileConfiguration;
}

public void saveConfig() {
    if (this.fileConfiguration == null) {
        if (this.ConfigFile == null) return;
        
    }
    try {
        this.getConfig().save(this.ConfigFile);
    }
    catch (IOException ex) {
        this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.ConfigFile, ex);
    }
}

public void saveDefaultConfig() {
    if (this.ConfigFile == null)  this.ConfigFile = new File(this.plugin.getDataFolder().getAbsolutePath() + "/" + this.fileName);
    if (!this.ConfigFile.exists())  this.plugin.saveResource(this.fileName, false);

}

public void setDefaults(final ConfigurationSection config) {
    if (config.getDefaultSection() == null)  return;   
    for (final String key : config.getDefaultSection().getKeys(false)) {
        if (config.isConfigurationSection(key)) {
            this.setDefaults(config.getConfigurationSection(key));
        }
        else {
            if (config.isSet(key))  continue;
            
            config.set(key, config.get(key));
        }

}
    
}

}