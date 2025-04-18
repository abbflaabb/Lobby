// Source code is decompiled from a .class file using FernFlower decompiler.
package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.UnbanConfig;
import org.bukkit.Bukkit;
import org.bukkit.BanList.Type;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Unban implements CommandExecutor {
   public Unban() {
      UnbanConfig.setupConfig();
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (command.getName().equalsIgnoreCase("unban")) {
         if (!sender.hasPermission("lobby.unban")) {
            sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("unbanMessages.noPermission")));
            return true;
         } else if (args.length != 1) {
            sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("unbanMessages.usage")));
            return true;
         } else {
            String target = args[0];
            if (Bukkit.getBanList(org.bukkit.BanList.Type.NAME).isBanned(target)) {
               Bukkit.getBanList(org.bukkit.BanList.Type.NAME).pardon(target);

               removeBanId(target);

               sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("unbanMessages.success").replace("%player%", target)));
            } else {
               sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("unbanMessages.notBanned")));
            }

            return true;
         }
      } else {
         return false;
      }
   }

   private void removeBanId(String playerName) {
      Config.getConfig().set("banIds." + playerName.toLowerCase(), null);
      Config.save();
   }
}
