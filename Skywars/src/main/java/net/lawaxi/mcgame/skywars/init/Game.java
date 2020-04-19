package net.lawaxi.mcgame.skywars.init;

import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.config.Config;
import net.lawaxi.mcgame.skywars.init.tasks.GameEndTask;
import net.lawaxi.mcgame.skywars.utils.GameState;
import net.lawaxi.mcgame.skywars.utils.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

import static net.lawaxi.mcgame.skywars.utils.Scoreboard.startScoreboard;

public class Game {

    public static BukkitTask gamewaittask;
    public static void startGame(){

        Scoreboard.survivalplayers=Scoreboard.player;

        for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers())
        {
            Player.ingame(player);
            player.setScoreboard(Scoreboard.ingame.get(player).getScoreboard());
        }

        Skywars.state= GameState.InGame;
        Scoreboard.reloadScoreboard();
    }

    public static void initializeGame() {

        startScoreboard();
        Skywars.state= GameState.Lobby;

        Player.spawnlocationplayers=new HashMap<>();
        Player.killer=new HashMap<>();
    }

    public static void endGame(){
        new GameEndTask((int) Config.read("ingame.endtime")).runTaskTimer(Skywars.instance,20,(int) Config.read("ingame.endtime"));
    }


}
