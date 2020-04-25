package net.lawaxi.mc.bridgepractice.listeners;

import net.lawaxi.mc.bridgepractice.Bridgepractice;
import net.lawaxi.mc.bridgepractice.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;

public class antimc implements Listener {

    @EventHandler
    private static void onFlChanged(FoodLevelChangeEvent e){
        if(e.getEntity() instanceof Player)
            ((Player) e.getEntity()).setFoodLevel(20);
    }

    @EventHandler
    private static void onInteractVillage(PlayerInteractEntityEvent e){
        if(e.getRightClicked() instanceof Villager)
            e.setCancelled(true);
    }

    @EventHandler
    private static void onBreakBlock(BlockBreakEvent e){
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            e.setCancelled(true);
    }
    @EventHandler
    private static void onOpenInventory(InventoryOpenEvent e){
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE))
            e.setCancelled(true);
    }


    @EventHandler
    private static void onDamaged(EntityDamageEvent e){
        if(e.getEntity() instanceof Player) {
            if(e.getEntity().getLocation().getY()>0) {
                if (e.getDamage() >= 20)
                    ((Player) e.getEntity()).sendTitle(
                            Bridgepractice.config.getString("messages.damaged2.title").replace("%n%", String.valueOf(e.getDamage())),
                            Bridgepractice.config.getString("messages.damaged2.sub").replace("%n%", String.valueOf(e.getDamage())), 20, 40, 20);
                else {
                    ((Player) e.getEntity()).sendTitle(
                            Bridgepractice.config.getString("messages.damaged1.title").replace("%n%", String.valueOf(e.getDamage())),
                            Bridgepractice.config.getString("messages.damaged1.sub").replace("%n%", String.valueOf(e.getDamage())), 20, 40, 20);
                }
            }
            else
            {
                new BukkitRunnable(){
                    @Override
                    public void run() {
                        e.getEntity().teleport(PlayerUtils.location.get(e.getEntity()));
                        PlayerUtils.clearBlocks((ArrayList<Location>)PlayerUtils.playersblock.get(e.getEntity()).clone());
                        this.cancel();
                    }
                }.runTask(Bridgepractice.instance);

            }
            e.setCancelled(true);
        }
    }

}
