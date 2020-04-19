package net.lawaxi.mc.againstlords.utils;

import net.lawaxi.mc.againstlords.AgainstLords;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;

public class GameGUI {

    public static Inventory GAME;

    public static int now;

    public static ItemStack openBlock;


    public static void createNewGAME(){

        //debug：控制台输出玩家手牌
        for(Player player:Game.playeringame)
        {
            String out = player.getDisplayName()+":";
            for(int i=0;i<Card.size();i++)
            {
                for(int j=0;j<Game.onhand.get(player).get(i);j++)
                {
                    out+=Card.indexToCard(i);
                }
            }
            System.out.println(out);
        }


        //GUI创建
        GAME= Bukkit.createInventory(null, 27, AgainstLords.getMessages("guititle"));

        //地主先出牌，多加一些没用的值放置越界
        now = Game.lordindex+Game.playeringame.size()-1;
        nextPlayer();
    }

    private static String launchinfo;
    public static boolean launch(Player player){
        if(!player.equals(Game.playeringame.get(now%Game.playeringame.size())))
        {
            player.sendTitle(AgainstLords.getMessages("notdutytitle"),AgainstLords.getMessages("notdutysub"),20,40,20);
            return true;
        }
        else
        {
            launchinfo = "";
            HashMap<Integer,Integer> launch = new HashMap<>();
            for(ItemStack stack : GAME.getContents())
            {
                if(stack!=null)
                {
                    int index = Card.getIndex(stack.clone());
                    if(index!=-1 && Game.onhand.get(player).get(index)-stack.getAmount()>=0)
                    {
                        if(launch.containsKey(index))
                            launch.replace(index,launch.get(index).intValue()+stack.getAmount());
                        else
                            launch.put(index,stack.getAmount());
                    }
                    else if(stack!=null)
                    {
                        //防止弱智熊孩子把其他东西扔进去
                        player.getInventory().addItem(stack);
                    }
                }
            }
            GAME.clear();

            for(int i=0;i<Card.size();i++)
            {
                if(launch.containsKey(i))
                {
                    Game.onhand.get(player).replace(i,Game.onhand.get(player).get(i)-launch.get(i));

                    for(int j=0;j<launch.get(i);j++)
                    {
                        launchinfo=launchinfo+Card.indexToCard(i)+" ";
                    }
                }
            }

            Bukkit.broadcastMessage(AgainstLords.getMessages("launch").replace("%player%",Game.getPlayerShowName(player)).replace("%cards%",launchinfo));
            for(Player player1:Bukkit.getOnlinePlayers())
            {
                player1.sendTitle(AgainstLords.getMessages("launchtitle").replace("%player%",Game.getPlayerShowName(player)).replace("%cards%",launchinfo),
                        AgainstLords.getMessages("launchsub").replace("%player%",Game.getPlayerShowName(player)).replace("%cards%",launchinfo),
                        20,40,20);
            }

            if(!Game.test())
                nextPlayer();

            return true;
        }
    }

    private static void nextPlayer()
    {
        now++;
        Player player = Game.playeringame.get(now%Game.playeringame.size());

        new BukkitRunnable(){

            @Override
            public void run() {
                player.openInventory(GameGUI.GAME);
            }
        }.runTask(AgainstLords.instance);

    }

}
