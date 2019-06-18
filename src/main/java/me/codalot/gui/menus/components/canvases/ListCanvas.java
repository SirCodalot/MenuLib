package me.codalot.gui.menus.components.canvases;

import lombok.Getter;
import me.codalot.gui.menus.Menu;
import me.codalot.gui.menus.Slot;
import me.codalot.gui.menus.components.ComponentType;
import me.codalot.gui.menus.components.IComponent;
import me.codalot.gui.menus.components.items.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class ListCanvas extends Canvas {

    private List<IComponent> components;
    private List<Slot> slots;

    private int index;
    private int scroll;

    private boolean loop;

    public ListCanvas() {
        super();

        components = new ArrayList<>();
        slots = new ArrayList<>();

        index = 0;
        scroll = 0;

        loop = false;
    }

    @SuppressWarnings("all")
    public ListCanvas(Map<String, MenuItem> items, Map<String, Object> map) {
        super();

        tag = (String) map.get("tag");

        components = new ArrayList<>();
        for (Map<String, Object> key : (List<Map<String, Object>>) map.get("components")) {
            components.add(ComponentType.newInstance(items, key));
        }

        slots = new ArrayList<>();
        ((List<Object>) map.get("slots")).forEach(slot -> slots.add(new Slot(slot)));

        index = 0;
        scroll = (int) map.getOrDefault("scroll", slots.size());

        loop = (boolean) map.getOrDefault("loop", false);
    }

    private void validateIndex() {
        if (loop)
            index = index % components.size();
        else {
            if (index < 0)
                index = 0;
            if (index > components.size())
                index = components.size();
        }
    }

    @Override
    public List<IComponent> getAllComponents() {
        List<IComponent> components = new ArrayList<>();

        for (IComponent component : this.components) {
            if (component instanceof Canvas)
                components.addAll(((Canvas) component).getAllComponents());

            components.add(component);
        }

        return components;
    }

    public void scrollForward() {
        index += scroll;
        validateIndex();
    }

    public void scrollBackward() {
        index -= scroll;
        validateIndex();
    }

    public void refresh() {
        clear();

        for (int i = 0; i < slots.size(); i++) {
            int compSlot = index + i;
            if (loop)
                compSlot = loopIndex(compSlot, components.size());

            if (compSlot < 0 || compSlot >= components.size())
                break;

            put(slots.get(i), components.get(compSlot));
        }
    }

    public ListCanvas setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public ListCanvas setComponents(List<IComponent> components) {
        this.components = components;
        return this;
    }

    public ListCanvas  setSlots(List<Slot> slots) {
        this.slots = slots;
        return this;
    }

    public ListCanvas setIndex(int index) {
        this.index = index;
        return this;
    }

    public ListCanvas setScroll(int scroll) {
        this.scroll = scroll;
        return this;
    }

    public ListCanvas setLoop(boolean loop) {
        this.loop = loop;
        return this;
    }

    @Override
    public void draw(Menu menu, Slot slot) {
        refresh();
        super.draw(menu, slot);
    }

    @Override
    @SuppressWarnings("all")
    public ListCanvas clone() {
        List<IComponent> components = new ArrayList<>();
        this.components.forEach(component -> components.add((IComponent) component.clone()));

        List<Slot> slots = new ArrayList<>();
        this.slots.forEach(slot -> slots.add(slot.clone()));

        return new ListCanvas()
                .setTag(tag)
                .setComponents(components)
                .setSlots(slots)
                .setIndex(index)
                .setScroll(scroll)
                .setLoop(loop);
    }

    private static int loopIndex(int index, int max) {
        if (index < 0)
            return  max + index;
        if (index >= max)
            return  index % max;

        return index;
    }
}
