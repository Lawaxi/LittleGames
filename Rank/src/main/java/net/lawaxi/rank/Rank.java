package net.lawaxi.rank;

import com.google.common.base.Charsets;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public final class Rank extends JavaPlugin {

    private static FileConfiguration config;
    public static FileConfiguration ranks;
    private static File rank;

    @Override
    public void onEnable() {
        // Plugin startup logic

        //注册事件
        getServer().getPluginManager().registerEvents(new PlayerJoinEvent(), this);

        //config.yml
        saveDefaultConfig();
        config = getConfig();
        if(!config.contains("path"))
        {
            config.set("path",getDataFolder().getAbsoluteFile()+File.separator+"ranks.yml");
            saveConfig();
        }

        //ranks.yml
        //初始化
        rank = new File(config.get("path").toString());
        saveResource("ranks.yml",false);
        ranks = YamlConfiguration.loadConfiguration(rank);

        //必须包含default
        if(!ranks.contains("types.default.list") || !ranks.contains("types.default.chat"))
        {
            ranks.set("types.default.list","§7[Player]%player%");
            ranks.set("types.default.chat","§7[Player]%player%§f");

            try{
                ranks.save(rank);
            } catch (IOException e) {
                onDisable();
            }
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("setrank")) {
            if (args.length == 2) {
                if (ranks.contains("types."+args[1]))
                {
                    ranks.set("players." + args[0], args[1]);
                    reloadRanks(args[0]);
                }

                else
                    sender.sendMessage("§c不存在该等级");
                return true;
            } else {
                return false;
            }

        }

        return super.onCommand(sender, command, label, args);
    }

    public static void reloadRanks(String player){
        for(Player player1: Bukkit.getOnlinePlayers())
        {
            if(player1.getName().equals(player))
            {
                if(!ranks.contains("players."+player))
                {
                    ranks.set("players." + player,"default");
                }

                player1.setPlayerListName(ranks.get("types."+ranks.get("players." + player).toString()+".list").toString().replace("%player%",player));
                player1.setDisplayName(ranks.get("types."+ranks.get("players." + player).toString()+".chat").toString().replace("%player%",player));

                try {
                    ranks.save(rank);
                }
                catch (IOException e)
                {

                }
            }
        }
    }
}
