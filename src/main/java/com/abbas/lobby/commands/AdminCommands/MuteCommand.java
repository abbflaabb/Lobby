package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import com.abbas.lobby.Utils.MuteConfig;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MuteCommand implements CommandExecutor {
   private static final String PERMISSION_MUTE = "lobby.mute";
   private static final String PERMISSION_EXEMPT = "lobby.mute.exempt";
   private static final Map<UUID, Long> mutedPlayers = new HashMap<>();
   private static final Map<UUID, String> muteReasons = new HashMap<>();
   private static final Map<String, String> muteIds = new HashMap<>();

   public MuteCommand() {
      MuteConfig.setupConfig();
      loadMuteIds();
      startMuteExpirationChecker();
   }

   @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      FileConfiguration config = Config.getConfig();

      if (!sender.hasPermission(PERMISSION_MUTE)) {
         sender.sendMessage(ColorUtils.translateColorCodes(config.getString("muteMessages.noPermission")));
         return true;
      }

      if (args.length < 2) {
         sender.sendMessage(ColorUtils.translateColorCodes(config.getString("muteMessages.usage")));
         return true;
      }

      String playerName = args[0];
      Player target = Bukkit.getPlayer(playerName);
      if (target == null) {
         sender.sendMessage(ColorUtils.translateColorCodes(config.getString("muteMessages.playerNotFound")));
         return true;
      }

      if (target.hasPermission(PERMISSION_EXEMPT)) {
         sender.sendMessage(ColorUtils.translateColorCodes(config.getString("muteMessages.exempt")));
         return true;
      }

      if (mutedPlayers.containsKey(target.getUniqueId())) {
         sender.sendMessage(ColorUtils.translateColorCodes(config.getString("muteMessages.alreadyMuted")));
         return true;
      }

      String durationStr = args[1];
      StringBuilder reasonBuilder = new StringBuilder();
      Long duration = null;

      if (durationStr.matches("\\d+[smhd]")) {
         duration = parseDuration(durationStr);
         if (duration == null) {
            sender.sendMessage(ColorUtils.translateColorCodes(config.getString("muteMessages.invalidDuration")));
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

      String muteId = generateMuteId();
      muteIds.put(target.getName().toLowerCase(), muteId);
      saveMuteId(target.getName().toLowerCase(), muteId);

      if (duration != null) {
         long expirationTime = System.currentTimeMillis() + duration;
         mutedPlayers.put(target.getUniqueId(), expirationTime);
         String durationFormatted = formatDuration(duration);

         target.sendMessage(ColorUtils.translateColorCodes(
                 config.getString("muteMessages.playerMuted")
                         .replace("%duration%", durationFormatted)
                         .replace("%reason%", reason)
                         .replace("%mute_id%", muteId)));

         sender.sendMessage(ColorUtils.translateColorCodes(
                 config.getString("muteMessages.success")
                         .replace("%player%", target.getName())
                         .replace("%duration%", durationFormatted)
                         .replace("%mute_id%", muteId)));
      } else {
         mutedPlayers.put(target.getUniqueId(), null);
         target.sendMessage(ColorUtils.translateColorCodes(
                 config.getString("muteMessages.playerMutedPermanent")
                         .replace("%reason%", reason)
                         .replace("%mute_id%", muteId)));

         sender.sendMessage(ColorUtils.translateColorCodes(
                 config.getString("muteMessages.successPermanent")
                         .replace("%player%", target.getName())
                         .replace("%mute_id%", muteId)));
      }

      muteReasons.put(target.getUniqueId(), reason);
      notifyStaff(sender, target, reason);
      return true;
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
            muteIds.put(playerName, Config.getConfig().getString("muteIds." + playerName));
         }
      }
   }

   private void removeMuteId(String playerName) {
      muteIds.remove(playerName.toLowerCase());
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

   // Keep existing helper methods
   private void notifyStaff(CommandSender sender, Player target, String reason) {
      String staffMessage = ColorUtils.translateColorCodes("&7[Staff] &e" + target.getName() +
              "&7 was muted by &e" + (sender instanceof Player ? sender.getName() : "Console") +
              "&7 for: &e" + reason);

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

   public static String getMuteId(String playerName) {
      return muteIds.getOrDefault(playerName.toLowerCase(), "N/A");
   }

   public static void unmute(UUID playerUUID) {
      Player player = Bukkit.getPlayer(playerUUID);
      if (player != null) {
         muteIds.remove(player.getName().toLowerCase());
         Config.getConfig().set("muteIds." + player.getName().toLowerCase(), null);
         Config.save();
      }
      mutedPlayers.remove(playerUUID);
      muteReasons.remove(playerUUID);
   }
}