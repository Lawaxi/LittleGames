package net.lawaxi.mcgame.skywars.listeners;

import net.lawaxi.mcgame.skywars.config.Messages;
import net.lawaxi.mcgame.skywars.init.Game;
import net.lawaxi.mcgame.skywars.utils.Scoreboard;
import org.bukkit.entity.Player;

public class utils {

    public static void leavegame(Player player){

        Scoreboard.survivalplayers--;

        if(Scoreboard.kills.containsKey(player.getKiller()))
            Scoreboard.newKill(player.getKiller());

        net.lawaxi.mcgame.skywars.init.Player.death(player);
        player.sendTitle(Messages.read("ingame.dead.title").toString(),Messages.read("ingame.dead.sub").toString(),20,40,20);

        Scoreboard.reloadScoreboard();

        if(Scoreboard.survivalplayers==1)
            Game.endGame();
    }
}
