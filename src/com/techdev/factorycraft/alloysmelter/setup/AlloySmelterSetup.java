package com.techdev.factorycraft.alloysmelter.setup;

import com.techdev.factorycraft.alloysmelter.util.AlloySmelterRecipe;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import static com.techdev.factorycraft.alloysmelter.menus.AlloySmelterGui.*;

public class AlloySmelterSetup {

    public void setup()
    {
        registerRecipes();
        setNonStaticItemSlots();
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
            4.0f,
            "Crying Obsidian"
        );

        new AlloySmelterRecipe(
                fuelItems_common,
                new ItemStack(Material.DIRT),
                new ItemStack(Material.GHAST_TEAR, 2),
                new ItemStack(Material.DIAMOND_BLOCK),
                4.0f,
                "Cry"
        );
    }

    public void setNonStaticItemSlots()
    {
        for (int inputSlot : inputSlots) {
            nonStaticItemSlots.add(inputSlot);
        }
        for (int outputSlot : outputSlots) {
            nonStaticItemSlots.add(outputSlot);
        }
        nonStaticItemSlots.add(hookSlot);
    }

}
