// Source code is decompiled from a .class file using FernFlower decompiler.
package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.WarnConfig;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WarnCommand implements CommandExecutor {

   public WarnCommand() {
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      WarnConfig.setupConfig();
      FileConfiguration config = Config.getConfig();
      if (!sender.hasPermission("lobby.warn")) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.noPermission")));
         return true;
      } else if (args.length < 2) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.usage")));
         return true;
      } else {
         String playerName = args[0];
         Player target = Bukkit.getPlayer(playerName);
         if (target == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.playerNotFound").replace("%player%", playerName)));
            return true;
         } else if (target.hasPermission("lobby.warn.exempt")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.exempt")));
            return true;
         } else {
            StringBuilder reasonBuilder = new StringBuilder();

            for(int i = 1; i < args.length; ++i) {
               reasonBuilder.append(args[i]).append(" ");
            }

            String reason = reasonBuilder.toString().trim();
            target.sendMessage("");
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.header")));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.warningMessage").replace("%sender%", sender.getName())));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.reasonMessage").replace("%reason%", reason)));
            target.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.header")));
            target.sendMessage("");
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.success").replace("%player%", target.getName()).replace("%reason%", reason)));
            String staffMessage = ChatColor.translateAlternateColorCodes('&', config.getString("warnMessages.warn.staffNotification").replace("%player%", target.getName()).replace("%sender%", sender instanceof Player ? sender.getName() : "Console").replace("%reason%", reason));
            Iterator var11 = Bukkit.getOnlinePlayers().iterator();

            while(var11.hasNext()) {
               Player player = (Player)var11.next();
               if (player != sender && player != target && player.hasPermission("lobby.warn")) {
                  player.sendMessage(staffMessage);
               }
            }

            Bukkit.getLogger().info(ChatColor.stripColor(staffMessage));
            return true;
         }
      }
   }
}
