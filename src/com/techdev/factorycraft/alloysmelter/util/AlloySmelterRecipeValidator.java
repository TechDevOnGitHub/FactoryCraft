package com.techdev.factorycraft.alloysmelter.util;

import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

public class AlloySmelterRecipeValidator {

    public boolean isValidRecipe;
    public String recipeName;
    public float totalRecipeCraftingCuration;
    public int recipeAmount;
    public int[] minRecipeItemAmounts;
    public ItemStack resultItem;
    public int resultItemAmount;

    public AlloySmelterRecipeValidator(InventoryView inventoryView, int[] inputSlots, int[] outputSlots)
    {
        boolean isValidFuel = false;
        boolean isValidIngredients = false;
        boolean isMinItemAmounts = false;
        int fuelIndex = 0;

        for (int i = 0; i < AlloySmelterRecipe.recipes.length; i++) {

            for (int j = 0; j < AlloySmelterRecipe.recipes[i].getValidFuelItems().length; j++) {
                if(isSimilarItem(AlloySmelterRecipe.recipes[i].getValidFuelItems()[j], inventoryView.getItem(inputSlots[0]))) {
                    isValidFuel = true;
                    fuelIndex = j;
                    break;
                }
            }

            if(isSimilarItem(inventoryView.getItem(inputSlots[1]), AlloySmelterRecipe.recipes[i].getFirstIngredient()) &&
                    isSimilarItem(inventoryView.getItem(inputSlots[2]), AlloySmelterRecipe.recipes[i].getSecondIngredient()))
            {
                isValidIngredients = true;
            }

            if(isValidFuel && isValidIngredients) {
                if(isMinAmount(inventoryView, inputSlots, AlloySmelterRecipe.recipes[i], fuelIndex)) {
                    isMinItemAmounts = true;
                }
            }


            if(isValidFuel && isValidIngredients && isMinItemAmounts)
            {
                this.totalRecipeCraftingCuration = AlloySmelterRecipe.recipes[i].getCraftingDuration();
                this.recipeName = AlloySmelterRecipe.recipes[i].getRecipeName();
                this.recipeAmount = getMaxRecipeCraftingAmount(inventoryView, inputSlots, AlloySmelterRecipe.recipes[i], fuelIndex);
                this.totalRecipeCraftingCuration *= recipeAmount;
                this.minRecipeItemAmounts = getMinRecipeItemAmounts(i, fuelIndex);
                this.resultItem = AlloySmelterRecipe.recipes[i].getResult();
                this.resultItemAmount = AlloySmelterRecipe.recipes[i].getResult().getAmount();
                break;
            }

        }
        this.isValidRecipe = isValidFuel && isValidIngredients && isMinItemAmounts;
    }

    public AlloySmelterRecipeValidator() {      }

    public int[] getMinRecipeItemAmounts(int recipeIndex, int fuelIndex)
    {
        return new int[] {
            AlloySmelterRecipe.recipes[recipeIndex].getValidFuelItems()[fuelIndex].getAmount(),
            AlloySmelterRecipe.recipes[recipeIndex].getFirstIngredient().getAmount(),
            AlloySmelterRecipe.recipes[recipeIndex].getSecondIngredient().getAmount()
        };
    }

    public boolean isSimilarItem(ItemStack item1, ItemStack item2)
    {
        boolean result;
        if(item1.getItemMeta().hasCustomModelData() && item2.getItemMeta().hasCustomModelData()) {
            result = item1.getType() == item2.getType() &&
                    item1.getItemMeta().getDisplayName() == item2.getItemMeta().getDisplayName() &&
                    item1.getItemMeta().getCustomModelData() == item2.getItemMeta().getCustomModelData();
        } else {
            result = item1.getType() == item2.getType() &&
                    item1.getItemMeta().getDisplayName() == item2.getItemMeta().getDisplayName();
        }
        if((item1.getItemMeta().hasCustomModelData() && !item2.getItemMeta().hasCustomModelData()) || (!item1.getItemMeta().hasCustomModelData() && item2.getItemMeta().hasCustomModelData())) {
            result = false;
        }
        return result;
    }

    public boolean isMinAmount(InventoryView inventoryView, int[] inputSlots, AlloySmelterRecipe recipe, int fuelItemIndex)
    {
        return inventoryView.getItem(inputSlots[0]).getAmount() >= recipe.getValidFuelItems()[fuelItemIndex].getAmount()
                && inventoryView.getItem(inputSlots[1]).getAmount() >= recipe.getFirstIngredient().getAmount()
                && inventoryView.getItem(inputSlots[2]).getAmount() >= recipe.getSecondIngredient().getAmount();
    }

    public int getMaxRecipeCraftingAmount(InventoryView inventoryView, int[] inputSlots, AlloySmelterRecipe recipe, int fuelIndex)
    {
        int[] recipeAmountValues = {recipe.getValidFuelItems()[fuelIndex].getAmount(), recipe.getFirstIngredient().getAmount(), recipe.getSecondIngredient().getAmount()};
        int[] inputAmountValues = {inventoryView.getItem(inputSlots[0]).getAmount(), inventoryView.getItem(inputSlots[1]).getAmount(), inventoryView.getItem(inputSlots[2]).getAmount()};

        int[] totalsArray = new int[3];
        int result = Integer.MAX_VALUE;

        for(int i = 0; i < recipeAmountValues.length; i++) {
            totalsArray[i] = (int) inputAmountValues[i] / recipeAmountValues[i];
            if(totalsArray[i] < result) {
                result = totalsArray[i];
            }
        }
        return clamp(result, 0, (int) Math.floor(64 / recipe.getResult().getAmount()));
    }

    public static int clamp(int val, int min, int max) {
        return Math.max(min, Math.min(max, val));
    }
}
