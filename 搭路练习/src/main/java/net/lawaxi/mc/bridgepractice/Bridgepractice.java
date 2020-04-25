package net.lawaxi.mc.bridgepractice;

import net.lawaxi.mc.bridgepractice.listeners.antimc;
import net.lawaxi.mc.bridgepractice.listeners.others;
import net.lawaxi.mc.bridgepractice.utils.PlayerUtils;
import net.lawaxi.rank.utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Bridgepractice extends JavaPlugin {

    public static FileConfiguration config;
    public static Location joinLocation;

    public static Bridgepractice instance;


    @Override
    public void onEnable() {
        // Plugin startup logic

        if(Bukkit.getPluginManager().getPlugin("Rank") == null)
            Bukkit.getPluginManager().disablePlugin(this);

        instance = this;
        Bukkit.getPluginManager().registerEvents(new antimc(),this);
        Bukkit.getPluginManager().registerEvents(new others(),this);

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
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        getLogger().warning("正在清除方块，请勿强制关闭！");
        for(Player player:Bukkit.getOnlinePlayers()){
            PlayerUtils.clearBlocks(PlayerUtils.playersblock.get(player));
        }
    }

    public static void addExp(Player player,int exp){
        utils.addExp(player,exp);
    }

    /*
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equalsIgnoreCase("setlobby")){
            if(sender instanceof Player)
            {
                config.set("map.joinlocation.world",((Player) sender).getLocation().getWorld().getName());
                config.set("map.joinlocation.x",((Player) sender).getLocation().getX());
                config.set("map.joinlocation.y",((Player) sender).getLocation().getY());
                config.set("map.joinlocation.z",((Player) sender).getLocation().getZ());
                saveConfig();
            }
        }
        return super.onCommand(sender, command, label, args);
    }*/
}
