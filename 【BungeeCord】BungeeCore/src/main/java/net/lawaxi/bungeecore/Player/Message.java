package net.lawaxi.bungeecore.Player;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Message {

    public static void sendLine(ProxiedPlayer player){
        player.sendMessage(getLine());
    }

    public static String getLine(){
        return "§6---------------------------------------------";
    }

    public static void sendLine2(ProxiedPlayer player){
        player.sendMessage(getLine2());
    }

    public static String getLine2(){
        return "§9---------------------------------------------";
    }

    public static void sendLine25(ProxiedPlayer player){
        player.sendMessage(getLine25());
    }

    public static String getLine25(){
        return "§9§m---------------------------------------------";
    }

}


