package me.codalot.gui.menus.components.clickables;

import me.codalot.gui.menus.Menu;
import org.bukkit.event.inventory.InventoryClickEvent;

public interface Clickable {

    void click(Menu menu, InventoryClickEvent event);

}
