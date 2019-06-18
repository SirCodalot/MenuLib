package me.codalot.gui.menus.components.canvases;

import lombok.Getter;
import me.codalot.gui.menus.Menu;
import me.codalot.gui.menus.Slot;
import me.codalot.gui.menus.components.ComponentType;
import me.codalot.gui.menus.components.IComponent;
import me.codalot.gui.menus.components.items.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class Canvas extends HashMap<Slot, IComponent> implements IComponent {

    protected String tag;

    public Canvas() {
        super();

        tag = null;
    }

    @SuppressWarnings("unchecked")
    public Canvas(Map<String, MenuItem> items, Map<String, Object> map) {
        for (Map<String, Object> key : (List<Map<String, Object>>) map.get("components")) {
            IComponent component = ComponentType.newInstance(items, key);
            if (component == null)
                continue;

            List<Slot> positions = new ArrayList<>();

            if (key.containsKey("positions"))
                ((List<Object>) key.get("positions")).forEach(slot -> positions.add(new Slot(slot)));
            if (key.containsKey("position"))
                positions.add(new Slot(key.get("position")));

            positions.forEach(slot -> put(slot, component.clone()));
        }
    }

    public List<IComponent> getAllComponents() {
        List<IComponent> components = new ArrayList<>();

        for (IComponent component : values()) {
            if (component instanceof Canvas)
                components.addAll(((Canvas) component).getAllComponents());

            components.add(component);
        }

        return components;
    }

    @Override
    public boolean hasTag(String tag) {
        return this.tag != null && this.tag.equalsIgnoreCase(tag);
    }

    @Override
    public void draw(Menu menu, Slot slot) {
        forEach((key, component) -> component.draw(menu, slot.clone().add(key)));
    }

    public Canvas setTag(String tag) {
        this.tag = tag;
        return this;
    }

    @Override
    @SuppressWarnings("all")
    public Canvas clone() {
        Canvas other = new Canvas().setTag(tag);
        forEach((key, component) -> other.put(key.clone(), component.clone()));
        return other;
    }
}
