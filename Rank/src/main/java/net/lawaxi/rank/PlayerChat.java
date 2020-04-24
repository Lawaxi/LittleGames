package net.lawaxi.rank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.HashMap;

public class PlayerChat implements Listener {

   public static HashMap<Player,String> level = new HashMap<>();
   public static HashMap<Player,Boolean> canshout = new HashMap<>();

    @EventHandler(ignoreCancelled = true)
    public void onPlayerChat(AsyncPlayerChatEvent event)
    {
        event.setCancelled(true);
        if(event.getMessage().substring(0,1).equals("@"))
        {
            if(canshout.get(event.getPlayer())) {
                //Shout
                try {
                    Bukkit.broadcastMessage(ShoutMessage(event.getPlayer(), event.getMessage().substring(1)));
                }
                catch (IllegalStateException e)
                {
                    return;
                }
            }
            else {
                event.getPlayer().sendMessage(Rank.config.get("chat.cannotshout").toString()
                        .replace("%rank%", Rank.ranks.getString("types." + Rank.config.getString("chat.shoutlevel") + ".name")));
            }
        }
        else {
            //Common
            try {
                Bukkit.broadcastMessage(CommonMessage(event.getPlayer(), event.getMessage()));
            }
            catch (IllegalStateException e)
            {
                return;
            }
        }
    }


    public static String CommonMessage(Player player,String message){
        return Rank.config.get("chat.common").toString()
                .replace("%level%",level.get(player))
                .replace("%player1%",player.getName())
                .replace("%player2%",player.getDisplayName())
                .replace("%message%",message);
    }
    public static String ShoutMessage(Player player,String message){
        return Rank.config.get("chat.shout").toString()
                .replace("%level%",level.get(player))
                .replace("%player1%",player.getName())
                .replace("%player2%",player.getDisplayName())
                .replace("%message%",message);
    }
}
