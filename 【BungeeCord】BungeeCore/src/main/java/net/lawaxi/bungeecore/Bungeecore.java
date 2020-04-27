package net.lawaxi.bungeecore;

import net.lawaxi.bungeecore.Chat.ChatCommand;
import net.lawaxi.bungeecore.Chat.ChatMode;
import net.lawaxi.bungeecore.Chat.ChatUtils;
import net.lawaxi.bungeecore.Chat.MsgCommand;
import net.lawaxi.bungeecore.Commands.Lobby;
import net.lawaxi.bungeecore.Friend.FriendCommand;
import net.lawaxi.bungeecore.Friend.FriendConfig;
import net.lawaxi.bungeecore.Friend.FriendRequest;
import net.lawaxi.bungeecore.Party.PartyCommand;
import net.lawaxi.bungeecore.Party.PartyInvite;
import net.lawaxi.bungeecore.Party.PartyUtils;
import net.lawaxi.bungeecore.Player.Message;
import net.lawaxi.bungeecore.Player.PlayerUtils;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.event.ServerConnectedEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class Bungeecore extends Plugin implements Listener {

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.getProxy().getPluginManager().registerCommand(this, new PartyCommand(this));
        this.getProxy().getPluginManager().registerCommand(this, new FriendCommand(this));
        this.getProxy().getPluginManager().registerCommand(this, new Lobby());
        this.getProxy().getPluginManager().registerCommand(this, new MsgCommand());
        this.getProxy().getPluginManager().registerCommand(this, new ChatCommand());
        this.getProxy().getPluginManager().registerListener(this, this);

        FriendConfig.file = new File(getDataFolder(),"friends.yml");
        if(!getDataFolder().exists())
            getDataFolder().mkdir();

        if(!FriendConfig.file.exists())
        {
            try{
                FriendConfig.file.createNewFile();
            }
            catch (IOException e){}
        }
        //FriendConfig.reloadConfig();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static HashMap<String, ArrayList<String>> friendremoveInfos = new HashMap<>();
    @EventHandler
    public void onLog(PostLoginEvent e){

        //1.玩家个人纪录初始化
        PlayerUtils.players.add(e.getPlayer());
        ChatUtils.chatModes.put(e.getPlayer(), ChatMode.PUBLIC);

        //2.重载好友数据配置
        FriendConfig.reloadConfig();
        if(friendremoveInfos.containsKey(e.getPlayer().getName()))
        {
            Message.sendLine25(e.getPlayer());
            e.getPlayer().sendMessage("§6您离线的时候");
            String players = "";
            for(String p : friendremoveInfos.get(e.getPlayer().getName())) {
                players+="§7"+p+"§6,";
            }
            e.getPlayer().sendMessage(players.substring(0,players.length()-3)+"§6 删除了您的好友");
            Message.sendLine25(e.getPlayer());

            friendremoveInfos.remove(e.getPlayer().getName());
        }

        //3.好友上线提示
        for(String a: FriendConfig.getPlayerFriends(e.getPlayer().getName()))
        {
            ProxiedPlayer player = PlayerUtils.searchPlayer(a);
            if(player!=null)
                player.sendMessage("§2(§a√§2) §a"+e.getPlayer().getName());
        }
    }


    @EventHandler
    public void onQuit(PlayerDisconnectEvent e){

        //1.玩家个人记录清除
        PlayerUtils.players.remove(e.getPlayer());
        ChatUtils.chatModes.remove(e.getPlayer());

        //2.1离开队伍
        switch (PartyUtils.getParty(e.getPlayer())){
            case 1:
                PartyCommand.leave(e.getPlayer(),2);
                break;
            case 2:
                PartyCommand.leave(e.getPlayer(),1);
        }

        //2.2结束传送请求
        for(PartyInvite invite:PartyInvite.invites)
        {
            if(invite.from.equals(e.getPlayer())) {
                Message.sendLine(invite.to);
                invite.to.sendMessage("§c玩家 §7"+e.getPlayer().getName()+"§c 已退出，组队邀请自动取消.");
                Message.sendLine(invite.to);
            }
            else if(invite.to.equals(e.getPlayer())){
                Message.sendLine(invite.from);
                invite.from.sendMessage("§c玩家 §7"+e.getPlayer().getName()+"§c 已退出，组队邀请自动取消.");
                Message.sendLine(invite.from);
            }
        }

        //3结束好友请求
        for(FriendRequest request:FriendRequest.friendRequests){
            if(request.from.equals(e.getPlayer())) {
                Message.sendLine2(request.to);
                request.to.sendMessage("§c玩家 §7"+e.getPlayer().getName()+"§c 已退出，好友请求自动取消.");
                Message.sendLine2(request.to);
            }
            else if(request.to.equals(e.getPlayer())){
                Message.sendLine2(request.from);
                request.from.sendMessage("§c玩家 §7"+e.getPlayer().getName()+"§c 已退出，好友请求自动取消.");
                Message.sendLine2(request.from);
            }
        }

        //4.好友上线提示
        for(String a: FriendConfig.getPlayerFriends(e.getPlayer().getName()))
        {
            ProxiedPlayer player = PlayerUtils.searchPlayer(a);
            if(player!=null)
                player.sendMessage("§4(§c×§4) §c"+e.getPlayer().getName());
        }
    }

    @EventHandler
    public void onChangeServer(ServerConnectedEvent e){
        if(PartyUtils.playersParty.containsKey(e.getPlayer()))
        {
            if(PartyUtils.playersParty.get(e.getPlayer()).leader.equals(e.getPlayer())) {
                for (ProxiedPlayer player : PartyUtils.playersParty.get(e.getPlayer()).players) {
                    if (!player.equals(e.getPlayer())) {

                        ServerInfo a = e.getServer().getInfo();
                        player.connect(a);

                        Message.sendLine(player);
                        player.sendMessage("§a成功传送至 §2" + a.getName());
                        Message.sendLine(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onChat(ChatEvent e){

                                                      //输入指令也会调用这个事件 需要避免
        if(e.getSender() instanceof ProxiedPlayer && !e.getMessage().substring(0,1).equalsIgnoreCase("/")){
            if(ChatUtils.chatModes.get(e.getSender()).equals(ChatMode.PARTY)){

                if(!PartyUtils.playersParty.containsKey(e.getSender()))
                {
                    ((ProxiedPlayer) e.getSender()).sendMessage("§c您不在任何一个队伍中，现已转到公共频道");
                    ChatUtils.chatModes.replace((ProxiedPlayer) e.getSender(),ChatMode.PUBLIC);
                }
                else {
                    ChatUtils.sendPartyMessage((ProxiedPlayer)e.getSender(),e.getMessage());
                }
                e.setCancelled(true);
            }
        }
    }
}