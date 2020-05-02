package net.lawaxi.mc.minesweeper.Game.JGG;

import net.lawaxi.mc.minesweeper.Game.Sounds;
import net.lawaxi.mc.minesweeper.InventoryGames;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.HashMap;

public class JGG implements Listener {

    public static void init(){
        jggInventory.title = InventoryGames.config.getString("JGG.title");
        jggInventory.size= InventoryGames.config.getInt("JGG.inventory.size");
        jggInventory.slot=(ArrayList<Integer>) InventoryGames.config.getList("JGG.inventory.slot");
        jggInventory.duty= InventoryGames.config.getInt("JGG.inventory.duty");

        jggInventory.none= InventoryGames.getItemStack("JGG.display.none");
        jggInventory.playerb.add(InventoryGames.getItemStack("JGG.display.player1"));
        jggInventory.playerb.add(InventoryGames.getItemStack("JGG.display.player2"));
        jggInventory.finishb.add(InventoryGames.getItemStack("JGG.display.finish1"));
        jggInventory.finishb.add(InventoryGames.getItemStack("JGG.display.finish2"));
    }


    public static HashMap<Player,jggInventory> JGGs = new HashMap<>();
    public static Player request;

    public static void start(Player player){

        if(JGGs.containsKey(player)){
            player.openInventory(JGGs.get(player).inventory);
        }
        else
        {
            if(request==null){
                request=player;

                TextComponent a = new TextComponent();
                a.setText(InventoryGames.config.getString("JGG.messages.request")
                        .replace("%player%",player.getDisplayName()));
                a.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/jgg"));
                a.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,new ComponentBuilder(InventoryGames.config.getString("JGG.messages.requesthover")).create()));
                for(Player player1:Bukkit.getOnlinePlayers()){
                    player1.spigot().sendMessage(a);
                }

            }
            else if(player!=request)
            {
                request.sendMessage(InventoryGames.config.getString("JGG.messages.accept")
                        .replace("%player%",player.getDisplayName()));

                jggInventory a = new jggInventory(request,player);
                JGGs.put(request,a);
                JGGs.put(player,a);

                request.openInventory(a.inventory);
                player.openInventory(a.inventory);

                Sounds.StartSound(request);
                Sounds.StartSound(player);

                request=null;
            }
            else if(player==request){
                request.sendMessage(InventoryGames.config.getString("JGG.messages.already"));
            }

        }

    }


    @EventHandler
    private static void Quit(PlayerQuitEvent e){
        if(JGGs.containsKey(e.getPlayer()))
        {
            JGGs.get(e.getPlayer()).player.get(0).sendMessage(InventoryGames.config.getString("JGG.messages.quit")
                    .replace("%player%",e.getPlayer().getDisplayName()));

            JGGs.get(e.getPlayer()).player.get(1).sendMessage(InventoryGames.config.getString("JGG.messages.quit")
                    .replace("%player%",e.getPlayer().getDisplayName()));

            JGGs.remove(JGGs.get(e.getPlayer()).player.get(0));
            JGGs.remove(JGGs.get(e.getPlayer()).player.get(1));
        }

        if(request==e.getPlayer())
            request = null;
    }

    @EventHandler(ignoreCancelled = true)
    private static void onChoose(InventoryClickEvent e){
        if(JGGs.containsKey(e.getWhoClicked()))
        {
            if(e.getInventory().equals(JGGs.get(e.getWhoClicked()).inventory))
            {
                e.setCancelled(true);
                if(JGGs.get(e.getWhoClicked()).getNow().equals(e.getWhoClicked())){
                    JGGs.get(e.getWhoClicked()).choose(e.getSlot());
                }
                else
                {
                    e.getWhoClicked().sendMessage(InventoryGames.config.getString("JGG.messages.noduty"));
                }
            }

        }
        /*
        else if(e.getInventory().getTitle().equals(jggInventory.title))
        {
            e.setCancelled(true);
        }*/
    }
}
