package net.lawaxi.mc.minesweeper;

import net.lawaxi.mc.minesweeper.Game.JGG.JGG;
import net.lawaxi.mc.minesweeper.Game.MS.Minesweeper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public final class InventoryGames extends JavaPlugin {

    public static FileConfiguration config;
    public static InventoryGames instance;
    public static Logger logger;



    @Override
    public void onEnable() {
        // Plugin startup logic

        saveDefaultConfig();
        config = getConfig();
        instance=this;
        logger=getLogger();
        configfile=new File(getDataFolder(),"config.yml");


        Minesweeper.init();
        Bukkit.getPluginManager().registerEvents(new Minesweeper(),this);
        JGG.init();
        Bukkit.getPluginManager().registerEvents(new JGG(),this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    public static ItemStack getItemStack (String key){

        ItemStack stack;
        String ma = config.getString(key+".item");
        if(ma.contains(":"))
            stack = new ItemStack(
                    Material.getMaterial(ma.substring(0,ma.indexOf(":"))),1,
                            Short.valueOf(ma.substring(ma.indexOf(":")+1))
            );
        else
            stack = new ItemStack(Material.getMaterial(ma),1);


        if(config.contains(key+".name")){
            ItemMeta a = stack.getItemMeta();
            a.setDisplayName(config.getString(key+".name"));
            stack.setItemMeta(a);
        }

        return stack.clone();

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            if (command.getName().equalsIgnoreCase("ms"))
            {
                if(args.length>0){
                    if(args[0].equalsIgnoreCase("reset") && Minesweeper.saolei.containsKey(sender.getName()))
                    {
                        Minesweeper.saolei.remove(sender.getName());
                    }
                }

                Minesweeper.start((Player) sender);
                return true;
            }
            else if(command.getName().equalsIgnoreCase("jgg")){
                JGG.start((Player) sender);
                return true;
            }
        }
        return super.onCommand(sender, command, label, args);
    }


    private static File configfile;
    public static void saveCFG(){
        try {
            config.save(configfile);
        }
        catch (IOException e){}
    }
}
