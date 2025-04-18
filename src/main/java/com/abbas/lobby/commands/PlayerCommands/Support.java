package com.abbas.lobby.commands.PlayerCommands;


import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Support implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String lable, String[] args) {
        if (command.getName().equalsIgnoreCase("support")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command!");
                return true;
            }
            if (!sender.hasPermission("lobby.support")) {
                sender.sendMessage(ColorUtils.translateColorCodes("&cYou do not have permission to use this command!"));
                return true;
            }
            Player p = (Player) sender;
            p.sendMessage(ColorUtils.translateColorCodes("&6&lMainCommands Support"));
            p.sendMessage(ColorUtils.translateColorCodes("&6&l/discord - Join Discord Server"));
            p.sendMessage(ColorUtils.translateColorCodes("&6&l/teamspeak - Join Teamspeak Server - " + ColorUtils.translateColorCodes("&c Beta and Coming Soon")));
            p.sendMessage(ColorUtils.translateColorCodes("&6&l/forums - Visit our Forums - " + ColorUtils.translateColorCodes("&c Beta and Coming Soon")));
            return true;
        }
        return false;
    }
}
