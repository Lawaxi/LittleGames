package net.lawaxi.mc.minesweeper.Game.MS;

import net.lawaxi.mc.minesweeper.Game.Sounds;
import net.lawaxi.mc.minesweeper.InventoryGames;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Minesweeper implements Listener {

    public static ItemStack empty;
    public static ArrayList<ItemStack> banner = new ArrayList<>();
    public static ItemStack mine;

    public static HashMap<String, msInventory> saolei = new HashMap<>();

    public static void init(){

        //配置初始化
        msInventory.title=InventoryGames.config.getString("Minesweeper.title");
        msInventory.size =(InventoryGames.config.getInt("Minesweeper.inventory.size")/9)*9;
        msInventory.minesize = InventoryGames.config.getInt("Minesweeper.inventory.mine.average")%msInventory.size;
        msInventory.minedelta = InventoryGames.config.getInt("Minesweeper.inventory.mine.delta");

        //物品显示初始化
        empty= InventoryGames.getItemStack("Minesweeper.display.empty");

        if(!InventoryGames.config.contains("Minesweeper.display.banner.0"))
        {
            InventoryGames.logger.warning("您需要至少设置一个 Minesweeper.display.banner");
            Bukkit.getPluginManager().disablePlugin(InventoryGames.instance);
        }
        else
        {
            ItemStack bannernow = InventoryGames.getItemStack("Minesweeper.display.banner.0");
            banner.add(bannernow.clone());

            for(int i=1;i<=8;i++){
                if(InventoryGames.config.contains("Minesweeper.display.banner."+i))
                    bannernow=InventoryGames.getItemStack("Minesweeper.display.banner."+i);

                banner.add(bannernow.clone());
            }
        }


        mine=InventoryGames.getItemStack("Minesweeper.display.mine");
    }

    public static void start(Player player){

        Sounds.StartSound(player);

        if(!saolei.containsKey(player.getName()))
            saolei.put(player.getName(), new msInventory(player.getName()));

        player.openInventory(saolei.get(player.getName()).inv);
    }

    public static void end(Player player, boolean win){

        Date a = new Date();

        if(win)
        {
            Sounds.WinSound(player);

            player.sendMessage(InventoryGames.config.getString("Minesweeper.messages.win")
                    .replace("%time%",getTime(saolei.get(player.getName()).time,a,true)));

            Bukkit.broadcastMessage(InventoryGames.config.getString("Minesweeper.messages.broad")
            .replace("%player%",player.getDisplayName())
            .replace("%time%",getTime(saolei.get(player.getName()).time,a,true)));
        }
        else{

            Sounds.FailedSound(player);

            player.sendMessage(InventoryGames.config.getString("Minesweeper.messages.failed")
                    .replace("%time%",getTime(saolei.get(player.getName()).time,a,true)));

        }

        saolei.remove(player.getName());

    }


    private static String getTime(Date start,Date end,boolean record){

        long time =  end.getTime()-start.getTime();
        if(record)
        {
            if(InventoryGames.config.getLong("Minesweeper.record")>time){
                Bukkit.broadcastMessage(InventoryGames.config.getString("Minesweeper.breakrecord"));
                InventoryGames.config.set("Minesweeper.record",time);
                InventoryGames.saveCFG();
            }
        }


        String out = "";
        if(time%(1000*60*60)>0){
            out+=time%(1000*60*60)+"小时";
            out+=time%(1000*60)+"分钟";
            out+=time%(60)+"秒";
        }
        else
        {
            if(time%(1000*60)>0)
            {
                out+=time%(1000*60)+"分钟";
                out+=time%(60)+"秒";
            }
            else
            {
                out+=time+"秒";
            }
        }
        return out;
    }

    @EventHandler(ignoreCancelled = true)
    private static void Choose(InventoryClickEvent e){


        if(saolei.containsKey(e.getWhoClicked().getName())){
            if(e.getInventory().equals(saolei.get(e.getWhoClicked().getName()).inv)){

                e.setCancelled(true);


                if(e.isLeftClick()){
                    saolei.get(e.getWhoClicked().getName()).leftSlot(e.getSlot());
                }
                else if(e.isRightClick())
                {
                    saolei.get(e.getWhoClicked().getName()).rightSlot(e.getSlot());
                }

            }

        }
        /*
        else if(e.getInventory().getTitle().equals(msInventory.title))
        {
            e.setCancelled(true);
        }*/
    }
}
