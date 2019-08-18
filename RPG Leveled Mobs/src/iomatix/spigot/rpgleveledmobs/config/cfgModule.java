package iomatix.spigot.rpgleveledmobs.config;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.io.File;
import java.util.HashSet;
import java.util.UUID;
import java.util.HashMap;

import org.bukkit.World;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.io.Files;
import iomatix.spigot.rpgleveledmobs.Main;

public class cfgModule {

    private static cfgModule instance;
	
	
	
    public static cfgModule getConfigModule() {
        if (cfgModule.instance == null) {
            new cfgModule();
        }
        return cfgModule.instance;
    }
}
