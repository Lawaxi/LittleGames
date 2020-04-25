package net.lawaxi.mc.bridgepractice.listeners;

import net.lawaxi.mc.bridgepractice.Bridgepractice;
import net.lawaxi.mc.bridgepractice.utils.PlayerUtils;
import net.lawaxi.mc.bridgepractice.utils.onBlock;
import net.lawaxi.mc.bridgepractice.utils.utils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import java.util.ArrayList;
import java.util.HashMap;

public class others implements Listener {

    @EventHandler(ignoreCancelled = true)
    private static void onMove(PlayerMoveEvent e){
        Location a =e.getFrom();
        a.setY(a.getY()-1);
        if(a.getBlock().getType().equals(Material.getMaterial(Bridgepractice.config.getString("block.start"))))
            onBlock.start(e.getPlayer(),a);
        else if(a.getBlock().getType().equals(Material.getMaterial(Bridgepractice.config.getString("block.end"))))
            onBlock.end(e.getPlayer(),a);
        else if(a.getBlock().getType().equals(Material.getMaterial(Bridgepractice.config.getString("block.return"))))
            onBlock.return1(e.getPlayer(),a);
    }

    @EventHandler
    private static void onJoin(PlayerJoinEvent e){
        PlayerUtils.playersblock.put(e.getPlayer(),new ArrayList<>());
        PlayerUtils.location.put(e.getPlayer(),Bridgepractice.joinLocation);

        e.getPlayer().teleport(Bridgepractice.joinLocation);
        PlayerUtils.giveBlocks(e.getPlayer());
    }

    @EventHandler
    private static void onQuit(PlayerQuitEvent e){
        new BukkitRunnable(){
            @Override
            public void run() {

                ArrayList<Location> list = (ArrayList<Location>)PlayerUtils.playersblock.get(e.getPlayer()).clone();
                PlayerUtils.playersblock.remove(e.getPlayer());
                PlayerUtils.location.remove(e.getPlayer());

                //清除杀人记录
                for(Player player: Bukkit.getOnlinePlayers()){
                    if(antimc.maybeKiller.containsKey(player))
                    {
                        if(antimc.maybeKiller.get(player).equals(e.getPlayer()))
                            antimc.maybeKiller.remove(player);
                    }
                }

                PlayerUtils.clearBlocks(list);

                this.cancel();
            }
        }.runTask(Bridgepractice.instance);
    }

    private static HashMap<Player, Integer> times = new HashMap<>();
    @EventHandler
    private static void onBlockPlace(BlockPlaceEvent e){
        if(!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
            PlayerUtils.playersblock.get(e.getPlayer()).add(e.getBlock().getLocation());

            if(times.containsKey(e.getPlayer())) {
                int second = utils.getSecond();
                int space = second-times.get(e.getPlayer());
                if(space!=0)
                {
                    double a =utils.getDouble(1000.0/space);
                    if(a>0) {
                        e.getPlayer().sendTitle(
                                Bridgepractice.config.getString("messages.speed.title")
                                        .replace("%n%", String.valueOf(a)),
                                Bridgepractice.config.getString("messages.speed.sub")
                                        .replace("%n%", String.valueOf(a)),
                                20, 40, 20);
                    }
                }
                times.replace(e.getPlayer(), second);
            }
            else
                times.put(e.getPlayer(),utils.getSecond());


            e.getPlayer().getItemInHand().setAmount(64);
        }
    }
}
