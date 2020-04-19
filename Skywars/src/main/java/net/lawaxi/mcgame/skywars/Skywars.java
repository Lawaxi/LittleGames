package net.lawaxi.mcgame.skywars;

import net.lawaxi.mcgame.skywars.config.Config;
import net.lawaxi.mcgame.skywars.config.Messages;
import net.lawaxi.mcgame.skywars.config.list;
import net.lawaxi.mcgame.skywars.init.Game;
import net.lawaxi.mcgame.skywars.init.Player;
import net.lawaxi.mcgame.skywars.listeners.*;
import net.lawaxi.mcgame.skywars.utils.GameState;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public final class Skywars extends JavaPlugin {

    public static Skywars instance;
    public static GameState state;
    public static World world;
    public static Server server;

    public static Logger logger;

    public static Map<Location,Material> preblocks1 = new HashMap<>();
    public static Map<Location,BlockData> preblocks2 = new HashMap<>();

    public static ArrayList<ArrayList<ItemStack>> chests ;
    @Override
    public void onEnable() {

        instance = this;
        logger=getLogger();

        //注册事件
        getServer().getPluginManager().registerEvents(new PlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new PlayerJoin(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuit(), this);
        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new BlockBreak(), this);

        //配置生成
        server=getServer();
        list.folder=getDataFolder();
        list.config = new File(getDataFolder(),"config.yml");
        list.messages = new File(getDataFolder(),"messages.yml");

        Messages.loadConfig();
        Config.loadConfig();

        //配置读取到内存

        world=getServer().getWorld(Config.read("map.limit.world").toString());

        //1.箱子预设读取
        chests = (ArrayList<ArrayList<ItemStack>>) Config.read("ingame.chests");

        //2.玩家出生点读取
        for(int i=1;i<=(int)Config.read("lobby.amount");i++)
        {

            Player.spawnlocation.add(new Location(
                    getServer().getWorld(Config.read("lobby.location."+String.valueOf(i)+".world").toString()),
                    (double)Config.read("lobby.location."+String.valueOf(i)+".x"),
                    (double)Config.read("lobby.location."+String.valueOf(i)+".y"),
                    (double)Config.read("lobby.location."+String.valueOf(i)+".z")));
        }

        //3.记录原始地图
        for(int x=Config.readMapLimit("x1");x<=Config.readMapLimit("x2");x++)
        {
            for(int y=Config.readMapLimit("y1");y<=Config.readMapLimit("y2");y++)
            {
                for(int z=Config.readMapLimit("z1");z<=Config.readMapLimit("z2");z++)
                {
                    preblocks1.put(new Location(world,x,y,z),world.getBlockAt(x,y,z).getType());
                    preblocks2.put(new Location(world,x,y,z),world.getBlockAt(x,y,z).getBlockData());
                }
            }
        }

        Game.initializeGame();

    }

    //替换地图为配置中的地图
    public static void reloadMap(){

        logger.warning("正在还原地图，这可能需要花费一些时间，请勿操作！");

        //方块还原
        for(int x=Config.readMapLimit("x1");x<=Config.readMapLimit("x2");x++)
        {
            for(int y=Config.readMapLimit("y1");y<=Config.readMapLimit("y2");y++)
            {
                for(int z=Config.readMapLimit("z1");z<=Config.readMapLimit("z2");z++)
                {
                    Skywars.world.getBlockAt(x,y,z).setType(preblocks1.get(new Location(Skywars.world,x,y,z)));
                    Skywars.world.getBlockAt(x,y,z).setBlockData(preblocks2.get(new Location(Skywars.world,x,y,z)));
                }
            }
        }

        //掉落物清理
        for(Entity entity :world.getEntities())
        {
            if(entity instanceof Item)
                entity.remove();
        }

    }

    @Override
    public void onDisable() {
        for(org.bukkit.entity.Player player : Bukkit.getOnlinePlayers())
            player.kickPlayer(Messages.read("server.stop").toString());
        reloadMap();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(command.getName().equalsIgnoreCase("skywars")) {
            if (args.length == 0) {
                AdminHelp(sender,false);
            } else {
                switch (args[0]) {
                    case "setlimit":{

                        if(args.length==1)
                            AdminHelp(sender,false);
                        else
                        {
                            if(sender instanceof org.bukkit.entity.Player)
                            {
                                //边界取两个对角线，index只取1,2
                                int index = (Integer.valueOf(args[1])+1)%2+1;

                                if(!Config.read("map.limit.world").toString().equals(((org.bukkit.entity.Player)sender).getWorld().getName()))
                                    sender.sendMessage("§c注意：不支持跨世界地图");

                                if(Config.write("map.limit.world", ((org.bukkit.entity.Player)sender).getWorld().getName())&&
                                        Config.write("map.limit.x"+String.valueOf(index), (int)((org.bukkit.entity.Player)sender).getLocation().getX())&&
                                        Config.write("map.limit.y"+String.valueOf(index), (int)((org.bukkit.entity.Player)sender).getLocation().getY())&&
                                        Config.write("map.limit.z"+String.valueOf(index), (int)((org.bukkit.entity.Player)sender).getLocation().getZ()) ){
                                    sender.sendMessage("§a地图边界点 §f"+index+"§a 设置成功！");
                                }
                                else{
                                    sender.sendMessage("§c地图边界点 §f"+index+"§c 设置失败！");
                                }
                            }
                            else
                            {
                                AdminHelp(sender,true);
                            }

                        }
                        break;
                    }
                    case "setlobby":
                    {
                        if(args.length==1)
                            AdminHelp(sender,false);
                        else
                        {
                            if(sender instanceof org.bukkit.entity.Player)
                            {
                                int index = Integer.valueOf(args[1]);
                                if(index>(int)Config.read("lobby.amount"))
                                    sender.sendMessage("§c注意：您当前正在设置的出生点序号为 §f"+index+"§c 大于设置的出生点数 §f"+(int)Config.read("lobby.amount")+"§c ！");

                                if(Config.write("lobby.location."+String.valueOf(index)+".world", ((org.bukkit.entity.Player)sender).getWorld().getName())&&
                                        Config.write("lobby.location."+String.valueOf(index)+".x", ((org.bukkit.entity.Player)sender).getLocation().getX())&&
                                        Config.write("lobby.location."+String.valueOf(index)+".y", ((org.bukkit.entity.Player)sender).getLocation().getY())&&
                                        Config.write("lobby.location."+String.valueOf(index)+".z", ((org.bukkit.entity.Player)sender).getLocation().getZ()))
                                {
                                    sender.sendMessage("§a出生点 §f"+index+"§a 设置成功！");
                                }
                                else
                                {
                                    sender.sendMessage("§c地图边界点 §f"+index+"§c 设置失败！");
                                }
                            }
                            else
                            {
                                AdminHelp(sender,true);
                            }

                        }
                        break;
                    }
                    case "setlobbyamount":{
                        if(args.length==1)
                            AdminHelp(sender,false);
                        else
                        {
                            int amount = Integer.valueOf(args[1]);
                            if(Config.write("lobby.amount",amount))
                            {
                                sender.sendMessage("§a成功设置出生点数量为 §f"+amount);
                            }
                            else
                            {
                                sender.sendMessage("§c出生点数量设置失败");
                            }

                        }
                        break;
                    }
                    /*
                        原本计划的是按单个物品随机，后改为按箱子随机

                    case "additem":{
                        if(sender instanceof org.bukkit.entity.Player)
                        {
                            ArrayList<ItemStack> items;
                            if(!Config.Config.contains("ingame.items"))
                                items = new ArrayList<>();
                            else
                                items = (ArrayList<ItemStack>)Config.read("ingame.items");

                            items.add(((org.bukkit.entity.Player)sender).getItemInHand());
                            if(Config.write("ingame.items",items))
                            {
                                sender.sendMessage("§a箱子物品生成表添加成功");
                            }
                            else
                            {
                                sender.sendMessage("§c箱子物品生成表添加失败");
                            }
                        }
                        else
                        {
                            AdminHelp(sender,true);
                        }
                        break;

                    }*/
                    case "addchest":{
                        if(sender instanceof org.bukkit.entity.Player) {
                            Location me = ((org.bukkit.entity.Player)sender).getLocation();
                            for(int x=me.getBlockX()-1;x<=me.getBlockX()+1;x++)
                            {
                                for(int y=me.getBlockY()-1;y<=me.getBlockY()+1;y++)
                                {
                                    for(int z=me.getBlockZ()-1;z<=me.getBlockZ()+1;z++)
                                    {
                                        Block block =me.getWorld().getBlockAt(x,y,z) ;
                                        if(block.getType().equals(Material.CHEST))
                                        {
                                            //有一个很奇怪的点 YamlConfig 写的时候 ArrayList 与数组是不区分的
                                            //但是读的时候会统一读成 ArrayList ，如果直接读数组会报错

                                            ArrayList<ItemStack[]> chests;
                                            if(!Config.Config.contains("ingame.chests"))
                                                chests = new ArrayList<>();
                                            else
                                                chests= (ArrayList<ItemStack[]>)Config.read("ingame.chests");

                                            chests.add(((Chest)block.getState()).getBlockInventory().getContents());


                                            if(Config.write("ingame.chests",chests))
                                            {
                                                sender.sendMessage("§a成功添加新箱子 §b(§f"+x+"§b,§f"+y+"§b,§f"+z+"§b) §a到箱子列表");
                                            }
                                            else
                                            {
                                                sender.sendMessage("§c新箱子添加失败");
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        else
                        {
                            AdminHelp(sender,true);
                        }
                        break;
                    }
                    case "setname":
                    {
                        if(args.length==1)
                            AdminHelp(sender,false);
                        else
                        {
                            if(Config.write("map.name",args[1]))
                                sender.sendMessage("§a地图名设置为 §f"+args[1]);
                            else
                                sender.sendMessage("§c地图名设置失败");
                        }
                        break;
                    }
                    default:{
                        AdminHelp(sender,false);
                        break;
                    }
                }
            }

            return true;
        }
        return super.onCommand(sender, command, label, args);
    }

    private static void AdminHelp(CommandSender player,boolean mode)
    {
        //两种模式，索性布尔值
        if(mode)
        {
            player.sendMessage("该操作只能玩家完成");
        }
        else
        {
            player.sendMessage("/skywars setname xxx 设置地图名称");
            player.sendMessage("/skywars setlimit 1(2) 设置地图边界");
            player.sendMessage("/skywars setlobbyamount 12 设置出生点数");
            player.sendMessage("/skywars setlobby 1(2,3,4,...) 设置出生点");
            //player.sendMessage("/skywars additem 将手中物品添加进箱子物品生成表");
            player.sendMessage("/skywars addchest 将附近的箱子添加进箱子预设表");
        }

    }
}
