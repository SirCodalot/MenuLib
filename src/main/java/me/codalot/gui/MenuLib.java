package me.codalot.gui;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuLib extends JavaPlugin {

    @Getter private static MenuLib instance;

    @Override
    public void onEnable() {
        instance = this;
    }

}
