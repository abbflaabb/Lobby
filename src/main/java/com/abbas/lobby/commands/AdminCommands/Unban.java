package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.UnbanConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class Unban implements ICommandAPI {
   private static final String COMMAND_NAME = "unban";
   private static final String PERMISSION_NODE = "lobby.unban";

   public Unban() {
      setupConfig();
   }

   @Override
   public void setupConfig() {
      UnbanConfig.setupConfig();
      Config.setup();
      if (!Config.getConfig().isConfigurationSection("unbanMessages")) {
         Config.getConfig().set("unbanMessages.noPermission", "&c⚠ You do not have permission to unban players!");
         Config.getConfig().set("unbanMessages.usage", "&c⚠ Usage: /unban <player>");
         Config.getConfig().set("unbanMessages.notBanned", "&c⚠ That player is not banned!");
         Config.getConfig().set("unbanMessages.success", "&a✔ Successfully unbanned %player%");
         Config.save();
      }
   }

   @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!command.getName().equalsIgnoreCase(getCommandName())) {
         return false;
      }

      if (!hasPermission(sender)) {
         sendNoPermissionMessage(sender);
         return true;
      }

      if (args.length != 1) {
         sender.sendMessage(ColorUtils.translateColorCodes(
                 Config.getConfig().getString("unbanMessages.usage")));
         return true;
      }

      return processUnban(sender, args[0]);
   }

   private boolean processUnban(CommandSender sender, String target) {
      if (Bukkit.getBanList(org.bukkit.BanList.Type.NAME).isBanned(target)) {
         Bukkit.getBanList(org.bukkit.BanList.Type.NAME).pardon(target);
         removeBanId(target);

         sender.sendMessage(ColorUtils.translateColorCodes(
                 Config.getConfig().getString("unbanMessages.success")
                         .replace("%player%", target)));
      } else {
         sender.sendMessage(ColorUtils.translateColorCodes(
                 Config.getConfig().getString("unbanMessages.notBanned")));
      }
      return true;
   }

   private void removeBanId(String playerName) {
      Config.getConfig().set("banIds." + playerName.toLowerCase(), null);
      Config.save();
   }

   @Override
   public boolean hasPermission(CommandSender sender) {
      return sender.hasPermission(getPermissionNode());
   }

   @Override
   public String getPermissionNode() {
      return PERMISSION_NODE;
   }

   @Override
   public String getCommandName() {
      return COMMAND_NAME;
   }

   @Override
   public String getDescription() {
      return "Unban a player from the server";
   }

   @Override
   public void sendNoPermissionMessage(CommandSender sender) {
      sender.sendMessage(ColorUtils.translateColorCodes(
              Config.getConfig().getString("unbanMessages.noPermission")));
   }

   @Override
   public boolean isPlayerOnly() {
      return false;
   }

   @Override
   public void sendPlayerOnlyMessage(CommandSender sender) {
   }
}