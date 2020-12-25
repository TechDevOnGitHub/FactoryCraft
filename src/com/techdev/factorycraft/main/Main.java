package com.techdev.factorycraft.main;

import com.techdev.factorycraft.alloysmelter.listeners.AlloySmelterEvents;
import com.techdev.factorycraft.alloysmelter.setup.AlloySmelterSetup;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

    @Override
    public void onEnable()
    {
        PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new AlloySmelterEvents(), this);
        pluginManager.registerEvents(this, this);

        new AlloySmelterSetup().setup();
    }

    @EventHandler
    public void onBlockInteract(PlayerInteractEvent event)
    {
        boolean isRightClick = event.getAction() == Action.RIGHT_CLICK_BLOCK;

        if(isRightClick && event.getPlayer().getInventory().getItemInMainHand().getType() == Material.STICK && event.getHand().equals(EquipmentSlot.HAND))
        {
            BlockFace blockFace = event.getBlockFace();
            Location location = getBlockPlaceLocation(event.getClickedBlock().getLocation(), blockFace, event.getClickedBlock().getType());

            event.getPlayer().getWorld().getBlockAt(location).setType(Material.DIAMOND_BLOCK);
            event.getPlayer().getWorld().playSound(location, Sound.BLOCK_STONE_PLACE, 0.3f, 1.0f);

        }
    }

    public Location getBlockPlaceLocation(Location location, BlockFace blockFace, Material blockType)
    {
        if
        (
            blockType !=
                Material.GRASS &&
            blockType !=
                Material.TALL_GRASS &&
            blockType !=
                Material.SEAGRASS &&
            blockType !=
                Material.TALL_SEAGRASS
        )
        if(blockFace == BlockFace.UP) {
            location.setY(location.getY() + 1);
        } else if(blockFace == BlockFace.DOWN) {
            location.setY(location.getY() - 1);
        } else if(blockFace == BlockFace.NORTH) {
            location.setZ(location.getZ() - 1);
        } else if(blockFace == BlockFace.WEST) {
            location.setX(location.getX() - 1);
        } else if(blockFace == BlockFace.SOUTH) {
            location.setZ(location.getZ() + 1);
        } else if(blockFace == BlockFace.EAST) {
            location.setX(location.getX() + 1);
        } else {
            System.out.println("Something went wrong! :/");
        }

        return location;
    }
}
