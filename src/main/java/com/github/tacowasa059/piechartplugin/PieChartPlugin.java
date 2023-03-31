package com.github.tacowasa059.piechartplugin;

import com.github.tacowasa059.piechartplugin.commands.PieChartCommand;
import com.github.tacowasa059.piechartplugin.commands.PieChartTabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

public final class PieChartPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("PieChart").setExecutor(new PieChartCommand(this));
        getCommand("PieChart").setTabCompleter(new PieChartTabCompleter());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
