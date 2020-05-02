package net.lawaxi.mc.minesweeper.Game;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {

    public static void StartSound(Player player){
        player.playSound(player.getLocation(), Sound.BLOCK_ANVIL_USE,1,1);
    }

    public static void FailedSound(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_GENERIC_EXPLODE,1,1);
    }

    public static void WinSound(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1,1);
    }

    public static void GameSound1(Player player){
        player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER,1,1);
    }
}
