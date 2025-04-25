package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class UnmuteCommand implements ICommandAPI {
   private static final String COMMAND_NAME = "unmute";
   private static final String PERMISSION_NODE = "lobby.unmute";

   public UnmuteCommand() {
      setupConfig();
   }

   @Override
   public void setupConfig() {
      Config.setup();
      FileConfiguration config = Config.getConfig();
      if (!config.isConfigurationSection("muteMessages.unmute")) {
         config.set(ConfigCommandPath.UNMUTE_NO_PERMISSION, "&c⚠ You do not have permission to unmute players!");
         config.set(ConfigCommandPath.UNMUTE_USAGE, "&c⚠ Usage: /unmute <player>");
         config.set(ConfigCommandPath.UNMUTE_NOT_MUTED, "&c⚠ That player is not muted!");
         config.set(ConfigCommandPath.UNMUTE_NOT_FOUND, "&c⚠ Player not found or is offline!");
         config.set(ConfigCommandPath.UNMUTE_SUCCESS, "&a✔ Successfully unmuted %player%");
         config.set(ConfigCommandPath.UNMUTE_TARGET, "&a✔ You have been unmuted!");
         config.set(ConfigCommandPath.UNMUTE_STAFF_NOTIFICATION, "&7[Staff] &e%player% &7was unmuted by &e%unmuter%");
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

      if (args.length < 1) {
         sender.sendMessage(ColorUtils.translateColorCodes(
                 Config.getConfig().getString(ConfigCommandPath.UNMUTE_USAGE)));
         return true;
      }

      return processUnmute(sender, args[0]);
   }

   private boolean processUnmute(CommandSender sender, String playerName) {
      FileConfiguration config = Config.getConfig();
      Player target = Bukkit.getPlayer(playerName);

      if (target == null) {
         boolean found = false;
         for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getName().equalsIgnoreCase(playerName)) {
               if (!MuteCommand.isPlayerMuted(onlinePlayer.getUniqueId())) {
                  sender.sendMessage(ColorUtils.translateColorCodes(
                          config.getString(ConfigCommandPath.UNMUTE_NOT_MUTED)));
                  return true;
               }
               MuteCommand.unmute(onlinePlayer.getUniqueId());
               found = true;
               target = onlinePlayer;
               break;
            }
         }

         if (!found) {
            sender.sendMessage(ColorUtils.translateColorCodes(
                    config.getString(ConfigCommandPath.UNMUTE_NOT_FOUND)));
            return true;
         }
      } else {
         if (!MuteCommand.isPlayerMuted(target.getUniqueId())) {
            sender.sendMessage(ColorUtils.translateColorCodes(
                    config.getString(ConfigCommandPath.UNMUTE_NOT_MUTED)));
            return true;
         }
         MuteCommand.unmute(target.getUniqueId());
      }

      sender.sendMessage(ColorUtils.translateColorCodes(
              config.getString(ConfigCommandPath.UNMUTE_SUCCESS)
                      .replace("%player%", playerName)));

      if (target != null) {
         target.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.UNMUTE_TARGET)));
      }

      notifyStaff(sender, playerName, target);
      return true;
   }

   private void notifyStaff(CommandSender sender, String playerName, Player target) {
      String staffMessage = ColorUtils.translateColorCodes(
              Config.getConfig().getString(ConfigCommandPath.UNMUTE_STAFF_NOTIFICATION)
                      .replace("%player%", playerName)
                      .replace("%unmuter%", sender instanceof Player ? sender.getName() : "Console"));

      for (Player player : Bukkit.getOnlinePlayers()) {
         if (player.hasPermission(PERMISSION_NODE) && player != sender && player != target) {
            player.sendMessage(staffMessage);
         }
      }
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
      return "Unmute a player from chat";
   }

   @Override
   public void sendNoPermissionMessage(CommandSender sender) {
      sender.sendMessage(ColorUtils.translateColorCodes(
              Config.getConfig().getString(ConfigCommandPath.UNMUTE_NO_PERMISSION)));
   }

   @Override
   public boolean isPlayerOnly() {
      return false;
   }

   @Override
   public void sendPlayerOnlyMessage(CommandSender sender) {
   }
}
