package me.codalot.gui.menus.components.items;

import org.bukkit.inventory.ItemStack;

public interface MenuItem {

    ItemStack getItem();

    MenuItem clone();

}
