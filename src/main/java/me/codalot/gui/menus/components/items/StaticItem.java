package me.codalot.gui.menus.components.items;

import lombok.Getter;
import me.codalot.gui.utils.ItemUtils;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * This is a MenuItem that always stays the same.
 */
@Getter
public class StaticItem implements MenuItem {

    private ItemStack item;

    public StaticItem(ItemStack item) {
        this.item = item;
    }

    public StaticItem(Map<String, Object> map) {
        this(ItemUtils.deserialize(map));
    }

    @Override
    @SuppressWarnings("all")
    public MenuItem clone() {
        return new StaticItem(item.clone());
    }
}
