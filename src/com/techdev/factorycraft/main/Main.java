package com.techdev.factorycraft.main;

import com.techdev.factorycraft.alloysmelter.listeners.AlloySmelterEvents;
import com.techdev.factorycraft.alloysmelter.setup.AlloySmelterSetup;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable()
    {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AlloySmelterEvents(), this);
        pluginManager.registerEvents(this, this);

        new AlloySmelterSetup().setup();

        File file = new File("plugins//FactoryCraft");
        file.mkdir();
    }
}
