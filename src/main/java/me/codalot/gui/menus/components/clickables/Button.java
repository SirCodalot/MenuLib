package me.codalot.gui.menus.components.clickables;

import lombok.Getter;
import me.codalot.gui.menus.Menu;
import me.codalot.gui.menus.Slot;
import me.codalot.gui.menus.components.IComponent;
import me.codalot.gui.menus.components.items.AnimatedItem;
import me.codalot.gui.menus.components.items.MenuItem;
import me.codalot.gui.utils.CommandUtils;
import me.codalot.gui.utils.SoundUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * This is a special menu component that can be clicked.
 */
@Getter
public class Button implements IComponent, Clickable {

    private String tag;

    private MenuItem item;
    private String action;

    private String permission;
    private String noPermissionMessage;

    private String sound;
    private String noPermissionSound;

    private List<String> commands;
    private List<String> sudo;

    /**
     * Use this constructor when making a menu with code
     */
    public Button(MenuItem item) {
        tag = null;

        this.item = item;
        action = null;

        permission = null;
        noPermissionMessage = null;

        sound = null;
        noPermissionSound = null;

        commands = null;
        sudo = null;
    }

    @SuppressWarnings("all")
    public Button(Map<String, MenuItem> items, Map<String, Object> map) {
        tag = (String) map.get("tag");

        item = items.get(map.get("item")).clone();
        action = (String) map.get("action");

        permission = (String) map.get("permission");
        noPermissionMessage = (String) map.get("no-permission-message");

        sound = (String) map.get("sound");
        noPermissionSound = (String) map.get("no-permission-sound");

        commands = (List<String>) map.get("commands");
        sudo = (List<String>) map.get("sudo");
    }

    public Button setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public Button setAction(String action) {
        this.action = action;
        return this;
    }

    public Button setPermission(String permission) {
        this.permission = permission;
        return this;
    }

    public Button setNoPermissionMessage(String noPermissionMessage) {
        this.noPermissionMessage = noPermissionMessage;
        return this;
    }

    public Button setSound(String sound) {
        this.sound = sound;
        return this;
    }

    public Button setNoPermissionSound(String noPermissionSound) {
        this.noPermissionSound = noPermissionSound;
        return this;
    }

    public Button setCommands(List<String> commands) {
        this.commands = commands;
        return this;
    }

    public Button setSudo(List<String> sudo) {
        this.sudo = sudo;
        return this;
    }

    @Override
    public boolean hasTag(String tag) {
        return this.tag != null && this.tag.equalsIgnoreCase(tag);
    }

    @Override
    public void click(Map<String, BiConsumer<Player, ClickType>> actions, Player player, ClickType type) {
        if (permission != null && !player.hasPermission(permission)) {
            if (noPermissionMessage != null)
                player.sendMessage(noPermissionMessage);

            SoundUtils.play(player, noPermissionSound);
            return;
        }

        BiConsumer<Player, ClickType> action = actions.get(this.action);
        if (action != null)
            action.accept(player, type);


        if (commands != null)
            CommandUtils.execute(commands, "name:" + player.getName());
        if (sudo != null)
            CommandUtils.execute(player, sudo, "name:" + player.getName());

        SoundUtils.play(player, sound);
    }

    @Override
    public void draw(Menu menu, Slot slot) {
        if (slot.isOutside(menu.getInventory()))
            return;

        if (item instanceof AnimatedItem)
            menu.getAnimations().add((AnimatedItem) item);

        menu.getInventory().setItem(slot.getSlot(), item.getItem());
        menu.getClickables().put(slot.getSlot(), this);
    }

    @Override
    @SuppressWarnings("all")
    public Button clone() {
        return new Button(item.clone())
                .setTag(tag)
                .setAction(action)
                .setPermission(permission)
                .setNoPermissionMessage(noPermissionMessage)
                .setSound(sound)
                .setNoPermissionSound(noPermissionSound)
                .setCommands(commands == null ? null : new ArrayList<>(commands))
                .setSudo(sudo == null ? null : new ArrayList<>(sudo));
    }

}
