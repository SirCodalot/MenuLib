package me.codalot.gui.listeners;

import me.codalot.gui.menus.Menu;
import me.codalot.gui.menus.components.items.AnimatedItem;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public class MenuListener implements Listener {

    private JavaPlugin plugin;

    public MenuListener(JavaPlugin plugin, boolean animations) {
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(this, plugin);

        if (!animations)
            return;

        new BukkitRunnable() {
            private int tick = 0;
            @Override
            public void run() {
                Set<Menu> animated = new HashSet<>();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Menu menu = getMenu(player.getOpenInventory().getTopInventory());

                    if (menu == null)
                        continue;

                    if (animated.contains(menu))
                        continue;

                    animated.add(menu);

                    if (menu.getAnimations().isEmpty())
                        continue;

                    for (AnimatedItem animation : menu.getAnimations()) {
                        if (tick % animation.getTicks() == 0)
                            animation.nextFrame();
                    }

                    menu.update();
                }

                tick++;
            }
        }.runTaskTimer(plugin, 10, 1);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Menu menu = getMenu(event.getInventory());

        if (menu != null)
            menu.onInventoryClick(event);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Menu menu = getMenu(event.getInventory());

        if (menu != null)
            menu.onInventoryClose(event, plugin);
    }

    private Menu getMenu(Inventory inventory) {
        if (inventory.getHolder() instanceof Menu)
            return (Menu) inventory.getHolder();
        else
            return null;
    }

}
