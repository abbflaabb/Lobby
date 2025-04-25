package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.WarnConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class WarnCommand implements ICommandAPI {
   private static final String COMMAND_NAME = "warn";
   private static final String PERMISSION_NODE = "lobby.warn";
   private static final String EXEMPT_PERMISSION = "lobby.warn.exempt";

   public WarnCommand() {
      setupConfig();
   }

   @Override
   public void setupConfig() {
      WarnConfig.setupConfig();
      Config.setup();
      FileConfiguration config = Config.getConfig();
      if (!config.isConfigurationSection("warnMessages.warn")) {
         config.set(ConfigCommandPath.WARN_NO_PERMISSION, "&c⚠ You do not have permission to warn players!");
         config.set(ConfigCommandPath.WARN_USAGE, "&c⚠ Usage: /warn <player> <reason>");
         config.set(ConfigCommandPath.WARN_PLAYER_NOT_FOUND, "&c⚠ Player %player% not found!");
         config.set(ConfigCommandPath.WARN_EXEMPT, "&c⚠ You cannot warn this player!");
         config.set(ConfigCommandPath.WARN_HEADER, "&7&m---------------------");
         config.set(ConfigCommandPath.WARN_WARNING_MESSAGE, "&c⚠ You have been warned by &e%sender%");
         config.set(ConfigCommandPath.WARN_REASON_MESSAGE, "&7Reason: &f%reason%");
         config.set(ConfigCommandPath.WARN_SUCCESS, "&a✔ Successfully warned %player% for: %reason%");
         config.set(ConfigCommandPath.WARN_STAFF_NOTIFICATION, "&7[Staff] &e%player% &7was warned by &e%sender% &7for: &f%reason%");
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

      if (args.length < 2) {
         sender.sendMessage(ColorUtils.translateColorCodes(
                 Config.getConfig().getString(ConfigCommandPath.WARN_USAGE)));
         return true;
      }

      return processWarn(sender, args);
   }

   private boolean processWarn(CommandSender sender, String[] args) {
      FileConfiguration config = Config.getConfig();
      String playerName = args[0];
      Player target = Bukkit.getPlayer(playerName);

      if (target == null) {
         sender.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.WARN_PLAYER_NOT_FOUND)
                         .replace("%player%", playerName)));
         return true;
      }

      if (target.hasPermission(EXEMPT_PERMISSION)) {
         sender.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.WARN_EXEMPT)));
         return true;
      }

      String reason = buildReason(args);
      sendWarningMessages(sender, target, reason);
      notifyStaff(sender, target, reason);
      return true;
   }

   private String buildReason(String[] args) {
      StringBuilder reasonBuilder = new StringBuilder();
      for (int i = 1; i < args.length; i++) {
         reasonBuilder.append(args[i]).append(" ");
      }
      return reasonBuilder.toString().trim();
   }

   private void sendWarningMessages(CommandSender sender, Player target, String reason) {
      FileConfiguration config = Config.getConfig();

      target.sendMessage("");
      target.sendMessage(ColorUtils.translateColorCodes(
              config.getString(ConfigCommandPath.WARN_HEADER)));
      target.sendMessage(ColorUtils.translateColorCodes(
              config.getString(ConfigCommandPath.WARN_WARNING_MESSAGE)
                      .replace("%sender%", sender.getName())));
      target.sendMessage(ColorUtils.translateColorCodes(
              config.getString(ConfigCommandPath.WARN_REASON_MESSAGE)
                      .replace("%reason%", reason)));
      target.sendMessage(ColorUtils.translateColorCodes(
              config.getString(ConfigCommandPath.WARN_HEADER)));
      target.sendMessage("");

      sender.sendMessage(ColorUtils.translateColorCodes(
              config.getString(ConfigCommandPath.WARN_SUCCESS)
                      .replace("%player%", target.getName())
                      .replace("%reason%", reason)));
   }

   private void notifyStaff(CommandSender sender, Player target, String reason) {
      String staffMessage = ColorUtils.translateColorCodes(
              Config.getConfig().getString(ConfigCommandPath.WARN_STAFF_NOTIFICATION)
                      .replace("%player%", target.getName())
                      .replace("%sender%", sender instanceof Player ? sender.getName() : "Console")
                      .replace("%reason%", reason));

      for (Player player : Bukkit.getOnlinePlayers()) {
         if (player != sender && player != target && player.hasPermission(PERMISSION_NODE)) {
            player.sendMessage(staffMessage);
         }
      }

      Bukkit.getLogger().info(org.bukkit.ChatColor.stripColor(staffMessage));
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
      return "Warn a player for breaking rules";
   }

   @Override
   public void sendNoPermissionMessage(CommandSender sender) {
      sender.sendMessage(ColorUtils.translateColorCodes(
              Config.getConfig().getString(ConfigCommandPath.WARN_NO_PERMISSION)));
   }

   @Override
   public boolean isPlayerOnly() {
      return false;
   }

   @Override
   public void sendPlayerOnlyMessage(CommandSender sender) {
   }
}
