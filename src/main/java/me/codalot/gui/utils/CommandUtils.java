package me.codalot.gui.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandUtils {

    public static void execute(CommandSender sender, List<String> commands, String... placeholders) {
        PlaceholderUtils.apply(commands, placeholders)
                .forEach(command -> Bukkit.dispatchCommand(sender, command));
    }

    public static void execute(List<String> commands, String... placeholders) {
        execute(Bukkit.getConsoleSender(), commands, placeholders);
    }

}
