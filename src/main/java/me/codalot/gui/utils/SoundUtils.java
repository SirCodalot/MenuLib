package me.codalot.gui.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class SoundUtils {

    public static void play(Player player, String serialized) {
        if (serialized == null)
            return;

        String[] split = serialized.split(" ");

        Sound sound = split.length > 0 ? Sound.valueOf(split[0]) : Sound.values()[0];
        float volume = split.length > 1 ? Float.parseFloat(split[1]) : 10;
        float pitch = split.length > 2 ? Float.parseFloat(split[2]) : 1;

        player.playSound(player.getLocation(), sound, volume, pitch);
    }

}
