package me.codalot.gui.utils;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class PlaceholderUtils {

    public static String apply(String original, String... placeholders) {
        for (String placeholder : placeholders) {
            original = original.replace("%" + placeholder.split(":")[0], placeholder.split(":", 2)[1]);
        }
        return original;
    }

    public static List<String> apply(List<String> original, String... placeholders) {
        List<String> replaced = new ArrayList<>();

        for (String line : original) {
            replaced.add(apply(line, placeholders));
        }

        return replaced;
    }

    public static ItemStack apply(ItemStack item, String... placeholders) {
        ItemMeta meta = item.getItemMeta();

        if (meta.hasDisplayName())
            meta.setDisplayName(apply(meta.getDisplayName(), placeholders));

        if (meta.hasLore())
            meta.setLore(apply(meta.getLore(), placeholders));

        item.setItemMeta(meta);
        return item;
    }

}
