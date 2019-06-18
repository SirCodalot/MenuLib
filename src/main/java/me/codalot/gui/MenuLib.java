package me.codalot.gui;

import lombok.Getter;
import me.codalot.gui.listeners.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MenuLib extends JavaPlugin {

    @Getter private static MenuLib instance;

    @Override
    public void onEnable() {
        instance = this;

        new MenuListener(this, true);
    }

}
