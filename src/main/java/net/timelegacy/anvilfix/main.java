package net.timelegacy.anvilfix;

import org.bukkit.plugin.java.JavaPlugin;

public class main extends JavaPlugin {
    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new AnvilListener(this), this);

        getLogger().info("Anvilfix has been enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Anvilfix has been disabled!");
    }
}
