package com.abbas.lobby.menus.command;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.menus.MenuGames;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class openMenuCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
        if (command.getName().equalsIgnoreCase("menu")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ColorUtils.translateColorCodes("&c&lThe Command Just Players Used"));
                return true;
            }

            Player player = (Player) sender;
            MenuGames.openGameMenu(player);
            return true;
        }
        return false;
    }
}
