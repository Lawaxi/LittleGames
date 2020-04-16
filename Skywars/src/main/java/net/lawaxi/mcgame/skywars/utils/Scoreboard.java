package net.lawaxi.mcgame.skywars.utils;

import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.config.Config;
import net.lawaxi.mcgame.skywars.config.Messages;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import java.util.HashMap;
import java.util.Map;

public class Scoreboard {

    public static Objective lobby;
    public static Map<Player,Objective> ingame = new HashMap<>();

    public static void startScoreboard(){

        lobby =Bukkit.getScoreboardManager().getNewScoreboard().registerNewObjective("lobby","dummy", Messages.read("scoreboard.title").toString());
        lobby.setDisplaySlot(DisplaySlot.SIDEBAR);

        lobby.getScore("§1").setScore(9);
        player1=0;
        player=0;
        waittime1=100;
        waittime=100;
        lobby.getScore(Messages.read("scoreboard.lobby.player").toString().replace("%now%","0").replace("%max%",String.valueOf(Config.read("map.maxplayer")))).setScore(8);
        lobby.getScore("§2").setScore(7);
        lobby.getScore(Messages.read("scoreboard.lobby.time1").toString()).setScore(6);
        lobby.getScore("§3").setScore(5);
        lobby.getScore(Messages.read("scoreboard.lobby.map").toString().replace("%map%",Config.read("map.name").toString())).setScore(4);
        lobby.getScore(Messages.read("scoreboard.lobby.mode").toString().replace("%mode%",Config.read("map.mode").toString())).setScore(3);
        lobby.getScore("§4").setScore(2);
        lobby.getScore(Messages.read("scoreboard.lobby.info").toString()).setScore(1);

        survivalplayers1=0;
        survivalplayers=0;
    }



    public static void reloadScoreboard(){
        switch (Skywars.state)
        {
            case InGame:
                reloadinGameScoreboard();

            default:
                reloadLobbyScoreboard();
        }
    }


    private static int player1;
    public static int player;

    //结尾不带1的为实际内容；带1的为计分板目前显示内容，会在调用reload时更新为不带1的
    private static int waittime1;
    public static int waittime;

    private static void reloadLobbyScoreboard()
    {
        //检测玩家数是否变化
        if(player1!=player)
        {
            lobby.getScoreboard().resetScores(Messages.read("scoreboard.lobby.player").toString().replace("%now%",String.valueOf(player1)).replace("%max%",String.valueOf(Config.read("map.maxplayer"))));
            player1=player;
            lobby.getScore(Messages.read("scoreboard.lobby.player").toString().replace("%now%",String.valueOf(player1)).replace("%max%",String.valueOf(Config.read("map.maxplayer")))).setScore(8);
        }

        //检测等待时间是否变化
        if(waittime1!=waittime)
        {
            if(waittime1==100)
            {
                lobby.getScoreboard().resetScores(Messages.read("scoreboard.lobby.time1").toString());
                waittime1=waittime;
                lobby.getScore(Messages.read("scoreboard.lobby.time").toString().replace("%sec%",String.valueOf(waittime1))).setScore(6);
            }
            else if(waittime==100)
            {
                lobby.getScoreboard().resetScores(Messages.read("scoreboard.lobby.time").toString().replace("%sec%",String.valueOf(waittime1)));
                waittime1=waittime;
                lobby.getScore(Messages.read("scoreboard.lobby.time1").toString()).setScore(6);
            }
            else
            {
                lobby.getScoreboard().resetScores(Messages.read("scoreboard.lobby.time").toString().replace("%sec%",String.valueOf(waittime1)));
                waittime1=waittime;
                lobby.getScore(Messages.read("scoreboard.lobby.time").toString().replace("%sec%",String.valueOf(waittime1))).setScore(6);
            }
        }
    }


    private static int survivalplayers1;
    public static int survivalplayers;

    public static Map<Player,Integer> kills1 = new HashMap<>();
    public static Map<Player,Integer> kills = new HashMap<>();

    private static void reloadinGameScoreboard(){

        if(survivalplayers!=survivalplayers1)
        {
            for(int i=0;i<Bukkit.getOnlinePlayers().size();i++)
            {

                ingame.get(Bukkit.getOnlinePlayers().toArray()[i]).getScoreboard().resetScores(Messages.read("scoreboard.ingame.player").toString().replace("%now%",String.valueOf(survivalplayers1)));
                survivalplayers1=survivalplayers;
                ingame.get(Bukkit.getOnlinePlayers().toArray()[i]).getScore(Messages.read("scoreboard.ingame.player").toString().replace("%now%",String.valueOf(survivalplayers1))).setScore(8);

            }
        }
        
        for (Player player : Bukkit.getOnlinePlayers()) {

            if(kills.get(player)!=kills1.get(player))
            {
                ingame.get(player).getScoreboard().resetScores(Messages.read("scoreboard.ingame.kills").toString().replace("%kills%",String.valueOf(kills1.get(player))));
                kills1.replace(player,kills.get(player));
                ingame.get(player).getScore(Messages.read("scoreboard.ingame.kills").toString().replace("%kills%",String.valueOf(kills1.get(player)))).setScore(4);
            }
            break;
        }
    }

    public static void newIngameSB(Player player){

        if(Scoreboard.kills.containsKey(player))
            Scoreboard.kills.replace(player,0);
        else
            Scoreboard.kills.put(player,0);
        if(Scoreboard.kills1.containsKey(player))
            Scoreboard.kills1.replace(player,0);
        else
            Scoreboard.kills1.put(player,0);

        Objective newingame=Bukkit.getScoreboardManager().getNewScoreboard().registerNewObjective("ingame","dummy", Messages.read("scoreboard.title").toString());
        newingame.setDisplaySlot(DisplaySlot.SIDEBAR);

        newingame.getScore("§1").setScore(9);
        newingame.getScore(Messages.read("scoreboard.ingame.player").toString().replace("%now%",String.valueOf(survivalplayers1))).setScore(8);
        newingame.getScore("§2").setScore(7);
        newingame.getScore(Messages.read("scoreboard.ingame.kills").toString().replace("%kills%","0")).setScore(6);
        newingame.getScore("§3").setScore(5);
        newingame.getScore(Messages.read("scoreboard.ingame.map").toString().replace("%map%",Config.read("map.name").toString())).setScore(4);
        newingame.getScore(Messages.read("scoreboard.ingame.mode").toString().replace("%mode%",Config.read("map.mode").toString())).setScore(3);
        newingame.getScore("§4").setScore(2);
        newingame.getScore(Messages.read("scoreboard.ingame.info").toString()).setScore(1);

        if(ingame.containsKey(player))
            ingame.replace(player,newingame);
        else
            ingame.put(player,newingame);

    }

    public static void newKill(Player player)
    {
        kills.replace(player,kills.get(player)+1);
    }

}
