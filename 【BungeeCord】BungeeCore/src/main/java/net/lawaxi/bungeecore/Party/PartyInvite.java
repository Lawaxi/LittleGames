package net.lawaxi.bungeecore.Party;

import net.lawaxi.bungeecore.Player.Message;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.ArrayList;

public class PartyInvite {

    public ProxiedPlayer from;
    public ProxiedPlayer to;

    public PartyInvite(ProxiedPlayer from, ProxiedPlayer to) {
        this.from = from;
        this.to = to;


        //60秒自动取消
    }

    public static ArrayList<PartyInvite> invites = new ArrayList<>();

    public static boolean sendInvite(ProxiedPlayer sender, ProxiedPlayer reciever){

        if(searchInvite(reciever)==-1) {
            invites.add(new PartyInvite(sender, reciever));

            Message.sendLine(reciever);
            reciever.sendMessage("§7" + sender.getName() + " §e向您发送了组队邀请");

            TextComponent textComponent = new TextComponent("§6点击这里§e以加入组队!邀请的有效时间为60秒!");
            textComponent.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/p accept"));
            textComponent.setInsertion("相对应输入/p accept");
            reciever.sendMessage(textComponent);
            Message.sendLine(reciever);

            return true;
        }
        return false;
    }

    public static boolean accept(ProxiedPlayer player){

        int index = searchInvite(player);

        if(index==-1)
            player.sendMessage("§c您没有待接受的邀请");
        else
        {
            ProxiedPlayer from = invites.get(index).from;

            if(!PartyUtils.playersParty.containsKey(from))
                PartyUtils.playersParty.put(from,new Party(from,"组队"));

            from.sendMessage("§7"+player.getName()+" §a接受了您的邀请");
            PartyUtils.playersParty.get(from).players.add(player);
            PartyUtils.playersParty.get(from).sendBoardMessage("§7"+player.getName()+" §6加入了组队");
            PartyUtils.playersParty.put(player,PartyUtils.playersParty.get(from));

            invites.remove(index);
            return true;
        }

        return false;
    }

    public static boolean deny(ProxiedPlayer player){

        int index = searchInvite(player);

        if(index==-1)
            player.sendMessage("§c您没有待拒绝的邀请");
        else
        {
            ProxiedPlayer from = invites.get(index).from;
            if(PartyUtils.playersParty.containsKey(from))
            {
                from.sendMessage("§7"+player.getName()+" §a拒绝了您的邀请");
            }
            else {
                player.sendMessage("§c对方已经不在队伍中了");
            }
            invites.remove(index);
            return true;
        }

        return false;
    }

    private static int searchInvite(ProxiedPlayer player){

        for (PartyInvite invite : invites) {
            if (invite.to.equals(player))
                return invites.indexOf(invite);
        }
        return -1;
    }

}
