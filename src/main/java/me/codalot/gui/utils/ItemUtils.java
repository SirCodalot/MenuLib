package me.codalot.gui.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemUtils {

    public static void set(ItemStack item, ItemStack value) {
        item.setType(value.getType());
        item.setDurability(value.getDurability());
        item.setAmount(value.getAmount());
        item.setItemMeta(value.getItemMeta());
    }

    @SuppressWarnings("all")
    public static ItemStack deserialize(Map<String, Object> map) {
        ItemStack item = createItem((String) map.get("material"));
        ItemMeta meta = item.getItemMeta();

        int amount = (int) map.getOrDefault("amount", 1);
        String name = map.containsKey("name") ?
                ChatColor.translateAlternateColorCodes('&', (String) map.get("name")) : null;
        List<String> lore = colorList((List<String>) map.get("lore"));

        item.setAmount(amount);

        if (name != null)
            meta.setDisplayName(name);

        if (lore != null && !lore.isEmpty())
            meta.setLore(lore);

        if ((boolean) map.getOrDefault("glow", false))
            meta.addEnchant(Enchantment.DURABILITY, 1, true);

        setUnbreakable(meta, (boolean) map.getOrDefault("unbreakable", false));

        meta.addItemFlags(ItemFlag.values());

        item.setItemMeta(meta);
        return item;
    }

    public static ItemStack createItem(String data) {
        String[] split = data.split(":");

        Material material = MaterialUtils.getMaterial(split[0]);
        int durability = split.length > 1 ? Integer.parseInt(split[1]) : 0;

        ItemStack item = new ItemStack(material);
        item.setDurability((short) durability);

        return item;
    }

    private static List<String> colorList(List<String> list) {
        if (list == null)
            return null;

        List<String> colored = new ArrayList<>();
        list.forEach(string -> colored.add(ChatColor.translateAlternateColorCodes('&', string)));

        return colored;
    }

    private static void setUnbreakable(ItemMeta meta, boolean unbreakable) {
        try {
            meta.getClass().getDeclaredMethod("setUnbreakable", boolean.class).invoke(meta, unbreakable);
        } catch (Exception e) {
            meta.spigot().setUnbreakable(unbreakable);
        }
    }

}
