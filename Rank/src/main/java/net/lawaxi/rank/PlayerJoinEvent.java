package net.lawaxi.rank;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class PlayerJoinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(org.bukkit.event.player.PlayerJoinEvent event)
    {
        Rank.reloadRanks(event.getPlayer().getName());
    }
}
