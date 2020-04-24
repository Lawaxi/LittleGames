package net.lawaxi.rank;

import org.bukkit.entity.Player;

public class utils {

    public static void addExp(Player player, int amount){

        String name = player.getName();
        int level = Rank.levels.getInt("players."+name)/Rank.levels.getInt("types.exp.each");
        if(level<Rank.levels.getInt("types.exp.max"))
        {
            if(amount>Rank.levels.getInt("types.exp.max")-level)
            {
                amount=Rank.levels.getInt("types.exp.max")-level;
            }
            int exp = Rank.levels.getInt("players."+player)+amount;
            Rank.levels.set("players."+player,exp);
            Rank.saveLevels();

            if(exp/Rank.levels.getInt("types.exp.each") != level)
                Rank.reloadPlayerLevels(player.getName());
        }
    }

    public static int getExp(Player player){
        return Rank.levels.getInt("players."+player.getName());
    }

    public static int getLevel(Player player){
        return getExp(player)/Rank.levels.getInt("types.exp.each");
    }
}
