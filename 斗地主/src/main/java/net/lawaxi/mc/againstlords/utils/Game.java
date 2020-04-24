package net.lawaxi.mc.againstlords.utils;

import net.lawaxi.mc.againstlords.AgainstLords;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import java.util.ArrayList;
import net.lawaxi.rank.utils;
import java.util.HashMap;
import java.util.Random;

public class Game {

    //                                    牌的index 数量
    public static HashMap<Player, HashMap<Integer,Integer>> onhand;
    public static ArrayList<Player> playeringame;
    public static int lordindex;


    public static boolean test(){

        for(Player player: playeringame)
        {
            if(testdone(player))
            {
                if(player.equals(playeringame.get(lordindex)))
                    end(AgainstLords.getMessages("win2title"),AgainstLords.getMessages("win2sub"));
                else
                    end(AgainstLords.getMessages("win1title"),AgainstLords.getMessages("win1sub"));
                return true;
            }
        }
        return false;
    }

    private static boolean testdone(Player player)
    {
        for(int i=0;i<Card.size();i++)
        {
            if(onhand.get(player).get(i)!=0) {
                return false;
            }
        }
        winner = player;
        return true;
    }


    private static HashMap<Integer,Integer> leftcards;
    public static boolean start(boolean force){
        if(Bukkit.getOnlinePlayers().size()>=3 || force)
        {

            AgainstLords.isInGame=true;

            onhand=new HashMap<>();
            playeringame=new ArrayList<>();
            leftcards = (HashMap<Integer, Integer>) Card.allCards.clone();
            Random random = new Random();

            //计算每人应分得的牌数，按地主牌>=1计算
            int amount = (54-1)/Bukkit.getOnlinePlayers().size();

            //随机取玩家顺序
            Player[] players = Bukkit.getOnlinePlayers().toArray(new Player[0]);
            for(int i=0;i<Bukkit.getOnlinePlayers().size();i++)
            {
                int index = random.nextInt(Bukkit.getOnlinePlayers().size());
                while(playeringame.contains(players[index]))
                {
                    index = random.nextInt(Bukkit.getOnlinePlayers().size());
                }

                playeringame.add(players[index]);
            }

            //初始化玩家背包
            onhandDefault = new HashMap<>();
            for(int i=0;i<Card.size();i++)
            {
                onhandDefault.put(i,0);
            }

            String playerlist = "";
            for(Player player:playeringame) {

                player.getInventory().clear();

                playerlist+=player.getDisplayName()+" ";
                initPlayerCards(random,player,amount);

                player.getInventory().addItem(GameGUI.openBlock);
            }

            ArrayList<String> startinfo = AgainstLords.getMessageList("startinfo");
            for(int i=0;i<startinfo.size();i++)
            {
                Bukkit.broadcastMessage(startinfo.get(i).replace("%players%",playerlist));
            }

            LordChosingGUI.createNewGUI();
            return true;
        }
        else
        {
            return false;
        }
    }

    private static HashMap<Integer,Integer> onhandDefault;
    private static void initPlayerCards(Random random,Player player,int amount){

        onhand.put(player,(HashMap<Integer,Integer>)onhandDefault.clone());

        for(int i =0;i<amount;i++)
        {
            //随机抽取剩余牌中的一张
            int index= random.nextInt(Card.size());
            while(leftcards.get(index)==0) {
                index = random.nextInt(Card.size());
            }

            //在剩余卡牌中删去
            leftcards.replace(index,leftcards.get(index).intValue()-1);

            //在玩家牌中放入
            onhand.get(player).replace(index,onhand.get(player).get(index).intValue()+1);
        }

        for(int i=0;i<Card.size();i++)
        {
            if(onhand.get(player).get(i)>0)
            {
                ItemStack stack = Card.cards.get(i);
                stack.setAmount(onhand.get(player).get(i));
                player.getInventory().addItem(stack);
            }
        }
    }

    private static String lordcardinfo;
    public static void setLord(Player player){

        lordcardinfo="";

        for(int i=0;i<Card.size();i++)
        {
            if(leftcards.get(i)>0){
                //生成物品
                ItemStack stack = Card.cards.get(i);
                stack.setAmount(leftcards.get(i));

                //在玩家物品和手牌列表中加入
                player.getInventory().addItem(stack);
                onhand.get(player).replace(i,onhand.get(player).get(i).intValue()+leftcards.get(i).intValue());

                //提示文本中加入
                for(int j=0;j<leftcards.get(i);j++)
                {
                    lordcardinfo=lordcardinfo+Card.indexToCard(i)+" ";
                }
            }
        }

        //公开地主牌
        Bukkit.broadcastMessage(AgainstLords.getMessages("lordinfo").replace("%player%",player.getDisplayName()).replace("%cards%",lordcardinfo));

        String title = AgainstLords.getMessages("lordinfotitle").replace("%player%",player.getDisplayName()).replace("%cards%",lordcardinfo);
        String sub = AgainstLords.getMessages("lordinfosub").replace("%player%",player.getDisplayName()).replace("%cards%",lordcardinfo);
        for(Player player1: Bukkit.getOnlinePlayers()){
            player1.sendTitle(title,sub ,20,40,20);
        }

        lordindex = playeringame.indexOf(player);
        LordChosingGUI.chosed=true;
        GameGUI.createNewGAME();
    }

    private static Player winner;
    public static void end(String title,String sub){
        AgainstLords.isInGame=false;

        //leftpre
        ArrayList<String> leftpre = AgainstLords.getMessageList("leftpre");
        for(int i=0;i<leftpre.size();i++)
        {
            Bukkit.broadcastMessage(leftpre.get(i));
        }

        for(Player player1: playeringame)
        {
            player1.sendTitle(title,sub,20,40,20);

            if(player1==winner && playeringame.contains(player1))
            {
                HashMap<Integer,Integer> him = onhand.get(player1);
                String forhim = " ";
                for(int i=0;i<Card.size();i++)
                {
                    for(int j=0;j<him.get(i);j++)
                    {
                        forhim+=Card.indexToCard(i)+" ";
                    }
                }

                //left
                ArrayList<String> left = AgainstLords.getMessageList("left");
                for(int i=0;i<left.size();i++)
                {
                    Bukkit.broadcastMessage(left.get(i)
                            .replace("%player%",getPlayerShowName(player1))
                            .replace("%cards%",forhim));
                }

                player1.getInventory().clear();
            }
        }

        //leftsuf
        ArrayList<String> leftsuf = AgainstLords.getMessageList("leftsuf");
        for(int i=0;i<leftsuf.size();i++)
        {
            Bukkit.broadcastMessage(leftsuf.get(i));
        }


        //Rank游戏奖励
        if(AgainstLords.useRank)
        {
            for(Player player : Bukkit.getOnlinePlayers())
            {
                int exp;
                if(player==winner)
                    exp = AgainstLords.config.getInt("levelreward.lose");
                else
                    exp =AgainstLords.config.getInt("levelreward.win");

                if (exp != 0)
                    utils.addExp(player, exp);
            }
        }
    }


    public static String getPlayerShowName(Player player)
    {
        if(playeringame.indexOf(player) == lordindex)
        {
            return AgainstLords.getMessages("playerprefix2").replace("%player%",player.getDisplayName());
        }
        else
        {
            return AgainstLords.getMessages("playerprefix1").replace("%player%",player.getDisplayName());
        }

    }
}
