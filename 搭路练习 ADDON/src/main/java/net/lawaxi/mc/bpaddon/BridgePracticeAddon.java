package net.lawaxi.mc.bpaddon;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.logging.Logger;

public final class BridgePracticeAddon extends JavaPlugin implements Listener {

    private static BridgePracticeAddon instance;
    private static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        this.getServer().getPluginManager().registerEvents(this, this);
        instance=this;
        logger=getLogger();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(ignoreCancelled = true)
    private static void onTNT(EntityExplodeEvent e){
        e.blockList().clear();
    }

    @EventHandler
    private static void onMove(PlayerMoveEvent e){
        Location a =e.getPlayer().getLocation();
        a.setY(a.getBlockY()-1);
        Block b = e.getPlayer().getWorld().getBlockAt(a.getBlockX(),a.getBlockY(),a.getBlockZ());

        if(b.getType().equals(Material.GOLD_BLOCK))
        {
            e.getPlayer().addPotionEffect(
                    new PotionEffect(PotionEffectType.SPEED,60,1,true,true, Color.YELLOW)
            );
        }
    }

    @EventHandler(ignoreCancelled = true)
    private static void onBlock(BlockPlaceEvent e){

        if(e.getPlayer().getItemInHand().getType().equals(Material.TNT))
        {
            e.setCancelled(true);
            Location a = e.getBlockPlaced().getLocation();

            if(e.getPlayer().getWorld().getBlockAt(a.getBlockX(),a.getBlockY()-1,a.getBlockZ()).getType().equals(Material.BOOKSHELF)) {

                a.setX(a.getBlockX() + 0.5);
                a.setZ(a.getBlockZ() + 0.5);
                a.setY(a.getBlockY()+0.5);

                ((TNTPrimed)e.getBlock().getLocation().getWorld().spawn(a, TNTPrimed.class)).setFuseTicks(30);

                /*TNTPrimed tnt = e.getBlock().getLocation().getWorld().spawn(a, TNTPrimed.class);
                tnt.setYield(3.0F);
                tnt.setIsIncendiary(false);
                tnt.setFuseTicks(20);
                tnt.setMetadata("LightTNT", new FixedMetadataValue(instance, "tnt." + e.getPlayer().getName()));
                */
                e.getPlayer().getItemInHand().setAmount(0);
            }
            else {
                e.getPlayer().sendMessage("§cTNT只能被放置在指定位置");
                e.getPlayer().getItemInHand().setAmount(1);
            }
        }
        else if(e.getPlayer().getItemInHand().getType().equals(Material.FIREBALL))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    private static void onFireball(PlayerInteractEvent e){

        if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {

            if (e.hasItem()) {

                if (e.getItem().getType().equals(Material.FIREBALL)) {

                    Vector direction = e.getPlayer().getEyeLocation().getDirection().multiply(2);
                    Projectile projectile = (Projectile)e.getPlayer().getWorld().spawn(e.getPlayer().getEyeLocation().add(direction.getX(), direction.getY(), direction.getZ()), Fireball.class);
                    projectile.setShooter(e.getPlayer());
                    projectile.setVelocity(direction);

                    /*
                    Fireball fireball = e.getPlayer().launchProjectile(Fireball.class);
                    fireball.setYield(3.0F);
                    fireball.setBounce(false);
                    fireball.setShooter(e.getPlayer());
                    fireball.setIsIncendiary(false);
                    fireball.setMetadata("FireBall", new FixedMetadataValue(instance,  + e.getPlayer().getName()));
                    */
                    e.getItem().setAmount(e.getItem().getAmount()-1);
                }
            }
        }
    }


}
