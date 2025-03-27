package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.UnbanConfig;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Unban implements CommandExecutor {

    public Unban() {
        UnbanConfig.setupConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("unban")) {
            if (!sender.hasPermission("lobby.unban")) {
                sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("unbanMessages.unban.noPermission")));
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("unbanMessages.unban.usage")));
                return true;
            }

            String target = args[0];

            if (Bukkit.getBanList(BanList.Type.NAME).isBanned(target)) {
                Bukkit.getBanList(BanList.Type.NAME).pardon(target);
                sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("unbanMessages.unban.success").replace("%player%", target)));
            } else {
                sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("unbanMessages.unban.notBanned")));
            }
            return true;
        }
        return false;
    }
}