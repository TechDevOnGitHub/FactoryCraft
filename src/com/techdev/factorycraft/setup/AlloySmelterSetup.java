package com.techdev.factorycraft.setup;

import com.techdev.factorycraft.util.alloysmelter.AlloySmelterRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class AlloySmelterSetup {

    public void setup()
    {
        registerRecipes();
    }

    public void registerRecipes()
    {
        ItemStack[] fuelItems_common =
        {
            new ItemStack(Material.COAL),
            new ItemStack(Material.CHARCOAL)
        };

        new AlloySmelterRecipe(
            fuelItems_common,
            new ItemStack(Material.OBSIDIAN),
            new ItemStack(Material.GHAST_TEAR, 2),
            new ItemStack(Material.CRYING_OBSIDIAN),
            20.0f,
            "Crying Obsidian"
        );
    }

}
