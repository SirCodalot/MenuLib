package me.codalot.gui.menus;

import lombok.Getter;
import lombok.Setter;
import me.codalot.gui.menus.components.canvases.Canvas;
import me.codalot.gui.menus.components.items.AnimatedItem;
import me.codalot.gui.menus.components.items.MenuItem;
import me.codalot.gui.menus.components.items.StaticItem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * MenuData holds information that is used when creating a menu.
 * For example: title, rows, sounds, components, etc...
 */
@SuppressWarnings({"WeakerAccess", "unused"})
@Getter
@Setter
public class MenuData {

    private String title;
    private int rows;

    private boolean openPrevious;

    private String openSound;
    private String closeSound;
    private String switchSound;

    private Canvas canvas;

    private Map<String, BiConsumer<Menu, InventoryClickEvent>> actions;

    /**
     * Use this constructor when making a menu from code.
     *
     * @param title The inventory's title
     * @param rows How many rows will the inventory have
     */
    public MenuData(String title, int rows) {
        this.title = title;
        this.rows = rows;

        openPrevious = false;

        openSound = null;
        closeSound = null;
        switchSound = null;

        canvas = new Canvas();
        actions = getGlobalActions();
    }

    /**
     * Use this constructor when making a menu from a file
     *
     * @param file The YAML file that holds the menu's information.
     */
    public MenuData(YamlConfiguration file) {
        title = ChatColor.translateAlternateColorCodes('&', file.getString("title", "Untitled"));
        rows = file.getInt("rows", 3);

        openPrevious = file.getBoolean("open-previous", false);

        openSound = file.getString("open-sound");
        closeSound = file.getString("close-sound");
        switchSound = file.getString("switch-sound");

        Map<String, MenuItem> items = new HashMap<>();
        if (file.contains("items"))
            for (String key : file.getConfigurationSection("items").getKeys(false)) {
                if (file.contains("items." + key + ".frames"))
                    items.put(key, new AnimatedItem(toMap(file.getConfigurationSection("items." + key))));
                else
                    items.put(key, new StaticItem(toMap(file.getConfigurationSection("items." + key))));
            }

        canvas = new Canvas(items, toMap(file.getConfigurationSection("canvas")));
        actions = getGlobalActions();
    }

    public Inventory createInventory(Menu menu) {
        return Bukkit.createInventory(menu, rows * 9, title);
    }

    public Canvas cloneCanvas() {
        return canvas.clone();
    }

    private static Map<String, Object> toMap(ConfigurationSection section) {
        Map<String, Object> map = new HashMap<>();

        for (String key : section.getKeys(false)) {
            Object value = section.get(key);

            if (value instanceof ConfigurationSection)
                map.put(key, toMap((ConfigurationSection) value));
            else
                map.put(key, value);

        }

        return map;
    }

    private static Map<String, BiConsumer<Menu, InventoryClickEvent>> getGlobalActions() {
        Map<String, BiConsumer<Menu, InventoryClickEvent>> map = new HashMap<>();

        map.put("exit", (menu, event) -> event.getWhoClicked().closeInventory());
        map.put("return", (menu, event) -> {
            if (menu.getPrevious() != null)
                menu.getPrevious().open((Player) event.getWhoClicked());
        });

        return map;
    }
}
