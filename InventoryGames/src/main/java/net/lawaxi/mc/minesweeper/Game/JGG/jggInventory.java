package net.lawaxi.mc.minesweeper.Game.JGG;

import net.lawaxi.mc.minesweeper.Game.Sounds;
import net.lawaxi.mc.minesweeper.InventoryGames;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class jggInventory {

    public static String title;
    public static int size;
    public static ArrayList<Integer> slot;
    public static int duty;
    public static ItemStack none;
    public static ArrayList<ItemStack> playerb = new ArrayList<>();
    public static ArrayList<ItemStack> finishb = new ArrayList<>();


    public jggInventory(Player player1, Player player2) {


        this.player.add(player1);
        this.player.add(player2);

        this.playerhead.add(new ItemStack(Material.SKULL_ITEM,1,(short) 3));
        this.playerhead.add(new ItemStack(Material.SKULL_ITEM,1,(short) 3));
        SkullMeta a = (SkullMeta)this.playerhead.get(0).getItemMeta();
        a.setOwner(player1.getName());
        a.setDisplayName(InventoryGames.config.getString("JGG.display.dutyname").replace("%player%",player1.getName()));
        this.playerhead.get(0).setItemMeta(a);
        SkullMeta b = (SkullMeta)this.playerhead.get(1).getItemMeta();
        b.setOwner(player2.getName());
        b.setDisplayName(InventoryGames.config.getString("JGG.display.dutyname").replace("%player%",player2.getName()));
        this.playerhead.get(1).setItemMeta(b);

        inventory = Bukkit.createInventory(null, size,title);

        for(int i=0;i<9;i++){
            slots.put(i,-1);
            inventory.setItem(slot.get(i),none);
        }

        inventory.setItem(duty,this.playerhead.get(0));
    }

    public ArrayList<Player> player = new ArrayList<>();
    public ArrayList<ItemStack> playerhead = new ArrayList<>();
    public int now = 0; //目前出牌者
    public Inventory inventory;
    public HashMap<Integer,Integer> slots = new HashMap<>();


    public Player getNow(){
        return player.get(now%2);
    }

    public void choose(int slot){
        if(this.slot.contains(slot))
        {
            if(slots.get(this.slot.indexOf(slot))==-1)
            {
                slots.replace(this.slot.indexOf(slot),now%2);
                inventory.setItem(slot,playerb.get(now%2));

                int a = check();
                if(a==-1){
                    now++;
                    inventory.setItem(duty,this.playerhead.get(now%2));

                }
                else
                    end(a);

            }
        }
    }

    private int check(){

        if(slots.get(0)==slots.get(1)&& slots.get(1)==slots.get(2) && slots.get(1)!=-1){
            inventory.setItem(slot.get(0),finishb.get(slots.get(0)));
            inventory.setItem(slot.get(1),finishb.get(slots.get(0)));
            inventory.setItem(slot.get(2),finishb.get(slots.get(0)));
            return slots.get(0);
        }


        if(slots.get(3)==slots.get(4)&& slots.get(4)==slots.get(5) && slots.get(3)!=-1){
            inventory.setItem(slot.get(3),finishb.get(slots.get(3)));
            inventory.setItem(slot.get(4),finishb.get(slots.get(3)));
            inventory.setItem(slot.get(5),finishb.get(slots.get(3)));
            return slots.get(3);
        }

        if(slots.get(6)==slots.get(7)&& slots.get(7)==slots.get(8) && slots.get(6)!=-1){
            inventory.setItem(slot.get(6),finishb.get(slots.get(6)));
            inventory.setItem(slot.get(7),finishb.get(slots.get(6)));
            inventory.setItem(slot.get(8),finishb.get(slots.get(6)));
            return slots.get(6);
        }

        if(slots.get(0)==slots.get(4)&& slots.get(4)==slots.get(8) && slots.get(0)!=-1){
            inventory.setItem(slot.get(0),finishb.get(slots.get(0)));
            inventory.setItem(slot.get(4),finishb.get(slots.get(0)));
            inventory.setItem(slot.get(8),finishb.get(slots.get(0)));
            return slots.get(0);
        }

        if(slots.get(2)==slots.get(4)&& slots.get(4)==slots.get(6) && slots.get(2)!=-1){
            inventory.setItem(slot.get(2),finishb.get(slots.get(2)));
            inventory.setItem(slot.get(4),finishb.get(slots.get(2)));
            inventory.setItem(slot.get(6),finishb.get(slots.get(2)));
            return slots.get(2);
        }

        if(slots.get(0)==slots.get(3)&& slots.get(3)==slots.get(6) && slots.get(0)!=-1){
            inventory.setItem(slot.get(0),finishb.get(slots.get(0)));
            inventory.setItem(slot.get(3),finishb.get(slots.get(0)));
            inventory.setItem(slot.get(6),finishb.get(slots.get(0)));
            return slots.get(0);
        }

        if(slots.get(1)==slots.get(4)&& slots.get(4)==slots.get(7) && slots.get(1)!=-1){
            inventory.setItem(slot.get(1),finishb.get(slots.get(1)));
            inventory.setItem(slot.get(4),finishb.get(slots.get(1)));
            inventory.setItem(slot.get(7),finishb.get(slots.get(1)));
            return slots.get(1);
        }

        if(slots.get(2)==slots.get(5)&& slots.get(5)==slots.get(8) && slots.get(2)!=-1){
            inventory.setItem(slot.get(2),finishb.get(slots.get(2)));
            inventory.setItem(slot.get(5),finishb.get(slots.get(2)));
            inventory.setItem(slot.get(8),finishb.get(slots.get(2)));
            return slots.get(2);
        }

        if(checkfull())
            return 3;

        return -1;
    }
    //0 1 2
    //3 4 5
    //6 7 8

    private boolean checkfull(){
        for(int i=0;i<9;i++){
            if(slots.get(i)==-1)
                return false;
        }
        return true;
    }

    public void end(int a){

        if(a==3)
        {
            Sounds.WinSound(player.get(0));
            Sounds.WinSound(player.get(1));

            player.get(0).sendMessage(InventoryGames.config.getString("JGG.messages.draw"));
            player.get(1).sendMessage(InventoryGames.config.getString("JGG.messages.draw"));

            Bukkit.broadcastMessage(InventoryGames.config.getString("JGG.messages.broad2")
                    .replace("%1%",player.get(0).getDisplayName())
                    .replace("%2%",player.get(1).getDisplayName())
                    .replace("%n%",String.valueOf(now/2)));
        }
        else
        {
            Sounds.WinSound(player.get(a));
            Sounds.FailedSound(player.get((a+1)%2));
            player.get(a).sendMessage(InventoryGames.config.getString("JGG.messages.win"));
            player.get((a+1)%2).sendMessage(InventoryGames.config.getString("JGG.messages.failed"));

            Bukkit.broadcastMessage(InventoryGames.config.getString("JGG.messages.broad")
                    .replace("%win%",player.get(a).getDisplayName())
                    .replace("%failed%",player.get((a+1)%2).getDisplayName())
                    .replace("%n%",String.valueOf(now/2)));
        }

        JGG.JGGs.remove(player.get(0));
        JGG.JGGs.remove(player.get(1));


    }

}
