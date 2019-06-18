package me.codalot.gui.menus.components;

import me.codalot.gui.menus.components.canvases.Canvas;
import me.codalot.gui.menus.components.canvases.ListCanvas;
import me.codalot.gui.menus.components.clickables.Button;
import me.codalot.gui.menus.components.items.MenuItem;

import java.util.Map;

public enum ComponentType {

    BUTTON,
    CANVAS,
    LIST;

    public static IComponent newInstance(Map<String, MenuItem> items, Map<String, Object> map) {
        ComponentType type = map.containsKey("type") ? valueOf((String) map.get("type")) : BUTTON;

        switch (type) {
            case BUTTON:
                return new Button(items, map);
            case CANVAS:
                return new Canvas(items, map);
            case LIST:
                return new ListCanvas(items, map);
        }

        return null;
    }

}
