package net.lawaxi.mc.bridgepractice;

import net.lawaxi.mc.bridgepractice.listeners.antimc;
import net.lawaxi.mc.bridgepractice.listeners.others;
import net.lawaxi.mc.bridgepractice.utils.PlayerUtils;
import net.lawaxi.mc.bridgepractice.utils.Villagers;
import net.lawaxi.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class Bridgepractice extends JavaPlugin {

    public static FileConfiguration config;
    public static Location joinLocation;

    public static Bridgepractice instance;
    public static Rank rank;

    @Override
    public void onEnable() {
        // Plugin startup logic


        //读取Rank插件
        if(Bukkit.getPluginManager().getPlugin("Rank") == null)
            Bukkit.getPluginManager().disablePlugin(this);
        else if(! (Bukkit.getPluginManager().getPlugin("Rank") instanceof Rank))
            Bukkit.getPluginManager().disablePlugin(this);
        else
            rank = (Rank) Bukkit.getPluginManager().getPlugin("Rank");

        //事件
        instance = this;
        Bukkit.getPluginManager().registerEvents(new antimc(),this);
        Bukkit.getPluginManager().registerEvents(new others(),this);

        //配置
        saveDefaultConfig();
        config = getConfig();

        if(!config.contains("map.joinlocation.world"))
        {
            World world = getServer().getWorlds().get(0);

            config.set("map.joinlocation.world",world.getName());
            config.set("map.joinlocation.x",world.getSpawnLocation().getX());
            config.set("map.joinlocation.y",world.getSpawnLocation().getY());
            config.set("map.joinlocation.z",world.getSpawnLocation().getZ());
        }
        saveConfig();

        joinLocation = new Location(getServer().getWorld(config.getString("map.joinlocation.world")),
                config.getDouble("map.joinlocation.x"),
                config.getDouble("map.joinlocation.y"),
                config.getDouble("map.joinlocation.z"));

        //记录盔甲架地点
        for(World world:Bukkit.getWorlds()){
            for(Entity e:world.getEntities()){
                if(e instanceof ArmorStand)
                {
                    Villagers.spawnpoint.add(e.getLocation());
                    e.remove();
                }
            }
        }
        Villagers.spawnAllVillage();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getLogger().warning("正在清除方块，请勿强制关闭！");
        for(Player player:Bukkit.getOnlinePlayers()){
            PlayerUtils.clearBlocks(PlayerUtils.playersblock.get(player));
        }

        getLogger().warning("正在恢复盔甲架，请勿强制关闭！");

        for(World world:Bukkit.getWorlds()){
            for(Entity e:world.getEntities()){
                if(e instanceof Villager)
                    e.remove();
            }
        }

        for(Location a:Villagers.spawnpoint){
            a.getWorld().spawnEntity(a, EntityType.ARMOR_STAND);
        }
    }

    public static void addExp(Player player,int exp){
        rank.addExp(player,exp);
    }
}
