package net.lawaxi.bungeecore.Friend;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FriendConfig {


    public static Configuration config;
    public static File file;

    public static void saveConfig(){
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void reloadConfig() {
        try {
            config= ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<String> getPlayerFriends(String player){

        if(!config.contains(player))
            config.set(player, new ArrayList<>());

        ArrayList<String> a = (ArrayList<String>) config.getList(player);
        for (String player1 : a) {

            //检验双方好友制
            if (!config.getList(player1).contains(player)) {
                a.remove(player1);
                config.set(player, a);
            }
        }
        saveConfig();
        return a;
    }

    public static void addFriend(ProxiedPlayer a,ProxiedPlayer b){

        ArrayList<String> temp = getPlayerFriends(a.getName());
        ArrayList<String> temp1 = getPlayerFriends(b.getName());

        temp.add(b.getName());
        config.set(a.getName(),temp.clone());
        temp1.add(a.getName());
        config.set(b.getName(),temp1.clone());

        saveConfig();
    }

    public static void removeFriend(ProxiedPlayer a,String b){

        ArrayList<String> temp = getPlayerFriends(a.getName());
        ArrayList<String> temp1 = getPlayerFriends(b);

        temp.remove(b);
        config.set(a.getName(),temp.clone());
        temp1.remove(a.getName());
        config.set(b,temp1.clone());

        saveConfig();
    }

}
