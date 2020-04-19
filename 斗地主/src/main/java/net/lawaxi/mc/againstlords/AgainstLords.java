package net.lawaxi.mc.againstlords;

import net.lawaxi.mc.againstlords.lisener.BlockPlace;
import net.lawaxi.mc.againstlords.lisener.InventoryClose;
import net.lawaxi.mc.againstlords.lisener.InventoryClick;
import net.lawaxi.mc.againstlords.lisener.PlayerDropItem;
import net.lawaxi.mc.againstlords.utils.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public final class AgainstLords extends JavaPlugin {

    private static FileConfiguration config;

    public static boolean isInGame = false;
    public static AgainstLords instance;

    @Override
    public void onEnable() {

        instance= this;

        getServer().getPluginManager().registerEvents(new BlockPlace(), this);
        getServer().getPluginManager().registerEvents(new InventoryClose(), this);
        getServer().getPluginManager().registerEvents(new InventoryClick(), this);
        getServer().getPluginManager().registerEvents(new PlayerDropItem(), this);

        saveDefaultConfig();
        config=getConfig();

        Card.init();
        Card.cards=(ArrayList<ItemStack>)config.getList("items");
        LordChosingGUI.chosingitems = (ArrayList<ItemStack>)config.getList("lordchosing");
        GameGUI.openBlock = (ItemStack)config.get("block");
        GameGUI.openBlock.setAmount(1);
    }



    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


    private static HashMap<Integer, ItemStack> cards = new HashMap<>(); //al set的存储变量
    private static HashMap<Integer, ItemStack> chosings = new HashMap<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(command.getName().equals("al"))
        {
            if(args.length!=0)
            {
                if(args[0].equals("set") && args.length!=1 && sender instanceof Player)
                {
                    int index = Card.CardToIndex(args[1]);
                    if(index==-1)
                    {
                        String sending = "§6可选的牌：§e";
                        for(int i=0;i<Card.size();i++)
                        {
                            sending+=Card.indexToCard(i);
                            if(i!=Card.size()-1)
                                sending+="§6,§e";
                        }
                        sender.sendMessage(sending);
                    }
                    else
                    {
                        if(cards.containsKey(index))
                            cards.replace(index,((Player)sender).getItemInHand());
                        else
                            cards.put(index,((Player)sender).getItemInHand());


                        sender.sendMessage("§a设置成功");
                    }

                    return true;

                }
                else if(args[0].equals("cset") && args.length!=1 && sender instanceof Player)
                {
                    int index = Integer.valueOf(args[1]);
                    if(index >=0 && index <=3)
                    {
                        if(chosings.containsKey(index))
                            chosings.replace(index,((Player)sender).getItemInHand());
                        else
                            chosings.put(index,((Player)sender).getItemInHand());

                        sender.sendMessage("§a设置成功");
                    }

                    return true;
                }
                else if(args[0].equals("save"))
                {
                    //判断是否完成牌对应物品的设置
                    for(int i=0;i<Card.size();i++)
                    {
                        if(!cards.containsKey(i))
                        {
                            sender.sendMessage("§c您尚未完成牌 §4"+Card.indexToCard(i)+"§c 的设置");
                            return true;
                        }
                    }

                    //判断是否完成地主选择对应物品的设置
                    for(int i=0;i<=3;i++)
                    {
                        if(!chosings.containsKey(i))
                        {
                            sender.sendMessage("§c您尚未完成地主选择中 §4"+i+"分§c 的设置");
                            return true;
                        }
                    }

                    //将HashMap转为ArrayList格式保存
                    ArrayList<ItemStack> cards = new ArrayList<>();
                    for(int i=0;i<Card.size();i++)
                    {
                        cards.add(this.cards.get(i));
                    }
                    config.set("items",cards);
                    Card.cards=cards;

                    ArrayList<ItemStack> chosings = new ArrayList<>();
                    for(int i=0;i<=3;i++)
                    {
                        chosings.add(this.chosings.get(i));
                    }
                    config.set("lordchosing",chosings);
                    LordChosingGUI.chosingitems = chosings;

                    saveConfig();

                    sender.sendMessage("§a保存成功");
                    return true;
                }
                else if(args[0].equals("setblock") && sender instanceof Player)
                {
                    config.set("block",((Player)sender).getItemInHand());

                    sender.sendMessage("§a设置成功");
                    saveConfig();

                    return true;
                }
            }

            sender.sendMessage("/al set 3(4,5,...,A,2) 设置牌对应的物品");
            sender.sendMessage("/al cset 0(1,2,3) 设置叫地主选择物品");
            sender.sendMessage("/al save 全部设置完后保存配置");
            sender.sendMessage("/al setblock 设置打开GUI用方块 自动保存");
            return true;
        }
        else if(command.getName().equals("start"))
        {
            if(Card.cards.size()==Card.size())
            {
                if(isInGame)
                    sender.sendMessage(getMessages("already"));
                else
                    new BukkitRunnable(){
                        @Override
                        public void run() {
                            if(!Game.start())
                            {
                                sender.sendMessage(getMessages("notenough"));
                            }
                        }
                    }.runTask(instance);
            }
            else
            {
                sender.sendMessage("§c管理员尚未完成对牌物品的配置，暂时无法开始游戏！");
            }
            return true;
        }
        else if(command.getName().equals("end"))
        {
            if(isInGame)
                Game.end(getMessages("win3title"),getMessages("win3sub"));

            return true;
        }

        return super.onCommand(sender, command, label, args);
    }

    public static String getMessages(String halfkey)
    {
        if(config.contains("messages."+halfkey))
        {
            return config.get("messages."+halfkey).toString();
        }
        return "";
    }

    public static ArrayList<String> getMessageList(String halfkey)
    {
        if(config.contains("messages."+halfkey))
        {
            return (ArrayList<String>)config.getList("messages."+halfkey);
        }
        return new ArrayList<>();
    }

}
