package net.lawaxi.mcgame.skywars.listeners;

import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.config.Config;
import net.lawaxi.mcgame.skywars.utils.GameState;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockBreak implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void beforeBreak(BlockBreakEvent event) {
        if((!Skywars.state.equals(GameState.InGame)) && event.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
        {
            event.setCancelled(true);
        }
    }
}
