package net.lawaxi.mcgame.skywars.init.tasks;

import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.config.Config;
import net.lawaxi.mcgame.skywars.config.Messages;
import net.lawaxi.mcgame.skywars.init.Game;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.defaults.ReloadCommand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GameEndTask extends BukkitRunnable {

    private int count;
    private int time;

    public GameEndTask(int time){
        this.time=time;
        this.count=0;
    }

    private static String getWinner(){

        for(org.bukkit.entity.Player player1 : Bukkit.getOnlinePlayers())
        {
            if(player1.getGameMode().equals(GameMode.SURVIVAL))
                return Messages.read("ingame.winner").toString().replace("%player%",player1.getName());

        }
        return Messages.read("ingame.winner1").toString();
    }

    @Override
    public void run() {

        if(count==0)
        {
            Bukkit.broadcastMessage(Messages.read("ingame.end").toString().replace("%winner%", getWinner()));

            for(org.bukkit.entity.Player player1 : Bukkit.getOnlinePlayers())
            {
                if(player1.getGameMode().equals(GameMode.SURVIVAL))
                {
                    net.lawaxi.mcgame.skywars.init.Player.death(player1);
                    player1.sendTitle(Messages.read("ingame.end2.title").toString(),
                        Messages.read("ingame.end2.sub").toString(),20,40,20);
                }
                else
                    player1.sendTitle(Messages.read("ingame.end1.title").toString(),
                            Messages.read("ingame.end1.sub").toString(),20,40,20);
            }

        }

        Bukkit.broadcastMessage(Messages.read("ingame.reset").toString().replace("%time%",String.valueOf(time-count)));
        count++;

        if(count==time)
        {
            for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers())
                player.kickPlayer(Messages.read("server.reload").toString());

            new BukkitRunnable(){
                @Override
                public void run() {

                    Skywars.server.reload();

                }
            }.runTask(Skywars.instance);
            this.cancel();

        }
    }
}
