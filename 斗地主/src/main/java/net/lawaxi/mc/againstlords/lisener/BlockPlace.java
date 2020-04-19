package net.lawaxi.mc.againstlords.lisener;

import net.lawaxi.mc.againstlords.AgainstLords;
import net.lawaxi.mc.againstlords.utils.Game;
import net.lawaxi.mc.againstlords.utils.GameGUI;
import net.lawaxi.mc.againstlords.utils.LordChosingGUI;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class BlockPlace implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if(AgainstLords.isInGame)
        {
            if(LordChosingGUI.chosed)
            {
                if(event.getPlayer().equals(Game.playeringame.get(GameGUI.now%Game.playeringame.size()))) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            event.getPlayer().openInventory(GameGUI.GAME);
                        }
                    }.runTask(AgainstLords.instance);

                    event.setCancelled(true);
                    return;
                }
            }
            else
            {
                if(event.getPlayer().equals(Game.playeringame.get(LordChosingGUI.now))) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            event.getPlayer().openInventory(LordChosingGUI.LordChosing);
                        }
                    }.runTask(AgainstLords.instance);

                    event.setCancelled(true);
                    return;
                }
            }
            event.getPlayer().sendTitle(AgainstLords.getMessages("notdutytitle"),AgainstLords.getMessages("notdutysub"),20,40,20);
            event.setCancelled(true);
        }
    }

}
