package net.lawaxi.rank;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerJoin implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event)
    {
        Rank.reloadPlayerLevels(event.getPlayer().getName());
        Rank.reloadScoreboard(event.getPlayer());
    }
}
