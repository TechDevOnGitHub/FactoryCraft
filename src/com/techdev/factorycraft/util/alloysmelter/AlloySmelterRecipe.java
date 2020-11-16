package com.techdev.factorycraft.util.alloysmelter;

import org.bukkit.inventory.ItemStack;

public class AlloySmelterRecipe {

    public static AlloySmelterRecipe[] recipes = new AlloySmelterRecipe[0];

    private ItemStack[] validFuelItems;
    private ItemStack firstIngredient;
    private ItemStack secondIngredient;
    private ItemStack result;
    private float craftingDuration;
    private String recipeName;

    public AlloySmelterRecipe(ItemStack[] validFuelItems, ItemStack ingredientOne, ItemStack ingredientTwo, ItemStack result, float craftingDuration, String recipeName)
    {
        this.validFuelItems = validFuelItems;
        this.firstIngredient = ingredientOne;
        this.secondIngredient = ingredientTwo;
        this.result = result;
        this.craftingDuration = craftingDuration;
        this.recipeName = recipeName;

        AlloySmelterRecipe[] newRecipes = new AlloySmelterRecipe[recipes.length + 1];
        System.arraycopy(recipes, 0, newRecipes, 0, recipes.length);
        newRecipes[recipes.length] = this;
        recipes = newRecipes;
    }

    public ItemStack[] getValidFuelItems() { return this.validFuelItems; }

    public ItemStack getFirstIngredient() { return this.firstIngredient; }

    public ItemStack getSecondIngredient() { return this.secondIngredient; }

    public ItemStack getResult() { return this.result; }

    public float getCraftingDuration() { return this.craftingDuration; }

    public String getRecipeName() { return this.recipeName; }

}
