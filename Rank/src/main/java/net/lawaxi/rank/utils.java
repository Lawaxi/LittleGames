package net.lawaxi.rank;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class utils {

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
    }

    public static int getExp(Player player){
        return Rank.levels.getInt("players."+player.getName());
    }

    public static int getLevel(Player player){
        return getExp(player)/Rank.levels.getInt("types.exp.each");
    }

    private static List<String> replace(List<String> a ,String target,String replacement){
        List<String> b = new ArrayList<>();
        for(String member : a)
        {
            b.add(member.replace(target,replacement));
        }
        return b;
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
        player.setScoreboard(scoreboard);
    }
}
