package com.abbas.lobby.commands.AdminCommands;

import com.abbas.lobby.Utils.BanConfig;
import com.abbas.lobby.Utils.ColorUtils;
import com.abbas.lobby.Utils.Config;
import org.bukkit.BanEntry;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Ban implements CommandExecutor {

    private static Map<String, String> banIds = new HashMap<>();

    public Ban() {
        BanConfig.setupConfig();
        loadBanIds();
        startBanExpirationChecker();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("ban")) {
            if (!sender.hasPermission("lobby.ban")) {
                sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("banMessages.ban.noPermission")));
                return true;
            }

            if (args.length < 1) {
                sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("banMessages.ban.usage")));
                return true;
            }

            String targetName = args[0];
            Player target = Bukkit.getPlayer(targetName);
            if (target == null) {
                sender.sendMessage(ColorUtils.translateColorCodes(Config.getConfig().getString("banMessages.ban.playerNotFound")));
                return true;
            }

            long duration = -1;
            String reason = "Hacking";

            if (args.length > 1) {
                try {
                    duration = parseDuration(args[1]);
                    if (args.length > 2) {
                        reason = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
                    }
                } catch (NumberFormatException e) {
                    reason = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
                }
            }

            String banId = generateBanId();

            banIds.put(targetName.toLowerCase(), banId);
            saveBanId(targetName.toLowerCase(), banId);

            Date expires = (duration > 0) ? new Date(System.currentTimeMillis() + duration) : null;
            Bukkit.getBanList(org.bukkit.BanList.Type.NAME).addBan(targetName, reason, expires, sender.getName());

            String banMessage = formatBanMessage(reason, expires, banId);
            target.kickPlayer(ColorUtils.translateColorCodes(banMessage));

            String senderMessage = Config.getConfig().getString("banMessages.ban.success")
                    .replace("%player%", targetName)
                    .replace("%reason%", reason)
                    .replace("%ban_id%", banId);
            sender.sendMessage(ColorUtils.translateColorCodes(senderMessage));
            return true;
        }
        return false;
    }

    private String generateBanId() {
        return "BAN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private void saveBanId(String playerName, String banId) {
        Config.getConfig().set("banIds." + playerName, banId);
        Config.save();
    }

    private void loadBanIds() {
        if (Config.getConfig().isConfigurationSection("banIds")) {
            for (String playerName : Config.getConfig().getConfigurationSection("banIds").getKeys(false)) {
                banIds.put(playerName, Config.getConfig().getString("banIds." + playerName));
            }
        }
    }

    private void removeBanId(String playerName) {
        banIds.remove(playerName.toLowerCase());
        Config.getConfig().set("banIds." + playerName.toLowerCase(), null);
        Config.save();
    }

    private long parseDuration(String durationStr) throws NumberFormatException {
        char timeUnit = durationStr.charAt(durationStr.length() - 1);
        long duration = Long.parseLong(durationStr.substring(0, durationStr.length() - 1));
        switch (timeUnit) {
            case 's': return duration * 1000;
            case 'm': return duration * 1000 * 60;
            case 'h': return duration * 1000 * 60 * 60;
            case 'd': return duration * 1000 * 60 * 60 * 24;
            default: throw new NumberFormatException("Invalid time unit");
        }
    }

    private String formatBanMessage(String reason, Date expires, String banId) {
        String timeLeft = (expires != null) ? getTimeLeft(expires) : "Permanent";
        String forumLink = Config.getConfig().getString("banMessages.ban.forumLink");

        return "§c§lLobbyBan§7» §cBanned for " + reason + "\n"
                + "§4§oBanned by CONSOLE\n\n"
                + "§7It seems like you are using a hacked client, please disable it!\n"
                + "§cUnban in §f> §e" + timeLeft + "\n"
                + "§7Ban ID: §e" + banId + "\n\n"
                + "§7Unban application in TS or forum\n"
                + "§eTS-Ip§7 » §ccoming soon\n"
                + forumLink;
    }

    private String getTimeLeft(Date expires) {
        long diff = expires.getTime() - System.currentTimeMillis();
        long days = TimeUnit.MILLISECONDS.toDays(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff) % 24;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff) % 60;

        return days + "day(s) " + hours + "h " + minutes + "min and " + seconds + "sec";
    }

    private void startBanExpirationChecker() {
        new BukkitRunnable() {
            @Override
            public void run() {
                org.bukkit.BanList banList = Bukkit.getBanList(org.bukkit.BanList.Type.NAME);
                for (BanEntry entry : banList.getBanEntries()) {
                    Date expiration = entry.getExpiration();
                    if (expiration != null && expiration.before(new Date())) {
                        banList.pardon(entry.getTarget());
                        removeBanId(entry.getTarget());
                        Bukkit.broadcastMessage(ColorUtils.translateColorCodes("&aBan for " + entry.getTarget() + " has expired."));
                    }
                }
            }
        }.runTaskTimer(Bukkit.getPluginManager().getPlugin("Lobby"), 0L, 1200L);
    }
}
