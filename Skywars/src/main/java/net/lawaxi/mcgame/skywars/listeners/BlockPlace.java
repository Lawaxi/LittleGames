package net.lawaxi.mcgame.skywars.listeners;

import net.lawaxi.mcgame.skywars.config.Config;
import org.bukkit.Material;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockPlace implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void beforePlace(BlockPlaceEvent event) {
        if(Boolean.valueOf(Config.read("ingame.autotnt").toString()))
        {
            if (event.getPlayer().getItemInHand() != null) {
                if (event.getPlayer().getItemInHand().getType() == Material.TNT) {
                    TNTPrimed tntPrimed = (TNTPrimed)event.getBlock().getLocation().getWorld().spawn(event.getBlock().getLocation(), TNTPrimed.class);
                    event.getPlayer().getItemInHand().setAmount(event.getPlayer().getItemInHand().getAmount()-1);
                    event.setCancelled(true);
                }
            }
        }
    }
}
