package net.lawaxi.bungeecore.Player;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.HashMap;

public class PlayerUtils {

    public static ArrayList<ProxiedPlayer> players = new ArrayList<>();

    public static ProxiedPlayer searchPlayer(String p){
        for(ProxiedPlayer player: PlayerUtils.players){
            if(player.getName().equalsIgnoreCase(p))
            {
                return player;
            }
        }
        return null;
    }
}
