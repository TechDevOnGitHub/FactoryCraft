package com.techdev.factorycraft.util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils
{
    public static ItemStack getGuiItem(Material material, int customModelData, String displayName)
    {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemMeta.setDisplayName(displayName);
        item.setItemMeta(itemMeta);
        return item;
    }

    public static ItemStack modifyAmount(ItemStack item, int modifier) {
        int newAmount = item.getAmount() + modifier;
        item.setAmount(newAmount);
        return item;
    }
}
