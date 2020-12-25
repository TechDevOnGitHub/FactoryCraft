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
            20.0f,
            "Crying Obsidian"
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
