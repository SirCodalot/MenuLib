package me.codalot.gui.menus;

import lombok.Getter;
import me.codalot.gui.menus.components.IComponent;
import me.codalot.gui.menus.components.canvases.Canvas;
import me.codalot.gui.menus.components.clickables.Button;
import me.codalot.gui.menus.components.clickables.Clickable;
import me.codalot.gui.menus.components.items.AnimatedItem;
import me.codalot.gui.utils.MaterialUtils;
import me.codalot.gui.utils.SoundUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Menus are InventoryHolders that behave differently. They store the
 * menu's main canvas as well as all of the clickable and animated
 * components.
 */
@SuppressWarnings("unused")
@Getter
public class Menu implements InventoryHolder {

    private MenuData data;
    private Menu previous;

    private Inventory inventory;
    private Canvas canvas;
    private Map<Integer, Clickable> clickables;
    private Set<AnimatedItem> animations;

    /**
     * @param data The menu's data (title, rows, items, etc...)
     */
    public Menu(MenuData data) {
        this.data = data;
        previous = null;

        inventory = data.createInventory(this);
        canvas = data.cloneCanvas();
        clickables = new HashMap<>();
        animations = new HashSet<>();

        update();
    }

    /**
     * This method clears the inventory and re-draws all of the components.
     */
    public void update() {
        inventory.clear();
        clickables.clear();
        animations.clear();
        canvas.draw(this, new Slot());
    }

    /**
     * Replaces certain text parts in items' names and lores.
     *
     * @param placeholders Array of placeholders. Placeholder format: key:value
     */
    public void applyPlaceholders(String... placeholders) {
        for (IComponent component : canvas.getAllComponents()) {
            if (component instanceof Button)
                ((Button) component).setPlaceholders(placeholders);
        }
    }

    private void open(Player player, boolean switched) {
        if (switched)
            SoundUtils.play(player, data.getSwitchSound());
        else
            SoundUtils.play(player, data.getOpenSound());

        player.openInventory(inventory);
    }

    /**
     * This method opens the menu for a player. Please
     * use this method instead of Player#openInventory
     *
     * @param player The menu will open for this player.
     */
    public void open(Player player) {
        boolean switched = false;

        if (isInMenu(player)) {
            if (previous == null)
                previous = (Menu) player.getOpenInventory().getTopInventory().getHolder();
            switched = true;
        }

        open(player, switched);
    }

    public void onInventoryClick(InventoryClickEvent event) {
        event.setCancelled(true);

        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(inventory))
            return;

        if (event.getCurrentItem() == null || MaterialUtils.isAir(event.getCurrentItem().getType()))
            return;

        clickables.get(event.getSlot()).click(this, event);
        update();
    }

    public void onInventoryClose(InventoryCloseEvent event, JavaPlugin plugin) {
        Player player = (Player) event.getPlayer();

        boolean canGoBack = previous != null && data.isOpenPrevious();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!player.isOnline())
                    return;

                if (!canGoBack) {
                    if (!isInMenu(player))
                        SoundUtils.play(player, data.getCloseSound());
                    return;
                }

                if (!isInMenu(player))
                    previous.open(player, true);

            }
        }.runTaskLater(plugin, 1);
    }

    private static boolean isInMenu(Player player) {
        return player.getOpenInventory().getTopInventory().getHolder() instanceof Menu;
    }

}
