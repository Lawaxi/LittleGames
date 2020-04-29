package net.lawaxi.mc.bridgepractice.utils;

import net.lawaxi.mc.bridgepractice.Bridgepractice;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerUtils {

    public static HashMap<Player, Location> location = new HashMap<>();
    public static HashMap<Player, ArrayList<Location>> playersblock = new HashMap<>();

    public static void clearBlocks(ArrayList<Location> blocks){

        for(Location location:blocks){

            location.getBlock().breakNaturally();

            try {
                Thread.sleep(50);
            }
            catch (InterruptedException e){}
        }
    }

    public static void killing(Player killer,Player player){
        killer.sendTitle(
                Bridgepractice.config.getString("messages.kill.title")
                        .replace("%player%",player.getDisplayName()),
                Bridgepractice.config.getString("messages.kill.sub")
                    .replace("%player%",player.getDisplayName()),20,40,20);

        Bridgepractice.addExp(killer,Bridgepractice.config.getInt("map.reward.kill"));
    }

    public static void giveBlocks(Player player){
        player.getInventory().clear();
        for(int i=Bridgepractice.rank.getLevel(player);i>=0;i--)
        {
            if(Bridgepractice.config.contains("blockskin."+String.valueOf(i))){

                player.getInventory().addItem(getBlock(i));
                return;
            }
        }
    }

    private static ItemStack getBlock(int index){
        String block = Bridgepractice.config.getString("blockskin."+String.valueOf(index));
        String[] arg = block.split(":");
        if (arg.length == 1) {
            return new ItemStack(Material.valueOf(arg[0]), 64);
        } else {
            return new ItemStack(Material.valueOf(arg[0]), 64, Short.parseShort(arg[1]));
        }
    }
}
