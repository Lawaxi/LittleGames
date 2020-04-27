package net.lawaxi.bungeecore.Party;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.HashMap;

public class PartyUtils {
    public static HashMap<ProxiedPlayer,Party> playersParty = new HashMap<>();

    public static int getParty(ProxiedPlayer player){
        //不在队伍返回0
        //在队伍且是队长返回1
        //在队伍且是组员返回2

        if(!PartyUtils.playersParty.containsKey(player)) {
            return 0;
        }

        if(PartyUtils.playersParty.get(player).leader.equals(player)){
            return 1;
        }
        return 2;
    }
}
