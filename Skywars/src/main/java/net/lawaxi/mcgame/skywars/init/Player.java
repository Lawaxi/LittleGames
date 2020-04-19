package net.lawaxi.mcgame.skywars.init;

import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.config.Config;
import net.lawaxi.mcgame.skywars.config.Messages;
import net.lawaxi.mcgame.skywars.init.tasks.GameWaitTask;
import net.lawaxi.mcgame.skywars.utils.GameState;
import net.lawaxi.mcgame.skywars.utils.Scoreboard;
import org.bukkit.GameMode;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Player {

    public static ArrayList<Location> spawnlocation = new ArrayList<>();
    public static Map<Integer,org.bukkit.entity.Player> spawnlocationplayers;

    public static void inlobby(org.bukkit.entity.Player player){

        player.getInventory().clear();
        player.getEnderChest().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SURVIVAL);

        //双人模式或者更多人的话可能出现 出生点数>玩家数 需要更新
        if(allused())
        {
            for(int i=0;i<spawnlocation.size();i++)
            {
                spawnlocationplayers = new HashMap<>();
            }
        }

        int index = getNow();
        player.teleport(spawnlocation.get(index));
        spawnlocationplayers.put(index,player);
    }

    private static boolean allused(){
        for(int i=0;i<spawnlocation.size();i++)
        {
            if(!spawnlocationplayers.containsKey(i))
                return false;
        }
        return true;
    }

    private static int getNow(){
        for(int i=0;i<spawnlocation.size();i++)
        {
            if(!spawnlocationplayers.containsKey(i))
                return i;
        }
        return 0;
    }


    public static void loadPlayer(org.bukkit.entity.Player player){

        Scoreboard.newIngameSB(player);

        //玩家状态
        if(Scoreboard.player > (int) Config.read("map.maxplayer"))
        {
            death(player);

            if(!Skywars.state.equals(GameState.InGame))
                Scoreboard.player--;

            player.sendTitle(Messages.read("ingame.join.full.title").toString(),Messages.read("ingame.join.full.sub").toString(),20,40,20);
        }
        else {
            if (Skywars.state.equals(GameState.InGame)) {
                player.sendTitle(Messages.read("ingame.join.already.title").toString(), Messages.read("ingame.join.already.sub").toString(), 20, 40, 20);
                Player.death(player);

            } else if(Skywars.state.equals(GameState.Lobby)){
                inlobby(player);
                if (Scoreboard.player >= (int) Config.read("map.minplayer")) {
                    Skywars.state = GameState.Prepare;
                    Game.gamewaittask = new GameWaitTask((int) Config.read("lobby.waittime")).runTaskTimer(Skywars.instance, 20, (int) Config.read("lobby.waittime"));
                }
            }
        }
    }

    public static Map<Player,ArrayList<Player>> killer;

    public static void ingame(org.bukkit.entity.Player player){

        player.getInventory().clear();
        player.getEnderChest().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
    }

    public static void death(org.bukkit.entity.Player player){

        //旁观
        player.getInventory().clear();
        player.getEnderChest().clear();
        player.setHealth(20);
        player.setFoodLevel(20);
        player.setGameMode(GameMode.SPECTATOR);

    }

}
