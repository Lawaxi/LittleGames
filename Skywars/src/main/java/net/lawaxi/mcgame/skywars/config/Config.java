package net.lawaxi.mcgame.skywars.config;

import net.lawaxi.mcgame.skywars.Skywars;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.LightningStrike;

import java.io.IOException;
import java.util.List;

public class Config {
    public static YamlConfiguration Config = new YamlConfiguration();
    public static YamlConfiguration defaultc = new YamlConfiguration();
    public static void loadConfig(){

        defaultc.set("map.name","§f未知地图");
        defaultc.set("map.mode","§a经典");

        defaultc.set("map.minplayer",2);
        defaultc.set("map.maxplayer",20);

        defaultc.set("map.limit.world",Skywars.server.getWorlds().get(0).getName());
        defaultc.set("map.limit.x1",0);
        defaultc.set("map.limit.y1",0);
        defaultc.set("map.limit.z1",0);
        defaultc.set("map.limit.x2",1);
        defaultc.set("map.limit.y2",1);
        defaultc.set("map.limit.z2",1);

        defaultc.set("lobby.waittime",30);
        defaultc.set("lobby.waittime_full",5);

        defaultc.set("lobby.amount",12);
        defaultc.set("ingame.endtime",20);
        defaultc.set("ingame.autotnt",true);
        defaultc.set("ingame.autorespawn",true);

        //defaultc.set("ingame.itemmin",4);
        //defaultc.set("ingame.itemmax",6);

        if(!list.folder.exists())
            list.folder.mkdir();

        if(list.config.exists())
            Config = YamlConfiguration.loadConfiguration(list.config);

        defaultc.set("lobby.amount",read("lobby.amount"));
        for(int i=1;i<=(int)read("lobby.amount");i++)
        {
            defaultc.set("lobby.location."+String.valueOf(i)+".world", Skywars.server.getWorlds().get(0).getName());
            defaultc.set("lobby.location."+String.valueOf(i)+".x",0.0);
            defaultc.set("lobby.location."+String.valueOf(i)+".y",0.0);
            defaultc.set("lobby.location."+String.valueOf(i)+".z",0.0);
        }

        if(!list.config.exists())
        {
            try {
                defaultc.save(list.config);
            }
            catch (IOException e)
            {
                Skywars.logger.warning("创建默认 config.yml 配置失败！");
            }
        }

    }

    public static List<?> readList(String key)
    {
        if(Config.contains(key))
            return Config.getList(key);
        else
            return null;
    }

    public static Object read(String key)
    {
        if(Config.contains(key))
            return Config.get(key);
        else
        {
            Config.set(key,defaultc.get(key));
            try {
                Config.save(list.config);
            }
            catch (IOException e)
            {
                Skywars.logger.warning("补充 config.yml 中的 "+key+" 配置失败！");
            }
            return defaultc.get(key);
        }
    }


    public static int readMapLimit(String key)
    {
        if(key.equals("x1"))
        {
            return Math.min((int)read("map.limit.x1"),(int)read("map.limit.x2"));
        }
        else if(key.equals("x2"))
        {
            return Math.max((int)read("map.limit.x1"),(int)read("map.limit.x2"));
        }
        else if(key.equals("y1"))
        {
            return Math.min((int)read("map.limit.y1"),(int)read("map.limit.y2"));
        }
        else if(key.equals("y2"))
        {
            return Math.max((int)read("map.limit.y1"),(int)read("map.limit.y2"));
        }
        else if(key.equals("z1"))
        {
            return Math.min((int)read("map.limit.z1"),(int)read("map.limit.z2"));
        }
        else
        {
            return Math.max((int)read("map.limit.z1"),(int)read("map.limit.z2"));
        }
    }

    public static boolean write(String key,Object object)
    {
        Config.set(key,object);

        try {
            Config.save(list.config);
            return true;
        }
        catch (IOException e)
        {
            return false;
        }
    }
}
