package net.lawaxi.bungeecore.Chat;

import net.lawaxi.bungeecore.Friend.FriendUtils;
import net.lawaxi.bungeecore.Player.PlayerUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class MsgCommand extends Command {
    public MsgCommand() {
        super("msg",null,"tell","m");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length==0)
        {
            sender.sendMessage("§c请输入要私聊的玩家");
            return;
        }

        if(sender instanceof ProxiedPlayer){
            if(!FriendUtils.isFriend(sender.getName(),args[0]))
            {
                sender.sendMessage("§c玩家 §7" + args[0] + "§c 不是你的好友");
                return;
            }
        }

        if(args.length==1)
        {
            sender.sendMessage("§c请输入要私聊的内容");
            return;
        }

        ProxiedPlayer player = PlayerUtils.searchPlayer(args[0]);
        if(player==null)
        {
            sender.sendMessage("§c玩家 §7" + args[0] + "§c 不在线");
            return;
        }

        ChatUtils.sendPrivateMessage(player,sender,args[1]);
    }
}
