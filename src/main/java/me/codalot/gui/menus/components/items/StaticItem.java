package me.codalot.gui.menus.components.items;

import me.codalot.gui.utils.ItemUtils;
import me.codalot.gui.utils.PlaceholderUtils;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * This is a MenuItem that always stays the same.
 */
@SuppressWarnings("WeakerAccess")
public class StaticItem implements MenuItem {

    private ItemStack item;

    public StaticItem(ItemStack item) {
        this.item = item;
    }

    public StaticItem(Map<String, Object> map) {
        this(ItemUtils.deserialize(map));
    }

    @Override
    public ItemStack getItem() {
        return item;
    }

    @Override
    public ItemStack getItem(String... placeholders) {
        return PlaceholderUtils.apply(item.clone(), placeholders);
    }

    @Override
    @SuppressWarnings("all")
    public MenuItem clone() {
        return new StaticItem(item.clone());
    }
}
