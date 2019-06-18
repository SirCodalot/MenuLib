package me.codalot.gui.menus.components;

import me.codalot.gui.menus.Menu;
import me.codalot.gui.menus.Slot;

/**
 * Everything inside a menu is an IComponent.
 */
@SuppressWarnings("unused")
public interface IComponent {

    String getTag();

    boolean hasTag(String tag);

    /**
     * This method places the component inside the inventory.
     * If the component is holding other components, then it'll
     * call their draw method.
     *
     * @param menu Which menu the component should be drawn in.
     * @param slot Where should the component be drawn.
     */
    void draw(Menu menu, Slot slot);

    IComponent clone();

}
