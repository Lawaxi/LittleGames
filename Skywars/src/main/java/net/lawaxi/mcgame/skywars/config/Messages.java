package net.lawaxi.mcgame.skywars.config;

import net.lawaxi.mcgame.skywars.Skywars;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.IOException;

public class Messages {
    public static YamlConfiguration Config;
    public static YamlConfiguration chinese = new YamlConfiguration();

    public static void loadConfig(){

        chinese.set("scoreboard.title","§b§l空岛战争");
        chinese.set("scoreboard.lobby.player","§e人数：§a%now%§f/§a%max%");
        chinese.set("scoreboard.lobby.time","§e游戏将在 §a%sec% §e秒后开始");
        chinese.set("scoreboard.lobby.time1","§e等待其他玩家加入...");
        chinese.set("scoreboard.lobby.map","§e地图：§a%map%");
        chinese.set("scoreboard.lobby.mode","§e模式：§a%mode%");
        chinese.set("scoreboard.lobby.info","§amc.lawaxi.net");

        chinese.set("scoreboard.ingame.player","§e剩余玩家：§a%now%");
        chinese.set("scoreboard.ingame.kills","§e击杀：§c%kills%");
        chinese.set("scoreboard.ingame.map","§e地图：%map%");
        chinese.set("scoreboard.ingame.mode","§e模式：%mode%");
        chinese.set("scoreboard.ingame.info","§amc.lawaxi.net");

        chinese.set("lobby.wait.msg","§e游戏将在 §b%time% §e秒后开始");
        chinese.set("lobby.wait.msg5sec","§e游戏将在 §c%time% §e秒后开始");
        chinese.set("lobby.wait.addition1","§c人数不足最少人数，计时取消！");
        chinese.set("lobby.wait.addition2","§6人数已满，等待时间缩短！");
        chinese.set("lobby.wait.title","§c%time%");
        chinese.set("lobby.wait.sub","§e请做好准备！");

        chinese.set("ingame.join.full.title","§7游戏人数已满");
        chinese.set("ingame.join.full.sub","§f您将成为旁观者观看游戏");
        chinese.set("ingame.join.already.title","§7游戏已经开始");
        chinese.set("ingame.join.already.sub","§f您将成为旁观者观看游戏");

        chinese.set("ingame.end","§6游戏结束！%winner%");
        chinese.set("ingame.winner","§6获胜者是 §e%player%");
        chinese.set("ingame.winner1","§6没有获胜者");
        chinese.set("ingame.reset","§c将在 §4%time% §c秒后重置");
        chinese.set("ingame.dead.title","§c你死了！");
        chinese.set("ingame.dead.sub","§7您将成为旁观者继续观看游戏");
        chinese.set("ingame.end1.title","§c游戏结束");
        chinese.set("ingame.end1.sub","§c您不是胜利者");
        chinese.set("ingame.end2.title","§a恭喜获胜");
        chinese.set("ingame.end2.sub","§2您赢得了比赛");

        chinese.set("server.stop","§4服务器已关闭");
        chinese.set("server.reload","§6游戏已结束，地图正在重置");

        if(!list.folder.exists())
            list.folder.mkdir();

        if(!list.messages.exists())
        {
            try {
                chinese.save(list.messages);
            }
            catch (IOException e)
            {
                Skywars.logger.warning("创建默认 messages.yml 配置失败！");
            }
        }


        Config = YamlConfiguration.loadConfiguration(list.messages);
    }

    public static Object read(String key)
    {
        if(Config.contains(key))
            return Config.get(key);
        else
        {
            Config.set(key,chinese.get(key));
            try {
                Config.save(list.messages);
            }
            catch (IOException e)
            {
                Skywars.logger.warning("补充 messages.yml 中的 "+key+" 配置失败！");
            }
            return chinese.get(key);
        }
    }
}
