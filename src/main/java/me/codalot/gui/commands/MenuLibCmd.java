package me.codalot.gui.commands;

import me.codalot.gui.MenuLib;
import me.codalot.gui.menus.Menu;
import me.codalot.gui.menus.MenuData;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.HashMap;

public class MenuLibCmd implements CommandExecutor {

    private static HashMap<String, MenuData> menus = new HashMap<>();

    @SuppressWarnings("all")
    public MenuLibCmd(MenuLib plugin) {
        plugin.getCommand("menulib").setExecutor(this);
        reload();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 0) {
            if (!sender.hasPermission("menulib.help")) {
                sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to execute this command.");
                return true;
            }

            sender.sendMessage(new String[]{
                    ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "MenuLib Commands:",
                    "",
                    ChatColor.AQUA + "  /mlib reload [optional:menu-name]" + ChatColor.DARK_AQUA + " Reloads a menu.",
                    ChatColor.AQUA + "  /mlib open <menu-name> [optional:player]" + ChatColor.DARK_AQUA + " Opens a menu.",
                    ChatColor.AQUA + "  /mlib list" + ChatColor.DARK_AQUA + " Lists all menus.",
                    ""
            });

            return true;
        }

        switch (args[0].toLowerCase()) {
            case "reload":
                if (!sender.hasPermission("menulib.reload")) {
                    sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to execute this command.");
                    return true;
                }

                if (args.length == 1) {
                    reload();
                    sender.sendMessage(ChatColor.DARK_AQUA + "Successfully reloaded all of the menus.");
                } else {
                    try {
                        if (reload(args[1]))
                            sender.sendMessage(ChatColor.DARK_AQUA + "Successfully reloaded " + ChatColor.AQUA + args[1] + ChatColor.DARK_AQUA + ".");
                        else
                            sender.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " does not exist.");
                    } catch (Exception e) {
                        sender.sendMessage(ChatColor.DARK_RED + "This menu isn't configured properly.");
                    }
                }
                return true;
            case "open":
                if (!sender.hasPermission("menulib.open")) {
                    sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to execute this command.");
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(ChatColor.DARK_RED + "Invalid Usage. Type " + ChatColor.RED + "/mlib" + ChatColor.DARK_RED + " for help.");
                    return true;
                }

                Player target;
                if (args.length == 2) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(ChatColor.DARK_RED + "You must be a player to open a menu.");
                        return true;
                    }
                    target = (Player) sender;
                } else
                    target = Bukkit.getPlayer(args[2]);

                if (target == null) {
                    sender.sendMessage(ChatColor.DARK_RED + "This player is offline.");
                    return true;
                }

                if (!open(target, args[1]))
                    sender.sendMessage(ChatColor.RED + args[1] + ChatColor.DARK_RED + " does not exist.");

                return true;
            case "list":
                if (!sender.hasPermission("menulib.list")) {
                    sender.sendMessage(ChatColor.DARK_RED + "You don't have permission to execute this command.");
                    return true;
                }

                sender.sendMessage(ChatColor.DARK_AQUA + "" + ChatColor.UNDERLINE + "Available Menus:");
                sender.sendMessage("");
                if (menus.isEmpty())
                    sender.sendMessage(ChatColor.DARK_GRAY + "There aren't any available menus.");
                else
                    menus.keySet().forEach(name -> sender.sendMessage(ChatColor.AQUA + "- " + name));
                sender.sendMessage("");
                return true;
        }

        sender.sendMessage(ChatColor.DARK_RED + "Invalid Usage. Type " + ChatColor.RED + "/mlib" + ChatColor.DARK_RED + " for help.");

        return true;
    }

    private boolean reload(String name) {
        File file = new File(MenuLib.getInstance().getDataFolder(), "menus/" + name + ".yml");

        if (!file.exists())
            return false;

        menus.put(name, new MenuData(YamlConfiguration.loadConfiguration(file)));
        return true;
    }

    private boolean open(Player player, String name) {
        MenuData data = menus.get(name);
        if (data == null)
            return false;

        new Menu(data).open(player);
        return true;
    }

    @SuppressWarnings("all")
    private void reload() {
        File folder = new File(MenuLib.getInstance().getDataFolder(), "menus");
        if (!folder.exists())
            folder.mkdirs();

        for (File file : folder.listFiles()) {
            try {
                String name = file.getName().replace(".yml", "");
                menus.put(name, new MenuData(YamlConfiguration.loadConfiguration(file)));
            } catch (Exception ignored) {}
        }
    }
}
