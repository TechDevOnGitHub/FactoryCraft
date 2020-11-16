package com.techdev.factorycraft.main;

import com.techdev.factorycraft.listeners.AlloySmelterEvents;
import com.techdev.factorycraft.setup.AlloySmelterSetup;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable()
    {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AlloySmelterEvents(), this);

        new AlloySmelterSetup().setup();
    }

}
