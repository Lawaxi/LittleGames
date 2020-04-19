package net.lawaxi.mc.againstlords.lisener;

import net.lawaxi.mc.againstlords.AgainstLords;
import net.lawaxi.mc.againstlords.utils.Card;
import net.lawaxi.mc.againstlords.utils.GameGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerDropItemEvent;

public class PlayerDropItem implements Listener {

    @EventHandler
    public void close(PlayerDropItemEvent event) {
        if (AgainstLords.isInGame) {
            event.setCancelled(true);
        }

    }
}