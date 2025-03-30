// Source code is decompiled from a .class file using FernFlower decompiler.
package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Utils.Config;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UnmuteCommand implements CommandExecutor {
   private static final String PERMISSION_UNMUTE = "lobby.unmute";

   public UnmuteCommand() {
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      FileConfiguration config = Config.getConfig();
      if (!sender.hasPermission("lobby.unmute")) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.unmute.noPermission")));
         return true;
      } else if (args.length < 1) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cUsage: /unmute <player>"));
         return true;
      } else {
         String playerName = args[0];
         Player target = Bukkit.getPlayer(playerName);
         if (target == null) {
            boolean found = false;
            Iterator var9 = Bukkit.getOnlinePlayers().iterator();

            while(var9.hasNext()) {
               Player onlinePlayer = (Player)var9.next();
               if (onlinePlayer.getName().equalsIgnoreCase(playerName)) {
                  if (!MuteCommand.isPlayerMuted(onlinePlayer.getUniqueId())) {
                     sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.unmute.notMuted")));
                     return true;
                  }

                  MuteCommand.unmute(onlinePlayer.getUniqueId());
                  found = true;
                  break;
               }
            }

            if (!found) {
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer not found or is offline!"));
               return true;
            }
         } else {
            if (!MuteCommand.isPlayerMuted(target.getUniqueId())) {
               sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.unmute.notMuted")));
               return true;
            }

            MuteCommand.unmute(target.getUniqueId());
         }

         String successMessage = ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.unmute.success").replace("%player%", playerName));
         sender.sendMessage(successMessage);
         if (target != null) {
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou have been unmuted!"));
         }

         String staffMessage = ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.staff.unmuteNotification").replace("%player%", playerName).replace("%unmuter%", sender instanceof Player ? sender.getName() : "Console"));
         Iterator var14 = Bukkit.getOnlinePlayers().iterator();

         while(var14.hasNext()) {
            Player player = (Player)var14.next();
            if (player.hasPermission("lobby.unmute") && player != sender && player != target) {
               player.sendMessage(staffMessage);
            }
         }

         return true;
      }
   }
}
