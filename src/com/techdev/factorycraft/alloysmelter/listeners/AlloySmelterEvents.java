package com.techdev.factorycraft.alloysmelter.listeners;

import com.techdev.factorycraft.main.Main;
import com.techdev.factorycraft.alloysmelter.menus.AlloySmelterGui;
import com.techdev.factorycraft.util.Hologram;
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

import static com.techdev.factorycraft.alloysmelter.menus.AlloySmelterGui.*;

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
                    List<ItemStack> defaultMenuContents = Arrays.asList(new AlloySmelterGui(event.getPlayer(), guiTitle).getInventory().getContents());

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

            /*Player player = event.getPlayer();
            Location armorStandLocation = event.getBlock().getLocation();
            armorStandLocation.setY(armorStandLocation.getY() + 1.0);
            armorStandLocation.setX(armorStandLocation.getX() + 0.5);
            armorStandLocation.setZ(armorStandLocation.getZ() + 0.5);

            new Hologram("Some example text", armorStandLocation, player.getWorld());*/
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

    public ItemStack getGuiItem(Material material, int customModelData)
    {
        ItemStack item = new ItemStack(material);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setCustomModelData(customModelData);
        itemMeta.setDisplayName(guiItemName);
        item.setItemMeta(itemMeta);
        return item;
    }

    public void updateGui(Player player)
    {
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(Main.class), new Runnable() {

            @Override
            public void run()
            {
                AlloySmelterRecipeValidator validator = new AlloySmelterRecipeValidator();
                try {
                    validator = new AlloySmelterRecipeValidator(player.getOpenInventory(), inputSlots, outputSlots);
                } catch (NullPointerException ignored) {    }

                InventoryView inventoryView = player.getOpenInventory();
                inventoryView.setItem(hookSlot, getHookSlotItem(inventoryView, validator));
            }
        }, 0L);
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

    public ItemStack getHookSlotItem(InventoryView inventoryView, AlloySmelterRecipeValidator validator) {
        ItemStack hook;
        if (validator.isValidRecipe && outputHasEnoughRoom(inventoryView, validator)) {
            hook = hookGreen;
            ItemMeta hookMeta = hook.getItemMeta();
            hookMeta.setDisplayName(ChatColor.GREEN + "Craft");
            ArrayList<String> hookLore = new ArrayList<>();
            hookLore.add(ChatColor.DARK_GRAY + ">> " + ChatColor.GRAY + "Recipe name: " + validator.recipeName);
            hookLore.add(ChatColor.DARK_GRAY + ">> " + ChatColor.GRAY + "Amount: " + ChatColor.GOLD + validator.recipeAmount + "x");
            hookLore.add(ChatColor.DARK_GRAY + ">> " + ChatColor.GRAY + "Duration: " + ChatColor.YELLOW + validator.recipeCraftingCuration + "s");
            hookMeta.setLore(hookLore);
            hook.setItemMeta(hookMeta);
        } else {
            hook = hookBlackWhite;
        }
        return hook;
    }

    public boolean outputHasEnoughRoom(InventoryView inventoryView, AlloySmelterRecipeValidator validator)
    {
        int x = 0;
        try {
            x = inventoryView.getItem(outputSlots[0]).getAmount() + validator.resultItemAmount;
        } catch (NullPointerException e) {      }
        return x <= 64;
    }

    public String getBlockId(Location location)
    {
        int[] blockPosition = new int[]{location.getBlockX(), location.getBlockY(), location.getBlockZ()};
        String blockId = StringHasher.hashString(Arrays.toString(blockPosition));
        return blockId;
    }

}
