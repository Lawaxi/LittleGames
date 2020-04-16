package net.lawaxi.mcgame.skywars.listeners;

import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.config.Messages;
import net.lawaxi.mcgame.skywars.init.Game;
import net.lawaxi.mcgame.skywars.utils.GameState;
import net.lawaxi.mcgame.skywars.utils.Scoreboard;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerDeath implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent event) {

        Player player = event.getEntity();

        if(Skywars.state.equals(GameState.InGame)){

            utils.leavegame(player);

        }

    }
}
