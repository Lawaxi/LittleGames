package net.lawaxi.bungeecore.Friend;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class FriendUtils {

    public static boolean isFriend(String a,String  b){

        if(FriendConfig.getPlayerFriends(a).contains(b))
            return true;
        return false;
    }
}
