package net.lawaxi.bungeecore.Player;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Message {

    public static void sendLine(ProxiedPlayer player){
        player.sendMessage(getLine());
    }

    public static String getLine(){
        return "§6---------------------------------------------";
    }
}
