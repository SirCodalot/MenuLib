package me.codalot.gui.menus.components;

import me.codalot.gui.menus.Menu;
import me.codalot.gui.menus.Slot;

public interface IComponent {

    String getTag();

    boolean hasTag(String tag);

    void draw(Menu menu, Slot slot);

    IComponent clone();

}
