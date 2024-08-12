package net.timelegacy.anvilfix;

import org.bukkit.plugin.java.JavaPlugin;

public class AnvilFix extends JavaPlugin {
    private boolean loggingEnabled;

    @Override
    public void onEnable() {
        // Save the default config if it doesn't exist  
        saveDefaultConfig();

        // Load the logging setting
        loggingEnabled = getConfig().getBoolean("enable-logging", true);

        // Register the event listener
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);

        getLogger().info("Anvilfix has been enabled!");
    }

    public boolean isLoggingEnabled() {
        return loggingEnabled;
    }

    @Override
    public void onDisable() {
        getLogger().info("Anvilfix has been disabled!");
    }
}
