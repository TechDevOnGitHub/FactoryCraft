package com.techdev.factorycraft.util;

import com.techdev.factorycraft.main.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;


public class Hologram
{
    private final ArmorStand entity;
    private String message;

    public Hologram(String message, Location location, World world)
    {
        double originalY = location.getY();
        location.setY(0.0);
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(location, EntityType.ARMOR_STAND);
        armorStand.setMarker(true);
        armorStand.setVisible(false);

        Bukkit.getServer().getScheduler().runTaskLater(Main.getProvidingPlugin(Main.class), new Runnable() {
            @Override
            public void run() {
                location.setY(originalY);
                armorStand.teleport(location);
                armorStand.setCustomName(message);
                armorStand.setCustomNameVisible(true);
            }
        }, 1L);

        this.entity = armorStand;
        this.message = message;
    }

    public void changeMessage(String message) {
        this.entity.setCustomName(message);
        this.message = message;
    }

    public void teleport(Location location) {
        this.entity.teleport(location);
    }

    public String getMessage() {
        return this.message;
    }

    public ArmorStand getEntity() {
        return this.entity;
    }

    public void delete() {
        this.entity.setHealth(0);
    }
}
