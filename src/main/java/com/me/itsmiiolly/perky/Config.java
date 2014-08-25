package com.me.itsmiiolly.perky;

import java.io.File;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Class to handle the getting of {@link org.bukkit.configuration.file.YamlConfiguration} options.
 */
public class Config {

    private static FileConfiguration config;
    private static Plugin plugin;

    /**
     * Load the plugin's {@link org.bukkit.configuration.file.YamlConfiguration}
     *
     * @param p The plugin's instance.
     */
    public static void load(Plugin p) {
        plugin = p;
        p.saveDefaultConfig();
        config = p.getConfig();
    }

    private static void save() {
        try {
            config.save(new File(plugin.getDataFolder(), "config.yml"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static class Modules {
        public static List<String> getEnabledModules() {
            return config.getStringList("enabled-modules");
        }
    }
}
