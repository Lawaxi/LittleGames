package net.lawaxi.mc.againstlords.lisener;

import net.lawaxi.mc.againstlords.AgainstLords;
import net.lawaxi.mc.againstlords.utils.Game;
import net.lawaxi.mc.againstlords.utils.GameGUI;
import net.lawaxi.mc.againstlords.utils.LordChosingGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

public class InventoryClose implements Listener {

    @EventHandler
    public void close(InventoryCloseEvent event) {
        if (AgainstLords.isInGame) {
            if(event.getInventory().equals(GameGUI.GAME))
            {
                GameGUI.launch((Player)event.getPlayer());
            }
        }

    }
}