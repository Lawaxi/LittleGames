package net.lawaxi.mcgame.skywars.listeners;

import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.init.Player;
import net.lawaxi.mcgame.skywars.utils.GameState;
import net.lawaxi.mcgame.skywars.utils.Scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scoreboard.Score;

public class PlayerQuit implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {

        if(Skywars.state== GameState.InGame)
        {
            utils.leavegame(event.getPlayer());
        }
        else
        {
            Player.spawnlocationplayers.remove(event.getPlayer());

            Scoreboard.player--;
            Scoreboard.reloadScoreboard();
        }
    }
}
