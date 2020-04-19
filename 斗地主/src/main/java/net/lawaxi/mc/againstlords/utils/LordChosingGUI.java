package net.lawaxi.mc.againstlords.utils;

import net.lawaxi.mc.againstlords.AgainstLords;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class LordChosingGUI {

    public static Inventory LordChosing;

    public static int now;

    private static HashMap<Player,Integer> theirchose;

    public static ArrayList<ItemStack> chosingitems;

    public static boolean chosed;


    public static void createNewGUI(){
        LordChosing= Bukkit.createInventory(null, 9, AgainstLords.getMessages("lcguititle"));

        LordChosing.setItem(1,chosingitems.get(0));
        LordChosing.setItem(3,chosingitems.get(1));
        LordChosing.setItem(5,chosingitems.get(2));
        LordChosing.setItem(7,chosingitems.get(3));

        now=-1;
        chosed=false;
        theirchose = new HashMap<>();
        nextPlayer();
    }

    public static boolean choose(ItemStack stack) {
        for (int i = 0; i < chosingitems.size(); i++) {
            if (chosingitems.get(i).equals(stack)) {

                theirchose.put(Game.playeringame.get(now),i);

                //1.有人叫三分直接定地主

                if (i==1)
                {
                    LordChosing.getItem(3).setType(Material.BARRIER);
                }
                else if(i==2)
                {
                    LordChosing.getItem(5).setType(Material.BARRIER);
                }
                else if(i==3)
                {
                    Game.setLord(Game.playeringame.get(now));
                    return true;
                }

                nextPlayer();
                return true;
            }

        }
        return false;
    }

    private static void nextPlayer()
    {
        if(now==Game.playeringame.size()-1)
        {
            //2.没有人叫三分 判定地主
            int max = 0;
            Player maxplayer = null;
            for(Player player:Game.playeringame)
            {
                if(theirchose.get(player)>max)
                {
                    max=theirchose.get(player);
                    maxplayer=player;
                }
            }

            //3.如果没有人叫 重新开局
            if(max==0)
            {
                Bukkit.broadcastMessage(AgainstLords.getMessages("lordnotfound"));
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        Game.start();
                    }
                }.runTaskLater(AgainstLords.instance,20*5);
            }
            else
            {
                Game.setLord(maxplayer);
            }

        }
        else
        {
            now++;

            Player player = Game.playeringame.get(now);

            new BukkitRunnable(){

                @Override
                public void run() {
                    player.openInventory(LordChosingGUI.LordChosing);
                }
            }.runTask(AgainstLords.instance);

        }
    }



}
