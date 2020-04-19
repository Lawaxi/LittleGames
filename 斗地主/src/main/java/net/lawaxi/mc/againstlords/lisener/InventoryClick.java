package net.lawaxi.mc.againstlords.lisener;

import net.lawaxi.mc.againstlords.AgainstLords;
import net.lawaxi.mc.againstlords.utils.GameGUI;
import net.lawaxi.mc.againstlords.utils.LordChosingGUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;

public class InventoryClick implements Listener {

    @EventHandler
    public void click(InventoryClickEvent event) {
        if (AgainstLords.isInGame) {
            if(event.getInventory().equals(LordChosingGUI.LordChosing))
            {
                LordChosingGUI.choose(event.getCurrentItem());
                event.setCancelled(true);
                event.getWhoClicked().closeInventory();
            }
        }

    }
}