package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.MuteConfig;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MuteCommand implements CommandExecutor {
   private static final String PERMISSION_MUTE = "lobby.mute";
   private static final String PERMISSION_EXEMPT = "lobby.mute.exempt";
   private static final Map<UUID, Long> mutedPlayers = new HashMap<>();
   private static final Map<UUID, String> muteReasons = new HashMap<>();
   private static final Map<UUID, String> muteIds = new HashMap<>();

   @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      MuteConfig.setupConfig();
      FileConfiguration config = Config.getConfig();

      if (!sender.hasPermission(PERMISSION_MUTE)) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.mute.noPermission")));
         return true;
      }

      if (args.length < 2) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.mute.usage")));
         return true;
      }

      String playerName = args[0];
      Player target = Bukkit.getPlayer(playerName);
      if (target == null) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.mute.playerNotFound")));
         return true;
      }

      if (target.hasPermission(PERMISSION_EXEMPT)) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.mute.exempt")));
         return true;
      }

      if (mutedPlayers.containsKey(target.getUniqueId())) {
         sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.mute.alreadyMuted")));
         return true;
      }

      String durationStr = args[1];
      StringBuilder reasonBuilder = new StringBuilder();
      Long duration = null;
      int i;

      if (durationStr.matches("\\d+[smhd]")) {
         duration = parseDuration(durationStr);
         if (duration == null) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', config.getString("muteMessages.mute.invalidDuration")));
            return true;
         }
         for (i = 2; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(" ");
         }
      } else {
         for (i = 1; i < args.length; i++) {
            reasonBuilder.append(args[i]).append(" ");
         }
      }

      String reason = reasonBuilder.toString().trim();
      if (reason.isEmpty()) {
         reason = "No reason specified";
      }

      String muteId = generateMuteID();
      muteIds.put(target.getUniqueId(), muteId);

      if (duration != null) {
         long expirationTime = System.currentTimeMillis() + duration;
         mutedPlayers.put(target.getUniqueId(), expirationTime);
         String durationFormatted = formatDuration(duration);

         target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                 config.getString("muteMessages.mute.playerMuted")
                         .replace("%duration%", durationFormatted)
                         .replace("%reason%", reason)
                         .replace("%mute_id%", muteId)));

         sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                 config.getString("muteMessages.mute.success")
                         .replace("%player%", target.getName())
                         .replace("%duration%", durationFormatted)
                         .replace("%mute_id%", muteId)));
      } else {
         mutedPlayers.put(target.getUniqueId(), null);
         target.sendMessage(ChatColor.translateAlternateColorCodes('&',
                 config.getString("muteMessages.mute.playerMutedPermanent")
                         .replace("%reason%", reason)
                         .replace("%mute_id%", muteId)));

         sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                 config.getString("muteMessages.mute.successPermanent")
                         .replace("%player%", target.getName())
                         .replace("%mute_id%", muteId)));
      }

      muteReasons.put(target.getUniqueId(), reason);
      notifyStaff(sender, target, reason);
      return true;
   }

   private void notifyStaff(CommandSender sender, Player target, String reason) {
      String staffMessage = ChatColor.GRAY + "[Staff] " + ChatColor.YELLOW + target.getName() +
              ChatColor.GRAY + " was muted by " + ChatColor.YELLOW + (sender instanceof Player ? sender.getName() : "Console") +
              ChatColor.GRAY + " for: " + ChatColor.YELLOW + reason;

      for (Player player : Bukkit.getOnlinePlayers()) {
         if (player.hasPermission(PERMISSION_MUTE) && player != sender && player != target) {
            player.sendMessage(staffMessage);
         }
      }
   }

   private String generateMuteID() {
      String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
      StringBuilder id = new StringBuilder("#");
      for (int i = 0; i < 7; i++) {
         id.append(chars.charAt((int) (Math.random() * chars.length())));
      }
      return id.toString();
   }

   private Long parseDuration(String duration) {
      Pattern pattern = Pattern.compile("(\\d+)([smhd])");
      Matcher matcher = pattern.matcher(duration);
      if (matcher.matches()) {
         long amount = Long.parseLong(matcher.group(1));
         switch (matcher.group(2)) {
            case "s":
               return amount * 1000L;
            case "m":
               return amount * 60L * 1000L;
            case "h":
               return amount * 60L * 60L * 1000L;
            case "d":
               return amount * 24L * 60L * 60L * 1000L;
            default:
               return null;
         }
      }
      return null;
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

   public static boolean isPlayerMuted(UUID playerUUID) {
      if (!mutedPlayers.containsKey(playerUUID)) {
         return false;
      }
      Long expirationTime = mutedPlayers.get(playerUUID);
      if (expirationTime == null) {
         return true;
      }
      if (System.currentTimeMillis() >= expirationTime) {
         mutedPlayers.remove(playerUUID);
         muteReasons.remove(playerUUID);
         return false;
      }
      return true;
   }

   public static String getMuteTimeLeft(UUID playerUUID) {
      Long expirationTime = mutedPlayers.get(playerUUID);
      if (expirationTime == null) return "Permanent";
      long timeLeft = expirationTime - System.currentTimeMillis();
      if (timeLeft <= 0) {
         mutedPlayers.remove(playerUUID);
         muteReasons.remove(playerUUID);
         return "0s";
      }
      return formatDuration(timeLeft);
   }

   public static String getMuteReason(UUID playerUUID) {
      return muteReasons.getOrDefault(playerUUID, "No reason specified");
   }

   public static String getMuteID(UUID playerUUID) {
      return muteIds.getOrDefault(playerUUID, "N/A");
   }

   public static void unmute(UUID playerUUID) {
      mutedPlayers.remove(playerUUID);
      muteReasons.remove(playerUUID);
      muteIds.remove(playerUUID);
   }
}
