package net.lawaxi.bungeecore.Party;

import net.lawaxi.bungeecore.Bungeecore;
import net.lawaxi.bungeecore.Player.Message;
import net.lawaxi.bungeecore.Player.PlayerUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.plugin.Command;

public class PartyCommand extends Command {

    private static Bungeecore instance;

    public PartyCommand(Bungeecore instance) {

        super("party",null,"p");
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!( sender instanceof ProxiedPlayer))
        {
            sender.sendMessage("§c只能玩家使用");
            return;
        }

        if(args.length==0) {
            Message.sendLine((ProxiedPlayer) sender);
            sendHelp((ProxiedPlayer) sender);
            Message.sendLine((ProxiedPlayer) sender);
            return;
        }


        ProxiedPlayer ps = (ProxiedPlayer) sender;
        Message.sendLine(ps);
        switch (args[0]){
            default:{
                invite(ps,args[0]);
                break;
            }
            case "help":
            {
                sendHelp(ps);
                break;
            }
            case "invite":{
                if(args.length>1){
                    invite(ps,args[1]);
                }
                else
                    ps.sendMessage("§c请输入玩家名字");
                break;
            }
            case "leave":{

                switch (PartyUtils.getParty(ps)){
                    case 0:
                        sender.sendMessage("§c您不在任何一个队伍中");
                        break;
                    case 1:
                        sender.sendMessage("§c您是队伍的队长，离开队伍将自动解散");
                        sender.sendMessage("§c如果您执意如此，请输入§4/p disband");
                        break;
                    case 2:
                    {
                        leave(ps,1);
                        break;
                    }
                }
                break;
            }
            case "remove":{

                if(args.length>1){
                    if(PartyUtils.getParty(ps)==1){
                        ProxiedPlayer player = PlayerUtils.searchPlayer(args[1]);
                        if(player!=null){
                            Party party = PartyUtils.playersParty.get(ps);
                            if(party.players.contains(player))
                                ps.sendMessage("§c玩家 §7"+args[1]+"§c 不在队伍中");
                            else
                            {
                                party.players.remove(player);
                                PartyUtils.playersParty.remove(player);
                                player.sendMessage("§c你被队长§7 "+ps.getName()+"§c 踢出了组队");
                                party.sendBoardMessage("§6队长§7 "+ps.getName()+"§6 将 §7"+args[1]+"§6 踢出了组队");
                            }

                        }
                        else
                            ps.sendMessage("§c玩家§7 "+args[1]+"§c 不在线");
                    }
                    else
                    {
                        sender.sendMessage("§c您不在队伍中或者不是队伍的队长");
                    }
                }
                else
                    ps.sendMessage("§c请输入玩家名字");
                break;
            }
            case "setname":{
                if(args.length>1){
                    if(PartyUtils.getParty(ps)==1){
                        Party party =  PartyUtils.playersParty.get(ps);
                        party.name = args[1];

                        party.sendBoardMessage(Message.getLine(),ps);
                        party.sendBoardMessage("§6队长§7 "+ps.getName()+"§6 将队伍名称设置为§f "+args[1]);
                        party.sendBoardMessage(Message.getLine(),ps);
                    }
                    else
                    {
                        sender.sendMessage("§c您不在队伍中或者不是队伍的队长");
                    }
                }
                else
                    ps.sendMessage("§c请输入名称");
                break;
            }
            case "setcolor":{
                if(args.length>1){
                    if(PartyUtils.getParty(ps)==1){
                        Party party =  PartyUtils.playersParty.get(ps);

                        if(party.name.substring(0,1).equals("§")){
                            party.name = "§"+args[1].substring(0,1)+party.name.substring(2);
                        }
                        else {
                            party.name = "§"+args[1].substring(0,1)+party.name;
                        }


                        party.sendBoardMessage(Message.getLine(),ps);
                        party.sendBoardMessage("§6队长§7 "+ps.getName()+"§6 将队伍名称设置为§f "+party.name);
                        party.sendBoardMessage(Message.getLine(),ps);
                    }
                    else
                    {
                        sender.sendMessage("§c您不在队伍中或者不是队伍的队长");
                    }
                }
                else {
                    ps.sendMessage("§6颜色用下列代号表示");
                    ps.sendMessage("§11 §22 §33 §44 §55 §66 §77 §88 §99 §00 §aa §bb §cc §dd §ee §ff");
                }
                break;
            }
            case "disband":
            {
                if(PartyUtils.getParty(ps)==1){
                    leave(ps,2);
                }
                else {
                    ps.sendMessage("§c您不在队伍中或者不是队伍的队长");
                }
                break;
            }
            case "accept":{
                PartyInvite.accept(ps);
                break;
            }
            case "deny":{
                PartyInvite.deny(ps);
                break;
            }
            case "list":{
                if(PartyUtils.getParty(ps)!=0){
                    sendList(ps,PartyUtils.playersParty.get(ps));
                }
                else
                    sender.sendMessage("§c您不在队伍中");
                break;
            }
            case "warp":{
                if(PartyUtils.getParty(ps)==2){
                    ServerInfo a =PartyUtils.playersParty.get(ps).leader.getServer().getInfo();
                    ps.connect(a);
                    sender.sendMessage("§a成功传送至 §2"+a.getName());
                }
                else
                    sender.sendMessage("§c您不在队伍中或者您就是组长");
                break;
            }
        }
        Message.sendLine(ps);

    }

    private static void invite(ProxiedPlayer ps,String pr1){
        if(pr1.equalsIgnoreCase(ps.getName()))
            ps.sendMessage("§c您不能邀请自己");
        else
        {
            ProxiedPlayer pr = PlayerUtils.searchPlayer(pr1);
            if(pr!=null){

                if(PartyUtils.playersParty.containsKey(ps)) {
                    if(!PartyUtils.playersParty.get(ps).leader.equals(ps))
                        ps.sendMessage("§c您当前组队的队长不是你!");
                }
                else if(PartyUtils.playersParty.containsKey(pr))
                {
                    ps.sendMessage("§c对方已经在一个队伍中了!");
                }
                else {

                    if(PartyInvite.sendInvite(ps, pr))
                        ps.sendMessage("§a组队邀请已发送");
                    else
                        ps.sendMessage("§c组队邀请发送失败，您或对方有已经挂起的邀请");
                }
            }
            else
            {
                ps.sendMessage("§c玩家 §7"+pr1+"§c 不在线");
            }

        }
    }

    public static void leave(ProxiedPlayer ps,int mode)
    {
        //mode=1 离开 mode=2 解散
        if(mode==1){
            Party party = PartyUtils.playersParty.get(ps);
            party.players.remove(ps);
            for(ProxiedPlayer player:party.players){
                player.sendMessage("§7"+ps.getName()+"§6 离开了队伍");
            }
        }
        else {
            Party party = PartyUtils.playersParty.get(ps);
            party.sendBoardMessage("§c队长 §7" + ps.getName() + "§c 解散了队伍.");
            for (ProxiedPlayer player : party.players) {
                party.players.remove(player);
                PartyUtils.playersParty.remove(player);
            }
        }


        for(PartyInvite invite:PartyInvite.invites)
        {
            if(invite.from.equals(ps))
            {
                Message.sendLine(invite.to);
                invite.to.sendMessage("§c邀请人已经离开队伍，邀请自动取消");
                Message.sendLine(invite.to);

                PartyInvite.invites.remove(invite);
            }
        }
    }


    private static void sendHelp(ProxiedPlayer player){
        player.sendMessage("§a组队命令:");
        player.sendMessage("§e/p help §7- §b帮助");
        player.sendMessage("§e/p invite <玩家> §7- §b邀请玩家加入组队");
        player.sendMessage("§e/p remove <玩家> §7- §9[队长]§b删除队伍中的一名玩家");
        player.sendMessage("§e/p setname <名称> §7- §9[队长]§b设定队伍名称");
        player.sendMessage("§e/p setcolor <颜色> §7- §9[队长]§b设定队伍名称颜色");
        player.sendMessage("§e/p list §7- §b查看队伍信息");
        player.sendMessage("§e/p leave §7- §b离开队伍");
        player.sendMessage("§e/p disband §7- §9[队长]§b解散队伍");
        player.sendMessage("§e/p warp §7- §b传送至队长所在服务器");
        player.sendMessage("§e/p accept §7- §b接受收到的组队邀请");
        player.sendMessage("§e/p deny §7- §b拒绝收到的组队邀请");
    }

    private static void sendList(ProxiedPlayer player,Party party){
        player.sendMessage("§a组队(%n%人):§f %name%"
                .replace("%n%",String.valueOf(party.players.size()))
                .replace("%name%",party.name));
        player.sendMessage("§a队长: §7%leader%".replace("%leader%",party.leader.getName()));


        String p = "";
        for(ProxiedPlayer p1:party.players)
        {
            p+="§7"+p1.getName()+"§a,";
        }
        if(p.substring(p.length()-1).equals(","))
            p= p.substring(0,p.length()-1);

        player.sendMessage("§a组员: %players%".replace("%players%",p));
    }


}
