package net.lawaxi.mcgame.skywars.listeners;

import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.init.Player;
import net.lawaxi.mcgame.skywars.utils.GameState;
import net.lawaxi.mcgame.skywars.utils.Scoreboard;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    //玩家进入大厅（将在玩家进入及游戏重载时的伪玩家进入时调用）
    //Player.reloadplayer();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        if(Skywars.state.equals(GameState.InGame))
        {
            event.getPlayer().setScoreboard(Scoreboard.ingame.get(event.getPlayer()).getScoreboard());
        }
        else
        {
            Scoreboard.player++;
            event.getPlayer().setScoreboard(Scoreboard.lobby.getScoreboard());
        }

        Player.loadPlayer(event.getPlayer());
        Scoreboard.reloadScoreboard();
    }
}
