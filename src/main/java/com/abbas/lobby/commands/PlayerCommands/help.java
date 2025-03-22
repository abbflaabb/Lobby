package com.abbas.lobby.commands.PlayerCommands;

import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class help implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
        if (command.getName().equalsIgnoreCase("help")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("&cYou must be a player to use this command!");
                return true;
            }
            Player p = (Player) sender;
            if (!p.hasPermission("lobby.help")) {
                p.sendMessage("&cYou do not have permission to use this command!");
                return true;
            }
            p.sendMessage(ColorUtils.translateColorCodes("&6&l/lobby - &e&lMain command for the lobby"));
            p.sendMessage(ColorUtils.translateColorCodes("&6&l/ping - Check your ping"));
            p.sendMessage(ColorUtils.translateColorCodes("&6&l/support - Get support from a staff member"));
            p.sendMessage(ColorUtils.translateColorCodes("&6&l/information - Get information about the server - " + ColorUtils.translateColorCodes("&c&lComingSoon")));
            p.sendMessage(ColorUtils.translateColorCodes("&6&l/menu - Show GamesMenu"));
            return true;
        }

        return false;
    }
}
