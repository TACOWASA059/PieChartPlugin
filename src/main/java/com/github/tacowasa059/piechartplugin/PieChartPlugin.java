package com.github.tacowasa059.piechartplugin;

import com.github.tacowasa059.piechartplugin.commands.getPieChart;
import com.github.tacowasa059.piechartplugin.commands.setThresholdDistance;
import org.bukkit.plugin.java.JavaPlugin;

public final class PieChartPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("PieChart").setExecutor(new getPieChart(this));
        getCommand("setThresholdDistance").setExecutor(new setThresholdDistance(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
