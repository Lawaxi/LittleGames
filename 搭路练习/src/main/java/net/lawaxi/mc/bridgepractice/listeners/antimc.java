package net.lawaxi.mc.bridgepractice.listeners;

import net.lawaxi.mc.bridgepractice.Bridgepractice;
import net.lawaxi.mc.bridgepractice.utils.PlayerUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

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
    public void onInventoryClick(InventoryOpenEvent e)
    {
        if(e.getPlayer().getGameMode().equals(GameMode.SURVIVAL))
            e.setCancelled(true);
    }

    /*
    @EventHandler
    private static void onPlaceWater(PlayerInteractEvent e){

        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE) && e.hasItem()) {
            if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK) &&
                    (e.getItem().getType().equals(Material.WATER_BUCKET) || e.getItem().getType().equals(Material.LAVA_BUCKET))) {
                {
                    new BukkitRunnable(){
                        @Override
                        public void run() {

                            Integer[] a ={-1,1};
                            for(int x:a){
                                for(int y:a){
                                    for(int z:a)
                                    {
                                        Block b=e.getClickedBlock().getLocation().add(x,y,z).getBlock();
                                        if(b.getType().equals(Material.WATER))
                                            b.setType(Material.AIR);
                                    }
                                }
                            }
                        }
                    }.runTaskLater(Bridgepractice.instance,20);
                }
            }
        }
    }*/


    @EventHandler
    private static void onDamaged(EntityDamageEvent e){
        if(e.getEntity() instanceof Player) {
            if(e.getEntity().getLocation().getY()>0) {
                if (e.getDamage() >= 20)
                {
                    ((Player) e.getEntity()).sendTitle(
                            Bridgepractice.config.getString("messages.damaged2.title").replace("%n%", String.valueOf(e.getDamage())),
                            Bridgepractice.config.getString("messages.damaged2.sub").replace("%n%", String.valueOf(e.getDamage())), 20, 40, 20);

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            e.getEntity().teleport(PlayerUtils.location.get(e.getEntity()));
                            PlayerUtils.clearBlocks((ArrayList<Location>)PlayerUtils.playersblock.get(e.getEntity()).clone());
                            this.cancel();
                        }
                    }.runTask(Bridgepractice.instance);
                }
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
                        if(maybeKiller.containsKey(e.getEntity()))
                        {
                            Bukkit.broadcastMessage(Bridgepractice.config.getString("messages.dead2")
                            .replace("%player%",((Player) e.getEntity()).getDisplayName())
                            .replace("%killer%",maybeKiller.get(e.getEntity()).getDisplayName()));

                            PlayerUtils.killing(maybeKiller.get(e.getEntity()),(Player) e.getEntity());
                        }
                        else
                        {
                            Bukkit.broadcastMessage(Bridgepractice.config.getString("messages.dead1")
                                    .replace("%player%",((Player) e.getEntity()).getDisplayName()));
                        }

                        PlayerUtils.clearBlocks((ArrayList<Location>)PlayerUtils.playersblock.get(e.getEntity()).clone());
                        this.cancel();
                    }
                }.runTask(Bridgepractice.instance);

            }
            e.setDamage(0.1);
        }
    }

    public static HashMap<Player,Player> maybeKiller = new HashMap<>();
    @EventHandler
    private static void onDamaged2(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){

            //记录被谁推下虚空
            if(maybeKiller.containsKey(e.getEntity()))
                maybeKiller.replace((Player)e.getEntity(),(Player) e.getDamager());
            else
                maybeKiller.put((Player)e.getEntity(),(Player) e.getDamager());

            //过10秒自动清除
            new BukkitRunnable(){
                @Override
                public void run() {
                    maybeKiller.remove(e.getEntity());
                }
            }.runTaskLater(Bridgepractice.instance,200);
        }
    }
}
