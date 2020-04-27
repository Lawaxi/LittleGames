package net.lawaxi.bungeecore;

import net.lawaxi.bungeecore.Party.PartyCommand;
import net.lawaxi.bungeecore.Party.PartyUtils;
import net.lawaxi.bungeecore.Player.Message;
import net.lawaxi.bungeecore.Player.PlayerUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

public final class Bungeecore extends Plugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.getProxy().getPluginManager().registerCommand(this, new PartyCommand(this));
        this.getProxy().getPluginManager().registerListener(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onLog(PostLoginEvent e){
        PlayerUtils.players.add(e.getPlayer());
    }


    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){
        PlayerUtils.players.remove(e.getPlayer());

        switch (PartyUtils.getParty(e.getPlayer())){
            case 1:
                PartyCommand.leave(e.getPlayer(),2);
                break;
            case 2:
                PartyCommand.leave(e.getPlayer(),1);
        }
    }

    @EventHandler
    public void onChangeServer(ServerConnectedEvent e){
        if(PartyUtils.playersParty.containsKey(e.getPlayer()))
        {
            if(PartyUtils.playersParty.get(e.getPlayer()).leader.equals(e.getPlayer())) {
                for (ProxiedPlayer player : PartyUtils.playersParty.get(e.getPlayer()).players) {
                    if (!player.equals(e.getPlayer())) {

                        ServerInfo a = e.getPlayer().getServer().getInfo();
                        player.connect(a);

                        Message.sendLine(player);
                        player.sendMessage("§a成功传送至 §2" + a.getName());
                        Message.sendLine(player);
                    }
                }
            }
        }
    }
}
