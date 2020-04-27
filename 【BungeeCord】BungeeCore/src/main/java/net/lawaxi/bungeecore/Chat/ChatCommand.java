package net.lawaxi.bungeecore.Chat;

import net.lawaxi.bungeecore.Party.PartyUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class ChatCommand extends Command {
    public ChatCommand() {
        super("chat",null);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {


        if(args.length>0){
            if(args[0].equalsIgnoreCase("p") || args[0].equalsIgnoreCase("party"))
            {
                if(PartyUtils.getParty((ProxiedPlayer) sender)!=0)
                    ChatUtils.chatModes.replace((ProxiedPlayer)sender,ChatMode.PARTY);
                else
                    sender.sendMessage("§c你不在任何一个队伍中");
                return;
            }


            else if(args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("all")) {
                ChatUtils.chatModes.replace((ProxiedPlayer) sender, ChatMode.PUBLIC);
                return;
            }
        }

        sender.sendMessage("§6使用 /chat <频道> 设置自己的聊天频道");
        sender.sendMessage("§6可用的聊天频道：all, party");
    }
}
