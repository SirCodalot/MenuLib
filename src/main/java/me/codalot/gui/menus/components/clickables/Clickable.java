package me.codalot.gui.menus.components.clickables;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Map;
import java.util.function.BiConsumer;

public interface Clickable {

    void click(Map<String, BiConsumer<Player, ClickType>> actions, Player player, ClickType type);

}
