package net.lawaxi.mcgame.skywars.init.tasks;

import com.sun.javafx.scene.control.behavior.SliderBehavior;
import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.config.Config;
import net.lawaxi.mcgame.skywars.init.Game;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Random;

public class ChestPrepareTask extends BukkitRunnable {


    @Override
    public void run() {

        Block block;
        Random number = new Random();

        //int plus = 0;
        //System.out.print("配置中共有 "+Game.chestitems.size()+" 个箱子预设");

        for(int x = Config.readMapLimit("x1"); x<=Config.readMapLimit("x2"); x++) {
            for (int y = Config.readMapLimit("y1"); y <= Config.readMapLimit("y2"); y++) {
                for (int z = Config.readMapLimit("z1"); z <= Config.readMapLimit("z2"); z++) {
                    block = Skywars.world.getBlockAt(x,y,z);
                    if(block.getType().equals(Material.CHEST) && Skywars.chests.size()>0)
                    {

                        ((Chest)block.getState()).getBlockInventory().setContents(
                                Skywars.chests.get(number.nextInt(Skywars.chests.size())).toArray(new ItemStack[0]));

                        /*
                            同样是更改方案后保留

                        now = new ArrayList<>();

                        //取最小物品数和最大物品数间的随机数 amount
                        amount = (int)Config.read("ingame.itemmin")+number.nextInt()%((int)Config.read("ingame.itemmax")-(int)Config.read("ingame.itemmin")+1);
                        for(int i=0;i<amount;i++)
                        {
                            //出现过的物品不会再出现
                            item = Math.abs(number.nextInt())%(chestitems.size());
                            while(now.contains(item))
                            {
                                item = Math.abs(number.nextInt())%(chestitems.size());
                            }
                            now.add(item);

                            chest.addItem(chestitems.get(item));
                        }*/

                    }
                    else if(block.getType().equals(Material.GLASS))
                    {
                        block.setType(Material.AIR);
                    }
                }
            }
        }

        //Skywars.logger.info("地图中共有 "+plus+" 个箱子");
    }
}
