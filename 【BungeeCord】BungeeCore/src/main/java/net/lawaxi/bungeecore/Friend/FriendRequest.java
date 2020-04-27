package net.lawaxi.bungeecore.Friend;

import net.lawaxi.bungeecore.Player.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FriendRequest {

    public ProxiedPlayer from;
    public ProxiedPlayer to;

    public FriendRequest(ProxiedPlayer from, ProxiedPlayer to) {
        this.from = from;
        this.to = to;


        FriendRequest t = this;
        new Timer().schedule(
                new TimerTask(){
                    @Override
                    public void run() {
                        if(friendRequests.contains(t))
                        {
                            Message.sendLine25(t.from);
                            t.from.sendMessage("§e60秒时间已到，发往 §7"+t.to+"§e 的好友请求已自动取消");
                            Message.sendLine25(t.from);

                            Message.sendLine25(t.to);
                            t.to.sendMessage("§e60秒时间已到，来自 §7"+t.from+"§e 的好友请求已自动取消");
                            Message.sendLine25(t.to);

                            friendRequests.remove(t);
                        }
                    }
                }
                ,60000);
    }

    public static ArrayList<FriendRequest> friendRequests = new ArrayList<>();


    public static boolean sendRequest(ProxiedPlayer sender, ProxiedPlayer reciever){

        if(searchRequest(sender,reciever)==-1) {
            friendRequests.add(new FriendRequest(sender,reciever));

            Message.sendLine25(reciever);
            reciever.sendMessage("§e好友请求: §f来自" + sender.getName());

            TextComponent a = new TextComponent("§a[接受]");
            a.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f accept "+sender.getName()));
            a.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§2相当于输入§a/f accept "+sender.getName() ).create() ));

            TextComponent b = new TextComponent("§c[拒绝]");
            b.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/f deny "+sender.getName()));
            b.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§4相当于输入§c/f deny "+sender.getName() ).create() ));


            reciever.sendMessage(a,new TextComponent("§8 - "),b);
            reciever.sendMessage("§e您有60秒的时间接受或拒绝这个请求" + sender.getName());
            Message.sendLine25(reciever);

            return true;
        }
        return false;
    }

    public static int searchRequest(ProxiedPlayer from, ProxiedPlayer to){

        for (FriendRequest request:friendRequests) {
            if (request.to.equals(to) &&request.from.equals(from))
                return friendRequests.indexOf(request);
        }
        return -1;
    }

    public static boolean accept(ProxiedPlayer from,ProxiedPlayer to){

        int index = searchRequest(from,to);

        if(index==-1)
        {
            to.sendMessage("§c对方没有给您发送过好友请求");
            return false;
        }
        else
        {

            FriendConfig.addFriend(from,to);

            Message.sendLine2(from);
            from.sendMessage("§7"+to.getName()+" §a现在是您的好友了");
            Message.sendLine2(from);

            //由于输入指令时有自动补充 sendLine2 此处无需添加
            to.sendMessage("§a您现在是 §7"+from.getName()+" §a的好友了");

            friendRequests.remove(index);
            return true;
        }

    }

    public static boolean deny(ProxiedPlayer from,ProxiedPlayer to){

        int index = searchRequest(from,to);

        if(index==-1)
        {
            to.sendMessage("§c对方没有给您发送过好友请求");
            return false;
        }
        else
        {

            Message.sendLine2(from);
            from.sendMessage("§7"+to.getName()+" §c拒绝了您的好友请求");
            Message.sendLine2(from);

            //由于输入指令时有自动补充 sendLine2 此处无需添加
            to.sendMessage("§c您拒绝了来自 §7"+from.getName()+" §c的好友请求");

            friendRequests.remove(index);
            return true;
        }

    }
}
