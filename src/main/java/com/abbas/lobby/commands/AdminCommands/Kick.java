package com.abbas.lobby.commands.AdminCommand;

import com.abbas.lobby.Utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kick implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("kick")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;

                if(!player.hasPermission("lobby.kick")) {
                    player.sendMessage(ColorUtils.translateColorCodes("&cYou do not have permission to run that command!"));
                    return true;
                }

                if (args.length < 1) {
                    player.sendMessage(ColorUtils.translateColorCodes("&cUsage: /kick <player> [reason]"));
                    return true;
                }

                Player target = Bukkit.getPlayer(args[0]);
                if (target == null) {
                    player.sendMessage(ColorUtils.translateColorCodes("&cPlayer not Online!"));
                    return true;
                }

                String reason = "You have been kicked from the server!";
                if (args.length > 1) {
                    reason = String.join(" ", args).substring(args[0].length()).trim();
                }

                target.kickPlayer(ColorUtils.translateColorCodes("&cYou were kicked!\n Reason: " + reason));
                player.sendMessage(ColorUtils.translateColorCodes("&aSuccessfully Kicked " + target.getName() + "for: " + reason));
                return true;
            }
        }
        return true;
    }

}
