package com.techdev.factorycraft.alloysmelter.menus;

import com.techdev.factorycraft.util.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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
    public static ItemStack hookGreen = ItemUtils.getGuiItem(Material.BARRIER, hookGreenCustomModelData, guiItemName);
    public static ItemStack hookBlackWhite = ItemUtils.getGuiItem(Material.BARRIER, 10, guiItemName);
    public static ItemStack craftingHammerItem = ItemUtils.getGuiItem(Material.BARRIER, 13, guiItemName);

    public static Inventory inventory;

    public static ItemStack backgroundItem = ItemUtils.getGuiItem(Material.BARRIER, 7, guiItemName);

    private Player player;

    public AlloySmelterGui(Player player, String title)
    {
        inventory = Bukkit.createInventory(player, guiSlots, guiTitle);
        this.player = player;
        fillBackground();
        setMenuItems();
    }

    public ItemStack getBackgroundItem() {
        return ItemUtils.getGuiItem(Material.BARRIER, 7, guiItemName);
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
        ItemStack coalItem = ItemUtils.getGuiItem(Material.BARRIER, 6, guiItemName);
        ItemStack ironItem = ItemUtils.getGuiItem(Material.BARRIER, 5, guiItemName);
        ItemStack pipeInput = ItemUtils.getGuiItem(Material.BARRIER, 1, guiItemName);
        ItemStack pipeStraight = ItemUtils.getGuiItem(Material.BARRIER,8, guiItemName);
        ItemStack pipeTopLeft = ItemUtils.getGuiItem(Material.BARRIER, 3, guiItemName);
        ItemStack pipeTopRight = ItemUtils.getGuiItem(Material.BARRIER, 4, guiItemName);
        ItemStack pipeOutput = ItemUtils.getGuiItem(Material.BARRIER, 2, guiItemName);
        ItemStack pipeCombine = ItemUtils.getGuiItem(Material.BARRIER, 11, guiItemName);

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

}
