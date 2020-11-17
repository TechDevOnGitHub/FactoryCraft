package com.techdev.factorycraft.menus;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class AlloySmelterGui implements Listener {

    public static int guiSlots = 5 * 9;
    public static String guiTitle = "            Alloy Smelter";
    public static String guiItemName = ChatColor.BLACK + "";

    public static int[] outputSlots = {39};
    public static int hookSlot = 41;

    public static List<Integer> nonStaticItemSlots = new ArrayList<>();

    public static int[] inputSlots = {11, 13, 15};

    public static int hookGreenCustomModelData = 9;
    public static ItemStack hookGreen = getGuiItem(Material.BARRIER, hookGreenCustomModelData);
    public static ItemStack hookBlackWhite = getGuiItem(Material.BARRIER, 10);
    public static ItemStack craftingHammerItem = getGuiItem(Material.BARRIER, 13);

    public static Inventory inventory;

    public static ItemStack backgroundItem = getGuiItem(Material.BARRIER, 7);

    private Player player;

    public AlloySmelterGui(Player player)
    {
        inventory = Bukkit.createInventory(player, guiSlots, guiTitle);
        this.player = player;
        fillBackground();
        setMenuItems();
    }

    public ItemStack getBackgroundItem() {
        return getGuiItem(Material.BARRIER, 7);
    }

    public int[] getInputSlots() {
        return new int[] {11, 13, 15};
    }

    public int[] getOutputSlots() {
        return outputSlots;
    }

    public String getMenuName() {
        return guiTitle;
    }

    public int getSlots() {
        return guiSlots;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void openInventory() {
        player.openInventory(inventory);
    }

    public void setItem(int slot, ItemStack item)
    {
        inventory.setItem(slot, item);
    }

    public void setMenuItems()
    {
        ItemStack coalItem = getGuiItem(Material.BARRIER, 6);
        ItemStack ironItem = getGuiItem(Material.BARRIER, 5);
        ItemStack pipeInput = getGuiItem(Material.BARRIER, 1);
        ItemStack pipeStraight = getGuiItem(Material.BARRIER,8);
        ItemStack pipeTopLeft = getGuiItem(Material.BARRIER, 3);
        ItemStack pipeTopRight = getGuiItem(Material.BARRIER, 4);
        ItemStack pipeOutput = getGuiItem(Material.BARRIER, 2);
        ItemStack pipeCombine = getGuiItem(Material.BARRIER, 11);

        inventory.setItem(2, coalItem);
        inventory.setItem(4, ironItem);
        inventory.setItem(6, ironItem);
        inventory.setItem(20, pipeInput);
        inventory.setItem(22, pipeInput);
        inventory.setItem(24, pipeInput);
        inventory.setItem(29, pipeTopLeft);
        inventory.setItem(30, pipeOutput);
        inventory.setItem(31, pipeCombine);
        inventory.setItem(32, pipeStraight);
        inventory.setItem(33, pipeTopRight);
        inventory.setItem(hookSlot, hookBlackWhite);
    }

    public void fillBackground()
    {
        for (int i = 0; i < guiSlots; i++) {
            boolean isBackgroundSlot = true;
            for (int j = 0; j < outputSlots.length; j++) {
                if(i == outputSlots[j]) isBackgroundSlot = false;
            }
            for (int j = 0; j < inputSlots.length; j++) {
                if(i == inputSlots[j]) isBackgroundSlot = false;
            }
            if(i == hookSlot) isBackgroundSlot = false;
            if(isBackgroundSlot) inventory.setItem(i, backgroundItem);
        }
    }

    public static ItemStack getGuiItem(Material material, int customModelData)
    {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemMeta.setDisplayName(guiItemName);
        item.setItemMeta(itemMeta);
        return item;
    }

}
