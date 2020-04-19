package net.lawaxi.mcgame.skywars.init.tasks;

import net.lawaxi.mcgame.skywars.Skywars;
import net.lawaxi.mcgame.skywars.config.Config;
import net.lawaxi.mcgame.skywars.config.Messages;
import net.lawaxi.mcgame.skywars.init.Game;
import net.lawaxi.mcgame.skywars.utils.GameState;
import net.lawaxi.mcgame.skywars.utils.Scoreboard;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class GameWaitTask extends BukkitRunnable {

    public GameWaitTask(int waittime) {
        this.waittime=waittime;
        this.count=0;
    }

    private int count;
    private int waittime;

    @Override
    public void run() {

        count++;
        int time = waittime - count+1;

        if(time==0)
        {
            Game.startGame();
            this.cancel();
        }
        else {

            //提前加载箱子
            if(time==2)
                new ChestPrepareTask().runTask(Skywars.instance);

            //玩家聊天提示
            if (time > 5 && time % 5 == 0)
                Bukkit.broadcastMessage(Messages.read("lobby.wait.msg").toString().replace("%time%", String.valueOf(time)));
            else if (time <= 5) {
                Bukkit.broadcastMessage(Messages.read("lobby.wait.msg5sec").toString().replace("%time%", String.valueOf(time)));

                for (org.bukkit.entity.Player player : Bukkit.getOnlinePlayers()) {
                    player.sendTitle(Messages.read("lobby.wait.title").toString().replace("%time%", String.valueOf(time)),
                            Messages.read("lobby.wait.sub").toString().replace("%time%", String.valueOf(time)),20,40,20);
                }
            }

            //计分板信息
            Scoreboard.waittime = time;
            Scoreboard.reloadScoreboard();

            //人满，加速
            if (Scoreboard.player == (int) Config.read("map.maxplayer")) {
                if (time > (int) Config.read("lobby.waittime_full")) {
                    Bukkit.broadcastMessage(Messages.read("lobby.wait.addition2").toString());
                    this.count+=waittime-(int) Config.read("lobby.waittime_full");
                }
            }

            //人数不够，计时结束
            if(Scoreboard.player < (int)Config.read("map.minplayer")) {
                Scoreboard.waittime=100;
                Skywars.state=GameState.Lobby;
                Bukkit.broadcastMessage(Messages.read("lobby.wait.addition1").toString());
                this.cancel();
            }

        }

    }
}
