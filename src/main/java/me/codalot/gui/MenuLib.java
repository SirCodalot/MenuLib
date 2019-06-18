package me.codalot.gui;

import lombok.Getter;
import me.codalot.gui.commands.MenuLibCmd;
import me.codalot.gui.listeners.MenuListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * MenuLib is an opensource library for making customizable menus.
 * For help, visit this project on GitHub.
 *
 * @author Shoham Tzubery (SirCodalot)
 * @version 1.0
 */
public class MenuLib extends JavaPlugin {

    @Getter private static MenuLib instance;

    @Override
    @SuppressWarnings("all")
    public void onEnable() {
        instance = this;

        File menusFolder = new File(getDataFolder(), "menus");
        if (!menusFolder.exists())
            menusFolder.mkdirs();

        getConfig().options().copyDefaults(true);
        saveDefaultConfig();

        new MenuLibCmd(this);
        new MenuListener(this, getConfig().getBoolean("support-animations", false));
    }

}
