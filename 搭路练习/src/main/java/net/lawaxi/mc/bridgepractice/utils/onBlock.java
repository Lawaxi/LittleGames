package net.lawaxi.mc.bridgepractice.utils;

import net.lawaxi.mc.bridgepractice.Bridgepractice;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class onBlock {

    public static void start(Player player, Location location) {
        location.setX(utils.getDouble(location.getX()));
        location.setY(utils.getDouble(location.getY() + 1));
        location.setZ(utils.getDouble(location.getZ()));
        if (!PlayerUtils.location.get(player).equals(location)) {
            PlayerUtils.location.replace(player, location);
            PlayerUtils.giveBlocks(player);

            for(int i=1;i<=10;i++)
            {
                Block block = player.getWorld().getBlockAt(location.getBlockX(),location.getBlockY()-i,location.getBlockZ());
                if(block.getType().equals(Material.CHEST))
                {
                    for(ItemStack stack:((Chest)block.getState()).getBlockInventory().getContents())
                    {
                        if(stack!=null)
                            player.getInventory().addItem(stack);
                    }
                }
            }

            player.sendTitle(
                    Bridgepractice.config.getString("messages.start.title")
                            .replace("%world%", location.getWorld().getName())
                            .replace("%x%", String.valueOf(location.getX()))
                            .replace("%y%", String.valueOf(location.getY()))
                            .replace("%z%", String.valueOf(location.getZ())),
                    Bridgepractice.config.getString("messages.start.sub")
                            .replace("%world%", location.getWorld().getName())
                            .replace("%x%", String.valueOf(location.getX()))
                            .replace("%y%", String.valueOf(location.getY()))
                            .replace("%z%", String.valueOf(location.getZ())), 20, 40, 20);
        }
    }



    public static void end(Player player, Location location){

        new BukkitRunnable(){
            @Override
            public void run() {
                player.teleport(PlayerUtils.location.get(player));
                PlayerUtils.clearBlocks((ArrayList<Location>)PlayerUtils.playersblock.get(player).clone());
                this.cancel();
            }
        }.runTask(Bridgepractice.instance);


        player.sendTitle(Bridgepractice.config.getString("messages.finish.title"),
                Bridgepractice.config.getString("messages.finish.sub"),
                20,40,20);
        Bridgepractice.addExp(player,Bridgepractice.config.getInt("map.reward.finish"));
    }

    public static void return1(Player player, Location location){
        player.teleport(Bridgepractice.joinLocation);
    }

    public static void teleport(Player player, Location location){


        Location location1 = location.clone();

        if(player.isSneaking()) {

            //先向上查找再向下查找

            for (int i = 1; i < 20; i++) {
                location1.setY(location.getY() + i);
                if (location1.getBlock().getType().equals(Bridgepractice.config.get("block.teleport"))) {
                    location1.setY(location1.getY() + 1);
                    player.teleport(location1);
                    return;
                }
            }

            for (int i = 1; i < 20; i++) {
                location1.setY(location.getY() - i);
                if (location1.getBlock().getType().equals(Bridgepractice.config.get("block.teleport"))) {
                    location1.setY(location1.getY() + 1);
                    player.teleport(location1);
                    return;
                }
            }
        }


    }
}
