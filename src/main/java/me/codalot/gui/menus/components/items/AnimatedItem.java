package me.codalot.gui.menus.components.items;

import lombok.Getter;
import me.codalot.gui.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This is a MenuItem that changes every few ticks, making
 * it look like an animated item.
 */
@SuppressWarnings("WeakerAccess")
@Getter
public class AnimatedItem implements MenuItem {

    private List<ItemStack> frames;
    private ItemStack item;

    private int ticks;
    private int frame;

    public AnimatedItem(List<ItemStack> frames, int ticks) {
        this.frames = frames;
        this.ticks = ticks;
        frame = 0;
        item = frames.get(0).clone();
    }

    @SuppressWarnings("unchecked")
    public AnimatedItem(Map<String, Object> map) {
        frames = new ArrayList<>();
        for (Map<String, Object> serialized : (List<Map<String, Object>>) map.get("frames")) {
            frames.add(ItemUtils.deserialize(serialized));
        }
        ticks = (int) map.getOrDefault("ticks", 5);
        frame = 0;
        item = frames.get(0).clone();
    }

    public void nextFrame() {
        frame++;
        if (frame >= frames.size())
            frame = 0;

        ItemUtils.set(item, frames.get(frame));
    }

    @Override
    @SuppressWarnings("all")
    public MenuItem clone() {
        List<ItemStack> frames = new ArrayList<>();
        this.frames.forEach(frame -> frames.add(frame.clone()));
        return new AnimatedItem(frames, ticks);
    }
}
