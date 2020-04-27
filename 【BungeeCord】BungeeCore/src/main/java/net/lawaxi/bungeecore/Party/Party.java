package net.lawaxi.bungeecore.Party;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class Party {

    public ProxiedPlayer leader;
    public ArrayList<ProxiedPlayer> players = new ArrayList<>(); //包括leader
    public String name;

    public Party(ProxiedPlayer leader, String name) {
        this.leader = leader;
        this.players.add(leader);
        this.name = name;
    }

    public void sendBoardMessage(String message){
        for(ProxiedPlayer player1 : this.players){
            player1.sendMessage(message);
        }
    }

    public void sendBoardMessage(String message,ProxiedPlayer exclude){
        for(ProxiedPlayer player1 : this.players){
            if(!player1.equals(exclude))
                player1.sendMessage(message);
        }
    }
}
