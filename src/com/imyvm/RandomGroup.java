package com.imyvm;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class RandomGroup extends JavaPlugin {

    private static final Logger log = Logger.getLogger("RandomGroup");
    private FileConfiguration config = getConfig();
    public List<String> names = new ArrayList<>();


    @Override
    public void onDisable() {
        log.info(String.format("[%s] Disabled Version %s", getDescription().getName(),
                getDescription().getVersion()));
    }

    @Override
    public void onEnable() {
        log.info(String.format("[%s] Enabled Version %s", getDescription().getName(), getDescription().getVersion()));

        config.addDefault("Groups", names);
        config.options().copyDefaults(true);
        saveConfig();

        names = config.getStringList("Groups");

        getCommand("rgroup").setExecutor(new Commands(this));
    }
}
