package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.API.ConfigAPI.ConfigCommandPath;
import com.abbas.lobby.API.MainAPIS.ICommandAPI;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.MuteConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MuteCommand implements ICommandAPI {
   private static final String COMMAND_NAME = "mute";
   private static final String PERMISSION_MUTE = "lobby.mute";
   private static final Map<UUID, Long> mutedPlayers = new HashMap<>();
   private static final Map<UUID, String> muteReasons = new HashMap<>();


   public MuteCommand() {
      setupConfig();
   }

   @Override
   public void setupConfig() {
      MuteConfig.setupConfig();
      Config.setup();

      FileConfiguration config = Config.getConfig();
      if (!config.isConfigurationSection("muteMessages")) {
         config.set(ConfigCommandPath.MUTE_NO_PERMISSION, "&c⚠ You do not have permission to mute players!");
         config.set(ConfigCommandPath.MUTE_USAGE, "&c⚠ Usage: /mute <player> [duration] [reason]");
         config.set(ConfigCommandPath.MUTE_PLAYER_NOT_FOUND, "&c⚠ Player not found!");
         config.set(ConfigCommandPath.MUTE_EXEMPT, "&c⚠ This player cannot be muted!");
         config.set(ConfigCommandPath.MUTE_ALREADY_MUTED, "&c⚠ This player is already muted!");
         config.set(ConfigCommandPath.MUTE_INVALID_DURATION, "&c⚠ Invalid duration format! Use s/m/h/d");
         config.set(ConfigCommandPath.MUTE_PLAYER_MUTED, "&c⚠ You have been muted for %duration%\n&7Reason: &f%reason%");
         config.set(ConfigCommandPath.MUTE_PLAYER_MUTED_PERMANENT, "&c⚠ You have been permanently muted!\n&7Reason: &f%reason%");
         config.set(ConfigCommandPath.MUTE_SUCCESS, "&a✔ Successfully muted %player% for %duration%");
         config.set(ConfigCommandPath.MUTE_SUCCESS_PERMANENT, "&a✔ Successfully muted %player% permanently");
         config.set(ConfigCommandPath.MUTE_STAFF_NOTIFICATION,
                 "&7[Staff] &e%player% &7was muted by &e%muter% &7for: &e%reason%.");
         Config.save();
      }

      loadMuteIds();
      startMuteExpirationChecker();
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
                 Config.getConfig().getString(ConfigCommandPath.MUTE_USAGE)));
         return true;
      }

      return processMute(sender, args);
   }
   private boolean processMute(CommandSender sender, String[] args) {
      FileConfiguration config = Config.getConfig();
      String playerName = args[0];
      Player target = Bukkit.getPlayer(playerName);

      if (target == null) {
         sender.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.MUTE_PLAYER_NOT_FOUND)));
         return true;
      }

      if (mutedPlayers.containsKey(target.getUniqueId())) {
         sender.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.MUTE_ALREADY_MUTED)));
         return true;
      }
      if (target.isOp()) {
         sender.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.MUTE_EXEMPT)));
         return true;
      }

      String durationStr = args[1];
      StringBuilder reasonBuilder = new StringBuilder();
      Long duration = null;

      if (durationStr.matches("\\d+[smhd]")) {
         duration = parseDuration(durationStr);
         if (duration == null) {
            sender.sendMessage(ColorUtils.translateColorCodes(
                    config.getString("muteMessages.invalidDuration")));
            return true;
         }
         for (int i = 2; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(" ");
         }
      } else {
         for (int i = 1; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(" ");
         }
      }

      String reason = reasonBuilder.toString().trim();
      if (reason.isEmpty()) {
         reason = "No reason specified";
      }

      applyMute(sender, target, duration, reason);
      return true;
   }

   private void applyMute(CommandSender sender, Player target, Long duration, String reason) {
      String muteId = generateMuteId();
      saveMuteId(target.getName().toLowerCase(), muteId);

      FileConfiguration config = Config.getConfig();

      if (duration != null) {
         long expirationTime = System.currentTimeMillis() + duration;
         mutedPlayers.put(target.getUniqueId(), expirationTime);
         String durationFormatted = formatDuration(duration);

         target.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.MUTE_PLAYER_MUTED)
                         .replace("%duration%", durationFormatted)
                         .replace("%reason%", reason)
                 ));

         sender.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.MUTE_SUCCESS)
                         .replace("%player%", target.getName())
                         .replace("%duration%", durationFormatted)
         ));
      } else {
         mutedPlayers.put(target.getUniqueId(), null);
         target.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.MUTE_PLAYER_MUTED_PERMANENT)
                         .replace("%reason%", reason)
                 ));

         sender.sendMessage(ColorUtils.translateColorCodes(
                 config.getString(ConfigCommandPath.MUTE_SUCCESS_PERMANENT)
                         .replace("%player%", target.getName())
            ));
      }

      muteReasons.put(target.getUniqueId(), reason);
      notifyStaff(sender, target, reason);
   }

   private String generateMuteId() {
      return "MUTE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
   }

   private void saveMuteId(String playerName, String muteId) {
      Config.getConfig().set("muteIds." + playerName, muteId);
      Config.save();
   }

   private void loadMuteIds() {
      if (Config.getConfig().isConfigurationSection("muteIds")) {
         for (String playerName : Config.getConfig().getConfigurationSection("muteIds").getKeys(false)) {
         }
      }
   }

   private void removeMuteId(String playerName) {
      Config.getConfig().set("muteIds." + playerName.toLowerCase(), null);
      Config.save();
   }

   private void startMuteExpirationChecker() {
      new BukkitRunnable() {
         @Override
         public void run() {
            for (Map.Entry<UUID, Long> entry : new HashMap<>(mutedPlayers).entrySet()) {
               if (entry.getValue() != null && System.currentTimeMillis() >= entry.getValue()) {
                  Player player = Bukkit.getPlayer(entry.getKey());
                  if (player != null) {
                     removeMuteId(player.getName());
                     unmute(entry.getKey());
                     player.sendMessage(ColorUtils.translateColorCodes("&aYour mute has expired."));
                  }
               }
            }
         }
      }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Lobby"), 0L, 1200L);
   }

   private void notifyStaff(CommandSender sender, Player target, String reason) {
      String staffMessage = ColorUtils.translateColorCodes(
              Config.getConfig().getString(ConfigCommandPath.MUTE_STAFF_NOTIFICATION)
                      .replace("%player%", target.getName())
                      .replace("%muter%", sender instanceof Player ? sender.getName() : "Console")
                      .replace("%reason%", reason)
        );

      for (Player player : Bukkit.getOnlinePlayers()) {
         if (player.hasPermission(PERMISSION_MUTE) && player != sender && player != target) {
            player.sendMessage(staffMessage);
         }
      }
   }

   private Long parseDuration(String duration) {
      Pattern pattern = Pattern.compile("(\\d+)([smhd])");
      Matcher matcher = pattern.matcher(duration);
      if (matcher.matches()) {
         long amount = Long.parseLong(matcher.group(1));
         switch (matcher.group(2)) {
            case "s": return amount * 1000L;
            case "m": return amount * 60L * 1000L;
            case "h": return amount * 60L * 60L * 1000L;
            case "d": return amount * 24L * 60L * 60L * 1000L;
            default: return null;
         }
      }
      return null;
   }

   public static boolean isPlayerMuted(UUID playerUUID) {
      if (!mutedPlayers.containsKey(playerUUID)) return false;
      Long expirationTime = mutedPlayers.get(playerUUID);
      if (expirationTime == null) return true;
      if (System.currentTimeMillis() >= expirationTime) {
         unmute(playerUUID);
         return false;
      }
      return true;
   }

   public static String getMuteTimeLeft(UUID playerUUID) {
      Long expirationTime = mutedPlayers.get(playerUUID);
      if (expirationTime == null) return "Permanent";
      long timeLeft = expirationTime - System.currentTimeMillis();
      if (timeLeft <= 0) {
         unmute(playerUUID);
         return "0s";
      }
      return formatDuration(timeLeft);
   }

   public static String formatDuration(long duration) {
      long totalSeconds = duration / 1000L;
      long days = totalSeconds / 86400L;
      long hours = (totalSeconds % 86400L) / 3600L;
      long minutes = (totalSeconds % 3600L) / 60L;
      long seconds = totalSeconds % 60L;

      StringBuilder formatted = new StringBuilder();
      if (days > 0) formatted.append(days).append("d ");
      if (hours > 0) formatted.append(hours).append("h ");
      if (minutes > 0) formatted.append(minutes).append("m ");
      if (seconds > 0 || formatted.length() == 0) formatted.append(seconds).append("s");

      return formatted.toString().trim();
   }

   public static String getMuteReason(UUID playerUUID) {
      return muteReasons.getOrDefault(playerUUID, "No reason specified");
   }



   public static void unmute(UUID playerUUID) {
      Player player = Bukkit.getPlayer(playerUUID);
      if (player != null) {
         Config.getConfig().set("muteIds." + player.getName().toLowerCase(), null);
         Config.save();
      }
      mutedPlayers.remove(playerUUID);
      muteReasons.remove(playerUUID);
   }
   @Override
   public boolean hasPermission(CommandSender sender) {
      return sender.hasPermission(getPermissionNode());
   }

   @Override
   public String getPermissionNode() {
      return PERMISSION_MUTE;
   }

   @Override
   public String getCommandName() {
      return COMMAND_NAME;
   }

   @Override
   public String getDescription() {
      return "Mute a player from chatting";
   }

   @Override
   public void sendNoPermissionMessage(CommandSender sender) {
      sender.sendMessage(ColorUtils.translateColorCodes(
              Config.getConfig().getString(ConfigCommandPath.MUTE_NO_PERMISSION)));
   }

   @Override
   public boolean isPlayerOnly() {
      return false;
   }

   @Override
   public void sendPlayerOnlyMessage(CommandSender sender) {
   }
}