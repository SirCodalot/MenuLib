package me.codalot.gui.menus;

import lombok.Getter;
import lombok.Setter;
import me.codalot.gui.menus.components.IComponent;
import me.codalot.gui.menus.components.canvases.Canvas;
import me.codalot.gui.menus.components.canvases.ListCanvas;
import me.codalot.gui.menus.components.clickables.Clickable;
import me.codalot.gui.menus.components.items.AnimatedItem;
import me.codalot.gui.utils.MaterialUtils;
import me.codalot.gui.utils.SoundUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
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
import java.util.function.BiConsumer;

@Getter
public class Menu implements InventoryHolder {

    private MenuData data;
    private Menu previous;

    private Inventory inventory;
    private Canvas canvas;
    private Map<Integer, Clickable> clickables;
    private Set<AnimatedItem> animations;

    @Setter private Map<String, BiConsumer<Player, ClickType>> actions;

    public Menu(MenuData data, Map<String, BiConsumer<Player, ClickType>> actions) {
        this.data = data;
        previous = null;

        inventory = data.createInventory(this);
        canvas = data.cloneCanvas();
        clickables = new HashMap<>();
        animations = new HashSet<>();

        this.actions = actions;
        generateActions();

        update();
    }

    public Menu(MenuData data) {
        this(data, new HashMap<>());
    }

    private void generateActions() {
        for (IComponent component : canvas.getAllComponents()) {
            if (component instanceof ListCanvas) {
                ListCanvas list = (ListCanvas) component;

                if (list.getTag() != null) {
                    actions.put(list.getTag() + "-forward", (player, click) -> list.scrollForward());
                    actions.put(list.getTag() + "-backward", (player, click) -> list.scrollBackward());
                }
            }
        }

        actions.put("exit", (player, click) -> player.closeInventory());
    }

    public void update() {
        inventory.clear();
        clickables.clear();
        animations.clear();
        canvas.draw(this, new Slot());
    }

    public void open(Player player, boolean switched) {
        if (switched)
            SoundUtils.play(player, data.getSwitchSound());
        else
            SoundUtils.play(player, data.getOpenSound());

        player.openInventory(inventory);
    }

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

        clickables.get(event.getSlot()).click(actions, (Player) event.getWhoClicked(), event.getClick());
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
