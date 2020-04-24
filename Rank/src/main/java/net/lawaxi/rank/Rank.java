package net.lawaxi.rank;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Logger;

public final class Rank extends JavaPlugin {

    public static FileConfiguration config;
    public static FileConfiguration ranks;
    public static FileConfiguration levels;
    private static File rank;
    private static File level;
    private static HashMap<Player,String> playerranks = new HashMap<>();

    public static Logger logger;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger = getLogger();

        //注册事件
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerChat(), this);

        //1.config.yml
        saveDefaultConfig();
        config = getConfig();
        if(!config.contains("path.rank") || !config.contains("path.level"))
        {
            config.set("path.rank",getDataFolder().getAbsoluteFile()+File.separator+"ranks.yml");
            config.set("path.level",getDataFolder().getAbsoluteFile()+File.separator+"levels.yml");
            saveConfig();
        }


        //2.ranks.yml
        //初始化
        rank = new File(config.get("path.rank").toString());
        if(!rank.exists())
        {
            saveResource("ranks.yml",false);

            //自定义路径
            File file =  new File(getDataFolder(),"ranks.yml");
            if(!rank.getAbsoluteFile().equals(file.getAbsoluteFile()))
            {
                FileUtil.copy(file,rank);
                file.delete();
            }

        }
        ranks = YamlConfiguration.loadConfiguration(rank);

        //必须包含default
        if(!ranks.contains("types.default.name") || !ranks.contains("types.default.list") || !ranks.contains("types.default.chat"))
        {
            ranks.set("types.default.level",0);
            ranks.set("types.default.name","§7默认");
            ranks.set("types.default.list","§7%player%");
            ranks.set("types.default.chat","§7%player%");

            saveRanks();
        }

        //shoutlevel
        if(!ranks.contains("types."+config.getString("chat.shoutlevel")+".level"))
            config.set("chat.shoutlevel","default");

        //3.levels.yml
        //初始化
        level = new File(config.get("path.level").toString());

        if(!level.exists())
        {
            saveResource("levels.yml",false);

            File file =  new File(getDataFolder(),"levels.yml");
            if(!level.getAbsoluteFile().equals(file.getAbsoluteFile()))
            {
                FileUtil.copy(file,level);
                file.delete();
            }

        }
        levels = YamlConfiguration.loadConfiguration(level);

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
                    saveRanks();
                    reloadPlayerRanks(args[0]);
                }

                else
                    sender.sendMessage("§c不存在该等级");
                return true;
            } else {
                return false;
            }

        }
        else if(command.getName().equals("addexp")){
            if(args.length==2){

                for(Player player: Bukkit.getOnlinePlayers())
                {
                    if(player.getName().equals(args[0]))
                    {
                        utils.addExp(player,Integer.valueOf(args[1]));
                        return true;
                    }
                }
                sender.sendMessage("§c玩家 "+args[0]+" 不在线!");
                return true;
            }
            else
            {
                return false;
            }
        }
        else if(command.getName().equals("level")){
            String player;
            if(args.length>0)
                player = args[0];
            else
                player = sender.getName();

            if(levels.contains("players."+player))
            {
                int exp = levels.getInt("players."+player);
                sender.sendMessage(config.getString("messages.inquire")
                        .replace("%1%",player)
                        .replace("%2%",String.valueOf(exp/Rank.levels.getInt("types.exp.each")))
                        .replace("%3%",String.valueOf(exp)));
            }
            else
            {
                sender.sendMessage(config.getString("messages.inquirefailed").replace("%1%",player));
            }
        }

        return super.onCommand(sender, command, label, args);
    }

    public static void reloadPlayerRanks(String player){
        for(Player player1: Bukkit.getOnlinePlayers())
        {
            if(player1.getName().equals(player))
            {
                ranks = YamlConfiguration.loadConfiguration(rank);
                if(!ranks.contains("players."+player))
                {
                    ranks.set("players." + player,"default");
                    saveRanks();
                }

                for(;;) {
                    if (playerranks.containsKey(player1)) {
                        if (!playerranks.get(player1).equals(ranks.get("players." + player).toString()))
                            playerranks.replace(player1, ranks.get("players." + player).toString());
                        else
                            break;
                    } else {
                        playerranks.put(player1, ranks.get("players." + player).toString());
                    }

                    player1.setPlayerListName(ranks.get("types."+ranks.get("players." + player).toString()+".list").toString().replace("%player%",player));
                    player1.setDisplayName(ranks.get("types."+ranks.get("players." + player).toString()+".chat").toString().replace("%player%",player));
                    break;
                }

                for(;;){

                    boolean can =
                            ranks.getInt("types."+ranks.get("players."+player)+".level")
                            >=
                            ranks.getInt("types."+config.getString("chat.shoutlevel")+".level");

                    if(PlayerChat.canshout.containsKey(player1))
                    {
                        if(PlayerChat.canshout.get(player1)!=can)
                            PlayerChat.canshout.replace(player1,can);
                    }
                    else
                    {
                        PlayerChat.canshout.put(player1,can);
                    }
                    return;
                }
            }
        }
    }

    public static void saveRanks(){

        try {
            ranks.save(rank);
        }
        catch (IOException e)
        {

        }
    }

    public static void reloadPlayerLevels(String player){
        for(Player player1: Bukkit.getOnlinePlayers())
        {
            if(player1.getName().equals(player))
            {
                levels = YamlConfiguration.loadConfiguration(level);
                reloadPlayerRanks(player);

                if(!levels.contains("players."+player)) {
                    levels.set("players." + player, levels.get("types.exp.default"));
                    saveLevels();
                }

                //Chat
                int level = utils.getLevel(player1);

                    String display = "";
                    for(int i=level;i>=0;i--)
                    {
                        if(levels.contains("types.display."+i))
                        {
                            display = levels.getString("types.display."+i).replace("%n%",String.valueOf(level));
                            break;
                        }
                    }
                    if(PlayerChat.level.containsKey(player1))
                    {
                        if(PlayerChat.level.get(player1).equals(display))
                            return;
                        else
                            PlayerChat.level.replace(player1,display);
                    }
                    else
                        PlayerChat.level.put(player1,display);

            }
        }
    }

    public static void saveLevels(){

        try {
            levels.save(level);
        }
        catch (IOException e)
        {

        }
    }
}
