package net.lawaxi.rank;

import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.util.FileUtil;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public final class Rank extends JavaPlugin {

    public static FileConfiguration config;
    public static FileConfiguration ranks;
    public static FileConfiguration levels;
    private static File rank;
    private static File level;
    public static HashMap<Player,String> playerranks = new HashMap<>();

    public static Logger logger;
    public static Server server;

    public static Rank instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        logger = getLogger();
        server = getServer();
        instance = this;

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
                Player player = server.getPlayer(args[0]);
                if(player!=null) {
                    addExp(player, Integer.valueOf(args[1]));
                }
                else {
                    sender.sendMessage("§c玩家 " + args[0] + " 不在线!");
                }
                return true;
            }
            return false;
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
            return true;
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
                int level = getLevel(player1);

                String display = getLevelDisplay(level);
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

    public static void addExp(Player player, int amount){

        int level = Rank.levels.getInt("players."+player.getName())/Rank.levels.getInt("types.exp.each");
        if(level<Rank.levels.getInt("types.exp.max"))
        {
            if(amount>Rank.levels.getInt("types.exp.max")-level)
            {
                amount=Rank.levels.getInt("types.exp.max")-level;
            }
            int exp = Rank.levels.getInt("players."+player.getName())+amount;
            Rank.levels.set("players."+player.getName(),exp);
            Rank.saveLevels();

            int level2 = exp/Rank.levels.getInt("types.exp.each");
            if(level2 != level) {
                Rank.reloadPlayerLevels(player.getName());

                player.sendMessage(replace(replace(replace((List<String>) Rank.config.getList("messages.addexp2")
                        ,"%1%",String.valueOf(amount))
                        ,"%2%",String.valueOf(level2))
                        ,"%3%",String.valueOf((level2+1)*Rank.levels.getInt("types.exp.each")-exp))
                        .toArray(new String[0]));
            }
            else
            {
                player.sendMessage(replace(replace(replace((List<String>) Rank.config.getList("messages.addexp1")
                        ,"%1%",String.valueOf(amount))
                        ,"%2%",String.valueOf(level+1))
                        ,"%3%",String.valueOf((level+1)*Rank.levels.getInt("types.exp.each")-exp))
                        .toArray(new String[0]));
            }
        }
        else
        {
            player.sendMessage(replace((List<String>) Rank.config.getList("messages.addexp3"),"%1%",String.valueOf(amount))
                    .toArray(new String[0]));
        }

        if(config.getBoolean("allow-scoreboard"))
            reloadScoreboard(player);
    }

    private static List<String> replace(List<String> a ,String target,String replacement){
        List<String> b = new ArrayList<>();
        for(String member : a)
        {
            b.add(member.replace(target,replacement));
        }
        return b;
    }

    public static int getExp(Player player){
        return Rank.levels.getInt("players."+player.getName());
    }

    public static int getLevel(Player player){
        return getExp(player)/Rank.levels.getInt("types.exp.each");
    }

    public static String getLevelDisplay(int level){
        String display = "";
        for(int i=level;i>=0;i--)
        {
            if(Rank.levels.contains("types.display."+i))
            {
                display = Rank.levels.getString("types.display."+i).replace("%n%",String.valueOf(level));
                break;
            }
        }
        return display;
    }

    public static void reloadScoreboard(Player player){
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        Scoreboard scoreboard = manager.getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("sb", "dummy");
        objective.setDisplayName(Rank.config.getString("scoreboard.title"));
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        ArrayList<String> a = (ArrayList<String> ) Rank.config.getList("scoreboard.list");
        for(String b : a)
        {
            objective.getScore(b
                    .replace("%rank%",Rank.ranks.getString("types."+Rank.playerranks.get(player)+".name"))
                    .replace("%level%",String.valueOf(getLevel(player)))
            ).setScore(a.size()-a.indexOf(b));
        }


        /*
        Objective o = scoreboard.registerNewObjective("ls", "dummy");
        o.setDisplaySlot(DisplaySlot.BELOW_NAME);
        o.getScore(player.getName()).setScore(getLevel(player));*/
        player.setScoreboard(scoreboard);
    }
}
