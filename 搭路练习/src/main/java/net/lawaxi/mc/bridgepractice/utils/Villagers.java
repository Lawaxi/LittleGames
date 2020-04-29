package net.lawaxi.mc.bridgepractice.utils;

import net.lawaxi.mc.bridgepractice.Bridgepractice;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Villager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

public class Villagers {

    public static ArrayList<Location> spawnpoint = new ArrayList<>();

    public static void spawnVillage(Location location)
    {
        //重置x或y坐标上相同的村民

        //清空原有村民
        for (Entity entity : location.getWorld().getEntities())
        {
            if(entity.getType().equals(EntityType.VILLAGER) && entity.getLocation().getBlockY()==location.getBlockY()) {
                if (entity.getLocation().getBlockX() == location.getBlockX())
                    entity.remove();
                else if (entity.getLocation().getBlockZ() == location.getBlockZ())
                    entity.remove();
            }
        }

        for(Location location1:spawnpoint){
            if((location1.getBlockX()==location.getBlockX() || location1.getBlockZ()==location.getBlockZ())&&location1.getBlockY()==location.getBlockY())
            {
                Villager a=(Villager) location1.getWorld().spawnEntity(location1,EntityType.VILLAGER);
                a.setCustomName(Bridgepractice.config.getString("messages.villager"));
                a.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 32767, 255, false, false), true);
            }
        }
    }

    public static void spawnAllVillage(){
        for(Location location1:spawnpoint){
            Villager a=(Villager) location1.getWorld().spawnEntity(location1,EntityType.VILLAGER);
            a.setCustomName(Bridgepractice.config.getString("messages.villager"));
            a.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 32767, 255, false, false), true);
        }
    }
}
