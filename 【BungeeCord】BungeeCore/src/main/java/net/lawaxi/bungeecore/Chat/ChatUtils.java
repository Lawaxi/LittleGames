package net.lawaxi.bungeecore.Chat;

import net.lawaxi.bungeecore.Party.PartyUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

public class ChatUtils {

    public static HashMap<ProxiedPlayer,ChatMode> chatModes = new HashMap<>();

    public static void sendPartyMessage(ProxiedPlayer player,String message){

        PartyUtils.playersParty.get(player).sendBoardMessage(
                "§9队伍 >> §f" + player.getName() + "§f: " + message
        );
    }

    public static void sendPrivateMessage(ProxiedPlayer to, CommandSender from, String message){

        to.sendMessage(
                "§d"+from.getName()+"§5 >> §d私聊§f: " + message
        );

        from.sendMessage(
                "§d私聊 §5<< §d"+from.getName()+"§f: " + message
        );
    }
}
