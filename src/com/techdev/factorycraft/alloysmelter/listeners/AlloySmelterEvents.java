package com.techdev.factorycraft.alloysmelter.listeners;

import com.techdev.factorycraft.main.Main;
import com.techdev.factorycraft.alloysmelter.gui.AlloySmelterGui;
import com.techdev.factorycraft.util.StringHasher;
import com.techdev.factorycraft.alloysmelter.util.AlloySmelterRecipeValidator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.techdev.factorycraft.alloysmelter.gui.AlloySmelterGui.*;

public class AlloySmelterEvents implements Listener {

    Material alloySmelterBlockType = Material.POLISHED_BASALT;
    Map<UUID, int[]> lastInteractedBlockPos = new HashMap<>();

    @EventHandler
    @SuppressWarnings("unchecked")
    public void onBlockInteract(PlayerInteractEvent event)
    {
        boolean isRightClick = event.getAction().equals(Action.RIGHT_CLICK_BLOCK);
        if(isRightClick)
        {
            if(event.getClickedBlock().getType() == alloySmelterBlockType && event.getHand() == EquipmentSlot.HAND)
            {
                Location blockLocation = event.getClickedBlock().getLocation();
                int[] blockPosition = new int[]{blockLocation.getBlockX(), blockLocation.getBlockY(), blockLocation.getBlockZ()};

                lastInteractedBlockPos.put(event.getPlayer().getUniqueId(), blockPosition);

                File file = new File(JavaPlugin.getProvidingPlugin(Main.class).getDataFolder(), "alloysmelter_data.yml");

                if(file.exists())
                {
                    YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                    String blockId = getBlockId(event.getClickedBlock().getLocation());
                    List<ItemStack> nonStaticItems = (List<ItemStack>) config.getList("data." + blockId + ".gui");
                    List<ItemStack> defaultMenuContents = Arrays.asList(new AlloySmelterGui(event.getPlayer()).getInventory().getContents());

                    for (int i = 0; i < nonStaticItemSlots.size(); i++) {
                        defaultMenuContents.set(nonStaticItemSlots.get(i), nonStaticItems.get(i));
                    }

                    Inventory inventory = Bukkit.createInventory(event.getPlayer(), guiSlots, guiTitle);

                    for (int i = 0; i < defaultMenuContents.size(); i++) {
                        inventory.setItem(i, defaultMenuContents.get(i));
                    }

                    event.getPlayer().openInventory(inventory);
                }
            }
        }
    }

    @EventHandler
    public void onBlockPlaced(BlockPlaceEvent event)
    {
        if(event.getBlockAgainst().getType() == alloySmelterBlockType)
        {
            event.setCancelled(true);
            return;
        }

        if(event.getBlock().getType() == alloySmelterBlockType)
        {
            File file = new File(JavaPlugin.getProvidingPlugin(Main.class).getDataFolder(), "alloysmelter_data.yml");

            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if(file.exists())
            {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                List<ItemStack> defaultNonStaticItems = Arrays.asList(null, null, null, null, hookBlackWhite);
                String blockId = getBlockId(event.getBlock().getLocation());

                config.set("data." + blockId + ".gui", defaultNonStaticItems);

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @EventHandler
    @SuppressWarnings("unchecked")
    public void onBlockBreak(BlockBreakEvent event)
    {
        if(event.getBlock().getType() == alloySmelterBlockType)
        {
            File file = new File(JavaPlugin.getProvidingPlugin(Main.class).getDataFolder(), "alloysmelter_data.yml");

            if(file.exists())
            {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String blockId = getBlockId(event.getBlock().getLocation());
                List<ItemStack> nonStaticItems = (List<ItemStack>) config.getList("data." + blockId + ".gui");

                for (int i = 0; i < nonStaticItems.size(); i++) {
                    if(nonStaticItems.get(i) != null && i != 4)
                        event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), nonStaticItems.get(i));
                }

                config.set("data." + blockId, null);

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if(event.getView().getTitle().equals(guiTitle))
        {
            Player player = (Player) event.getWhoClicked();
            try {
                if(event.getCurrentItem().getItemMeta().getDisplayName().equals(guiItemName) || event.getSlot() == hookSlot)
                {
                    event.setCancelled(true);
                }
                if(event.getSlot() == hookSlot && event.getClickedInventory().getItem(hookSlot).getItemMeta().getCustomModelData() == hookGreenCustomModelData) {
                    craft(player);
                }
            } catch (NullPointerException ignored) {
                if(event.getCursor().getType() != Material.AIR && event.getSlot() == outputSlots[0])
                {
                    event.setCancelled(true);
                }
            }
            updateGui(player);
        }
    }

    @EventHandler
    public void onMenuDrag(InventoryDragEvent event)
    {
        if(event.getView().getTitle().equals(guiTitle))
        {
            try {
                if(event.getView().getTitle().equals(guiTitle))
                {
                    event.setCancelled(true);
                }
            } catch (NullPointerException ignored) {    }
        }
    }

    @EventHandler
    public void onMenuClose(InventoryCloseEvent event)
    {
        if(event.getView().getTitle().equals(guiTitle))
        {
            File file = new File(JavaPlugin.getProvidingPlugin(Main.class).getDataFolder(), "alloysmelter_data.yml");

            if(file.exists())
            {
                YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
                String blockId = StringHasher.hashString(Arrays.toString(lastInteractedBlockPos.get(event.getPlayer().getUniqueId())));
                List<ItemStack> menuContents = Arrays.asList(event.getInventory().getContents());
                List<ItemStack> nonStaticItems = new ArrayList<>();

                for (Integer nonStaticItemSlot : nonStaticItemSlots) {
                    nonStaticItems.add(menuContents.get(nonStaticItemSlot));
                }

                config.set("data." + blockId + ".gui", nonStaticItems);

                try {
                    config.save(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void craft(Player player)
    {
        AlloySmelterRecipeValidator validator = new AlloySmelterRecipeValidator();
        try {
            validator = new AlloySmelterRecipeValidator(player.getOpenInventory(), inputSlots, outputSlots);
        } catch (NullPointerException ignored) {     }

        InventoryView inventoryView = player.getOpenInventory();

        for (int i = 0; i < validator.recipeAmount; i++) {
            inventoryView.getTopInventory().addItem(validator.resultItem);

            for (int j = 0; j < inputSlots.length; j++) {
                ItemStack item = inventoryView.getItem(inputSlots[j]);
                item.setAmount(item.getAmount() - validator.minRecipeItemAmounts[j]);
                inventoryView.setItem(inputSlots[j], item);
            }
        }
    }

}
