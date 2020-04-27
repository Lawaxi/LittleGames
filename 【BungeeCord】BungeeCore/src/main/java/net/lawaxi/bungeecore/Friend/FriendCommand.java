package net.lawaxi.bungeecore.Friend;

import net.lawaxi.bungeecore.Bungeecore;
import net.lawaxi.bungeecore.Player.Message;
import net.lawaxi.bungeecore.Player.PlayerUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

public class FriendCommand extends Command {

    private static Bungeecore instance;

    public FriendCommand(Bungeecore instance) {

        super("friend", null, "f", "好友");
        this.instance = instance;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage("§c只能玩家使用");
            return;
        }

        if (args.length == 0) {
            Message.sendLine2((ProxiedPlayer) sender);
            sendHelp((ProxiedPlayer) sender);
            Message.sendLine2((ProxiedPlayer) sender);
            return;
        }


        ProxiedPlayer ps = (ProxiedPlayer) sender;
        Message.sendLine2(ps);
        switch (args[0]) {
            default: {
                request(ps, args[0]);
                break;
            }
            case "help":{
                sendHelp(ps);
                break;
            }
            case "request":{
                if(args.length==1)
                    ps.sendMessage("§c请输入要请求成为好友的玩家");
                else
                    request(ps, args[1]);
                break;
            }
            case "accept":{
                if(args.length==1)
                    ps.sendMessage("§c请输入要接受传送请求的发起玩家");
                else{
                    ProxiedPlayer player = PlayerUtils.searchPlayer(args[1]);
                    if(player==null)
                        ps.sendMessage("§c玩家 §7"+args[1]+"§c 不在线");
                    else
                        FriendRequest.accept(player,ps);
                }

                break;
            }
            case "deny":{
                if(args.length==1)
                    ps.sendMessage("§c请输入要拒绝传送请求的发起玩家");
                else{
                    ProxiedPlayer player = PlayerUtils.searchPlayer(args[1]);
                    if(player==null)
                        ps.sendMessage("§c玩家 §7"+args[1]+"§c 不在线");
                    else
                        FriendRequest.deny(player,ps);
                }

                break;
            }
            case "list":{
                String  list ="";
                for(String friend:FriendConfig.getPlayerFriends(ps.getName()))
                {
                    list+="§7"+friend+"§a,";
                }

                if(list.equals(""))
                    ps.sendMessage("§6您没有好友，赶快添加一个吧!");
                else {
                    ps.sendMessage("§a好友列表:");
                    ps.sendMessage(list.substring(0,list.length()-3));
                }
                break;

            }
            case "remove":{
                if(args.length==1)
                    ps.sendMessage("§c请输入要删除的好友");
                else{
                    if(FriendConfig.getPlayerFriends(ps.getName()).contains(args[1]))
                    {
                        FriendConfig.removeFriend(ps,args[1]);

                        ProxiedPlayer player = PlayerUtils.searchPlayer(args[1]);
                        if(player==null)//在线 or 不在线
                        {
                            if(Bungeecore.friendremoveInfos.containsKey(args[1]))
                            {
                                Bungeecore.friendremoveInfos.get(args[1]).add(ps.getName());
                            }
                            else{
                                ArrayList<String> a = new ArrayList<>();
                                a.add(ps.getName());
                                Bungeecore.friendremoveInfos.put(args[1],( ArrayList<String>)a.clone());
                            }

                        }
                        else
                        {
                            Message.sendLine2(player);
                            player.sendMessage("§7"+ps.getName()+" §6删除了您的好友");
                            Message.sendLine2(player);
                        }

                        ps.sendMessage("§6已经将 §7"+args[1]+"§6 从您的好友列表中删除");
                    }
                    else
                        ps.sendMessage("§c玩家 §7"+args[1]+"§c 不是您的好友");

                }

                break;
            }
        }
        Message.sendLine2(ps);
    }

    private static void sendHelp(ProxiedPlayer player){
        player.sendMessage("§a好友命令:");
        player.sendMessage("§e/f help §7- §b帮助");
        player.sendMessage("§e/f list <玩家> §7- §b您的好友列表");
        player.sendMessage("§e/f request <玩家> §7- §b申请玩家成为好友");
        player.sendMessage("§e/f accept <玩家> §7- §b接受一位玩家的申请");
        player.sendMessage("§e/f deny <玩家> §7- §b拒绝一位玩家的申请");
        player.sendMessage("§e/f remove <玩家> §7- §b将一位玩家从好友列表中删除");
    }

    private static void request(ProxiedPlayer from,String to){

        if(from.getName().equalsIgnoreCase(to)) {
            from.sendMessage("§c您不能添加自己为好友");
            return;
        }

        ProxiedPlayer player = PlayerUtils.searchPlayer(to);
        if(player==null)
        {
            from.sendMessage("§c玩家 §7"+to+" §c不在线");
            return;
        }

        //先查找对方有没有向你发过


        if(FriendRequest.searchRequest(player,from)!=-1)
        {
            from.sendMessage("§a对方向您发送过好友请求，您无需再次发送");
            from.sendMessage("§a输入 §2/f accept "+to+" §a即可成为对方的好友");
        }

        if(FriendRequest.sendRequest(from,player))
        {
            from.sendMessage("§a好友申请已发送");
        }
        else
        {
            from.sendMessage("§c您已经向他发送过一个好友请求了");
        }

    }
}
